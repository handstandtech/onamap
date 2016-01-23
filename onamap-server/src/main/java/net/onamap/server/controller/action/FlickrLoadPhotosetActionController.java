package net.onamap.server.controller.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Singleton;
import com.handstandtech.flickr.server.FlickrHelper;
import com.handstandtech.flickr.shared.model.FlickrPhoto;
import com.handstandtech.flickr.shared.model.FlickrPhotoset;
import com.handstandtech.flickr.shared.model.FlickrPhotosetInfo;
import com.handstandtech.flickr.shared.model.FlickrPhotosetInfos;
import net.onamap.server.constants.FlickrConstants;
import net.onamap.server.constants.Urls;
import net.onamap.server.controller.RequiresLoginAbstractController;
import net.onamap.server.dao.GMapsModelDAOImpl;
import net.onamap.server.dao.PhotoDAOImpl;
import net.onamap.server.dao.PhotosetDAOImpl;
import net.onamap.server.dao.UserDAOImpl;
import net.onamap.server.task.TaskHelper;
import net.onamap.server.util.SessionHelper;
import net.onamap.shared.model.FlickrUserInfo;
import net.onamap.shared.model.Photo;
import net.onamap.shared.model.Photoset;
import net.onamap.shared.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.*;

@SuppressWarnings("serial")
@Singleton
@Path("/action/flickr")
@Produces(MediaType.TEXT_HTML)
public class FlickrLoadPhotosetActionController extends
        RequiresLoginAbstractController {

    private static final long ONE_SECOND_MS = 1000;
    private static UserDAOImpl userDao = new UserDAOImpl();
    private static PhotosetDAOImpl photosetDao = new PhotosetDAOImpl();
    private static PhotoDAOImpl photoDao = new PhotoDAOImpl();
    private static GMapsModelDAOImpl gmapsDao = new GMapsModelDAOImpl();

    private static Logger log = LoggerFactory.getLogger(FlickrLoadPhotosetActionController.class);
    private static Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    @GET
    @Path("/load_photosets")
    public Response loadPhotosets(@Context HttpServletRequest request, @Context HttpServletResponse response, @QueryParam("id") String flickrPhotosetId) {
        User user = SessionHelper.getCurrentUser(request);
        FlickrUserInfo flickrInfo = user.getFlickrInfo();
        if (flickrInfo != null) {

            FlickrHelper flickr = FlickrConstants.createFlickrHelper(
                    flickrInfo.getToken(), flickrInfo.getTokenSecret());
            FlickrPhotosetInfos photosets = flickr.photosets_getList(flickrInfo
                    .getId());
            request.getSession().setAttribute("photosets", photosets);
        }

        return Response.temporaryRedirect(URI.create(Urls.HOME)).build();
    }

    @GET
    @Path("/photoset")
    public Response photoset(@Context HttpServletRequest request, @Context HttpServletResponse
            response, @QueryParam("id") String flickrPhotosetId) {
        List<String> toReverseGeocode = new ArrayList<String>();

        log.info("Processing Photoset ID: " + flickrPhotosetId);
        User user = SessionHelper.getCurrentUser(request);
        FlickrUserInfo flickrInfo = user.getFlickrInfo();
        if (flickrInfo == null || isNullOrEmpty(flickrPhotosetId)) {
            return Response.temporaryRedirect(URI.create(Urls.HOME)).build();
        }

        // Long photosetId = Long.parseLong(flickrPhotosetIdStr);
        // have flickr info, continue!
        FlickrHelper flickr = FlickrConstants.createFlickrHelper(
                flickrInfo.getToken(), flickrInfo.getTokenSecret());

        FlickrPhotosetInfo photosetInfo = getPhotosetInfo(request, flickrPhotosetId);

        // Create our photoset and add to db
        Photoset photoset = photosetDao.findPhotosetByFlickrId(flickrPhotosetId);
        if (photoset == null) {
            photoset = new Photoset();
            photoset.setId(flickrPhotosetId);
        }

        if (photosetInfo != null) {
            photoset.setTitle(photosetInfo.getTitle().get_content());
            photoset.setDescription(photosetInfo.getDescription().get_content());
            photoset.setCount(photosetInfo.getPhotos());
        }

        List<FlickrPhoto> allFlickrPhotosInPhotoset = getAllFlickrPhotosInPhotoset(flickr, photosetInfo);
        log.info("Total Photo Count from Remote Flickr: " + allFlickrPhotosInPhotoset.size());

        List<String> photoIds = getPhotoIdsFromPhotoset(allFlickrPhotosInPhotoset);

        photoset.setUserId(user.getId());
        photoset.setPhotoIds(photoIds);
        photosetDao.updatePhotoset(photoset);

        user.setFlickrPhotosetId(flickrPhotosetId);
        Long userId = userDao.updateUser(user);
        user = userDao.findUser(userId);
        SessionHelper.setCurrentUser(user, request, response);

        Collection<Photo> photosByIdInDBList = photoDao.getPhotosByIds(photoIds);
        Map<String, Photo> photosByIdInDBMap = new HashMap<>();
        for (Photo photo : photosByIdInDBList) {
            photosByIdInDBMap.put(photo.getId(), photo);
        }

        List<Photo> photosToUpdate = new ArrayList<Photo>();
        for (FlickrPhoto remoteFlickrPhoto : allFlickrPhotosInPhotoset) {
            String flickrPhotoId = remoteFlickrPhoto.getId();

            // Get the photo out of the database
            Photo photoFromLocalDB = photosByIdInDBMap.get(flickrPhotoId);

            final boolean photoInDBWasNull = (photoFromLocalDB == null);
            final boolean photoLocationWasNull = (photoFromLocalDB.getCityStateCountry() == null);
            if (photoInDBWasNull || photoLocationWasNull) {
                //Add to reverse geocode
                toReverseGeocode.add(flickrPhotoId);
            }

            if (photoInDBWasNull) {
                //Not in DB
                photoFromLocalDB = new Photo(remoteFlickrPhoto);
                photosToUpdate.add(photoFromLocalDB);
            } else {
                log.info("");
                final long localDBLastUpdateTime = photoFromLocalDB.getFlickrLastUpdatedTime() * ONE_SECOND_MS;
                final long remoteFlickrPhotoLastUpdateTime = remoteFlickrPhoto.getLastupdate() * ONE_SECOND_MS;
                final boolean isOutDated = (localDBLastUpdateTime <= remoteFlickrPhotoLastUpdateTime);
                if (isOutDated) {
                    log.info("Photo exists in DB and is outdated.  Photo to be updated: flickrPhotoId: " + flickrPhotoId + " | localDBLastUpdateTime: " + new Date(localDBLastUpdateTime) + " | remoteFlickrPhotoLastUpdateTime: " + new Date(remoteFlickrPhotoLastUpdateTime) + " setting flickPhoto as remote info, remoteFlickrPhoto: " + gson.toJson(remoteFlickrPhoto));
                    //In DB, but outdated.
                    photoFromLocalDB.setFlickrPhoto(remoteFlickrPhoto);
                    photosToUpdate.add(photoFromLocalDB);
                }
            }

        }

        log.info("Updating " + photosToUpdate.size() + " photos.");
        photoDao.updatePhotos(photosToUpdate);
        request.setAttribute("photos", allFlickrPhotosInPhotoset);

        for (String photoId : toReverseGeocode) {
            TaskHelper.queueReverseGeocode(photoId);
        }

        return Response.temporaryRedirect(URI.create(Urls.HOME)).build();
    }

    private List<String> getPhotoIdsFromPhotoset(final List<FlickrPhoto> allFlickrPhotosInPhotoset) {
        final List<String> photoIds = new ArrayList<String>();
        for (final FlickrPhoto flickrPhoto : allFlickrPhotosInPhotoset) {
            photoIds.add(flickrPhoto.getId());
        }
        return photoIds;
    }

    private List<FlickrPhoto> getAllFlickrPhotosInPhotoset(FlickrHelper flickr, FlickrPhotosetInfo photosetInfo) {
        List<FlickrPhoto> flickrPhotos = new ArrayList<FlickrPhoto>();
        Integer photosCount = photosetInfo.getPhotos();
        Integer count = 0;
        Integer page = 1;
        Integer per_page = 500;
        while (count < photosCount) {
            FlickrPhotoset flickrPhotoset = flickr.photosets_getPhotos(photosetInfo.getId(), page, per_page);
            List<FlickrPhoto> photosInPage = flickrPhotoset.getPhotos();
            flickrPhotos.addAll(photosInPage);
            page++;
            count += per_page;
        }
        return flickrPhotos;
    }

    private List<FlickrPhoto> getPhotosInPage(FlickrHelper flickr, String flickrPhotosetId, Integer page, Integer per_page) {
        FlickrPhotoset flickrPhotoset = flickr
                .photosets_getPhotos(flickrPhotosetId, page, per_page);
        log.info("photos: " + gson.toJson(flickrPhotoset));
        List<FlickrPhoto> flickrPhotos = flickrPhotoset.getPhotos();
        return flickrPhotos;
    }

    private FlickrPhotosetInfo getPhotosetInfo(HttpServletRequest request, String photosetId) {
        FlickrPhotosetInfos photosetInfos = (FlickrPhotosetInfos) request.getSession().getAttribute("photosets");
        List<FlickrPhotosetInfo> photosets = photosetInfos.getPhotoset();
        if (photosets != null && !photosets.isEmpty()) {
            for (FlickrPhotosetInfo currInfo : photosets) {
                if (currInfo.getId().equalsIgnoreCase(photosetId)) {
                    return currInfo;
                }
            }
        }
        return null;
    }
}
