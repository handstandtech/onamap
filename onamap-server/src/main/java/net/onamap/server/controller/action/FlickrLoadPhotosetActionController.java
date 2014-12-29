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
import java.util.List;

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

            // Create our photoset and add to db
            Photoset photoset = photosetDao
                    .findPhotosetByFlickrId(flickrPhotosetId);
            String photosetId = null;
            if (photoset != null) {
                photosetId = photoset.getId();
            } else {
                photoset = new Photoset();
                photoset.setId(flickrPhotosetId);
                setPhotosetTitleAndDescription(request, photoset);
                photoset.setUserId(user.getId());
                photosetId = photosetDao.updatePhotoset(photoset);
            }
            user.setFlickrPhotosetId(flickrPhotosetId);

            Long userId = userDao.updateUser(user);
            user = userDao.findUser(userId);
            SessionHelper.setCurrentUser(user, request, response);

            FlickrPhotoset flickrPhotoset = flickr
                    .photosets_getPhotos(flickrPhotosetId);
            System.out.println("photos: " + flickrPhotoset);

            List<FlickrPhoto> flickrPhotos = flickrPhotoset.getPhotos();
            for (FlickrPhoto flickrPhoto : flickrPhotos) {

                // Get the photo out of the database
                Photo photoFromLocalDB = photoDao.findPhotoByFlickrId(flickrPhoto.getId());
                if (photoFromLocalDB == null) {
                    photoFromLocalDB = new Photo(flickrPhoto);
                    photoFromLocalDB.setFlickrPhotosetId(flickrPhotosetId);
                    String photoId = photoDao.updatePhoto(photoFromLocalDB);
                    toReverseGeocode.add(photoId);
                } else {
                    // If it's been updated more recently..
                    if (flickrPhoto.getLatitude() != photoFromLocalDB.getLatitude() || flickrPhoto.getLongitude() != photoFromLocalDB.getLongitude()) {
                        log.info(photoFromLocalDB.getFlickrLastUpdatedTime() + " vs " + flickrPhoto.getLastupdate());
//                    if (flickrPhoto.getLastupdate() > photoFromLocalDB.getLastupdate()) {
                        photoFromLocalDB.setFlickrPhoto(flickrPhoto);
                        photoFromLocalDB.setFlickrPhotosetId(flickrPhotosetId);
                        String photoId = photoDao.updatePhoto(photoFromLocalDB);
                        toReverseGeocode.add(photoId);
                    }
                }

            }
            request.setAttribute("photos", flickrPhotos);

            for (String photoId : toReverseGeocode) {
                // TODO Task to Reverse Geocode
                TaskHelper.queueReverseGeocode(photoId);
            }
        }

        return Response.temporaryRedirect(URI.create(Urls.HOME)).
                build();
    }

    private void setPhotosetTitleAndDescription(HttpServletRequest request, Photoset photoset) {
        FlickrPhotosetInfos photosetInfos = (FlickrPhotosetInfos) request.getSession().getAttribute("photosets");
        List<FlickrPhotosetInfo> photosets = photosetInfos.getPhotoset();
        if (photosets != null && !photosets.isEmpty()) {
            for (FlickrPhotosetInfo currInfo : photosets) {
                if (currInfo.getId().equalsIgnoreCase(photoset.getId())) {
                    photoset.setTitle(currInfo.getTitle().get_content());
                    photoset.setDescription(currInfo.getDescription().get_content());
                }
            }
        }
    }
}
