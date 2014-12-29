package net.onamap.server.controller.action;

import com.google.inject.Singleton;
import com.handstandtech.flickr.server.FlickrHelper;
import com.handstandtech.flickr.shared.model.FlickrPhoto;
import com.handstandtech.flickr.shared.model.FlickrPhotoset;
import com.handstandtech.flickr.shared.model.FlickrPhotosetInfo;
import com.handstandtech.flickr.shared.model.FlickrPhotosetInfos;
import net.onamap.server.constants.FlickrConstants;
import net.onamap.server.constants.Urls;
import net.onamap.server.controller.RequiresLoginAbstractController;
import net.onamap.server.dao.PhotoDAOImpl;
import net.onamap.server.dao.PhotosetDAOImpl;
import net.onamap.server.dao.UserDAOImpl;
import net.onamap.server.task.TaskHelper;
import net.onamap.server.util.SessionHelper;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
@Singleton
@Path("/action/flickr")
@Produces(MediaType.TEXT_HTML)
public class FlickrLoadPhotosetActionController extends
        RequiresLoginAbstractController {

    private static UserDAOImpl userDao = new UserDAOImpl();
    private static PhotosetDAOImpl photosetDao = new PhotosetDAOImpl();
    private static PhotoDAOImpl photoDao = new PhotoDAOImpl();

    private static Logger log = LoggerFactory
            .getLogger(FlickrLoadPhotosetActionController.class.getName());

    @GET
    @Path("/load_photosets")
    public Response loadPhotosets(@Context HttpServletRequest request, @Context HttpServletResponse response, @QueryParam("id") String flickrPhotosetId) {
        User user = SessionHelper.getCurrentUser(request);
        User.FlickrUserInfo flickrInfo = user.getFlickrInfo();
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

        log.info("PHOTOSET: " + flickrPhotosetId);
        User user = SessionHelper.getCurrentUser(request);
        User.FlickrUserInfo flickrInfo = user.getFlickrInfo();
        if (flickrInfo != null && !isNullOrEmpty(flickrPhotosetId)) {

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
            photoset.setUserId(user.getId());
            photosetDao.updatePhotoset(photoset);
            user.setFlickrPhotosetId(flickrPhotosetId);

            Long userId = userDao.updateUser(user);
            user = userDao.findUser(userId);
            SessionHelper.setCurrentUser(user, request, response);

            List<FlickrPhoto> allFlickrPhotosInPhotoset = getAllFlickrPhotosInPhotoset(flickr, photosetInfo);

            List<String> photoIds = new ArrayList<String>();
            for (FlickrPhoto flickrPhoto : allFlickrPhotosInPhotoset) {
                photoIds.add(flickrPhoto.getId());
            }

            Map<String, Photo> photosByIdInDB = photoDao.getPhotosByIds(photoIds);
            List<Photo> photosToUpdate = new ArrayList<Photo>();
            for (FlickrPhoto flickrPhoto : allFlickrPhotosInPhotoset) {
                String flickrPhotoId = flickrPhoto.getId();

                // Get the photo out of the database
                Photo photoFromLocalDB = photosByIdInDB.get(flickrPhotoId);
                if (photoFromLocalDB == null) {
                    //Not in DB
                    photoFromLocalDB = new Photo(flickrPhoto);
                    photoFromLocalDB.setFlickrPhotosetId(flickrPhotosetId);
                    toReverseGeocode.add(flickrPhotoId);
                } else {
                    //In DB, but outdated.
                    // If it's been updated more recently..
//                    boolean coordsChanged = flickrPhoto.getLatitude() != photoFromLocalDB.getLatitude() || flickrPhoto.getLongitude() != photoFromLocalDB.getLongitude();
                    Date localDBLastUpdateDate = photoFromLocalDB.getLastUpdated();
                    boolean haveUpToDateVersion = localDBLastUpdateDate != null && flickrPhoto.getLastupdate() < localDBLastUpdateDate.getTime();
                    if (haveUpToDateVersion == false) {
                        photoFromLocalDB.setFlickrPhoto(flickrPhoto);
                        photoFromLocalDB.setFlickrPhotosetId(flickrPhotosetId);
                        toReverseGeocode.add(flickrPhotoId);
                    }
                }
                photosToUpdate.add(photoFromLocalDB);
            }

            photoDao.updatePhotos(photosToUpdate);
            request.setAttribute("photos", allFlickrPhotosInPhotoset);

            for (String photoId : toReverseGeocode) {
                // TODO Task to Reverse Geocode
                TaskHelper.queueReverseGeocode(photoId);
            }
        }

        return Response.temporaryRedirect(URI.create(Urls.HOME)).
                build();
    }

    private List<FlickrPhoto> getAllFlickrPhotosInPhotoset(FlickrHelper flickr, FlickrPhotosetInfo photosetInfo) {
        log.info("getAllFlickrPhotosInPhotoset: " + photosetInfo);
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
        System.out.println("photos: " + flickrPhotoset);
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
