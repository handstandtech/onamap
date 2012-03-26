package net.onamap.server.controller.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.onamap.server.constants.Pages;
import net.onamap.server.constants.RequestParams;
import net.onamap.server.controller.RequiresLoginAbstractController;
import net.onamap.server.dao.PhotoDAOImpl;
import net.onamap.server.dao.PhotosetDAOImpl;
import net.onamap.server.dao.UserDAOImpl;
import net.onamap.server.task.TaskHelper;
import net.onamap.server.util.RequestHelper;
import net.onamap.server.util.SessionHelper;
import net.onamap.shared.model.Photo;
import net.onamap.shared.model.Photoset;
import net.onamap.shared.model.User;
import net.onamap.shared.model.User.FlickrUserInfo;
import oauth.signpost.OAuthConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;
import com.handstandtech.flickr.server.FlickrHelper;
import com.handstandtech.flickr.shared.model.FlickrPhoto;
import com.handstandtech.flickr.shared.model.FlickrPhotoset;

@SuppressWarnings("serial")
@Singleton
public class FlickrLoadPhotosetActionController extends
		RequiresLoginAbstractController {

	private static UserDAOImpl userDao = new UserDAOImpl();
	private static PhotosetDAOImpl photosetDao = new PhotosetDAOImpl();
	private static PhotoDAOImpl photoDao = new PhotoDAOImpl();

	private static Logger log = LoggerFactory
			.getLogger(FlickrLoadPhotosetActionController.class.getName());

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String flickrPhotosetId = request.getParameter("id");

		List<Long> toReverseGeocode = new ArrayList<Long>();

		log.info("PHOTOSET: " + flickrPhotosetId);
		User user = SessionHelper.getCurrentUser(request);
		FlickrUserInfo flickrInfo = user.getFlickrInfo();
		if (flickrInfo != null && !isNullOrEmpty(flickrPhotosetId)) {

			// Long photosetId = Long.parseLong(flickrPhotosetIdStr);
			// have flickr info, continue!
			OAuthConsumer consumer = FlickrHelper.getOAuthConsumer();
			consumer.setTokenWithSecret(flickrInfo.getToken(),
					flickrInfo.getTokenSecret());
			FlickrHelper flickr = new FlickrHelper(consumer);

			// Create our photoset and add to db
			Photoset photoset = photosetDao
					.findPhotosetByFlickrId(flickrPhotosetId);
			Long photosetId = null;
			if (photoset != null) {
				photosetId = photoset.getId();
			} else {
				photoset = new Photoset();
				photoset.setFlickrPhotosetId(flickrPhotosetId);
				photoset.setUserId(user.getId());
				photosetId = photosetDao.updatePhotoset(photoset);
			}
			RequestHelper.set(request, RequestParams.PHOTOSET_ID, photosetId);
			user.setPhotosetId(photosetId);

			Long userId = userDao.updateUser(user);
			user = userDao.findUser(userId);
			SessionHelper.setCurrentUser(user, request, response);

			FlickrPhotoset flickrPhotoset = flickr
					.photosets_getPhotos(flickrPhotosetId);
			System.out.println("photos: " + flickrPhotoset);

			List<FlickrPhoto> flickrPhotos = flickrPhotoset.getPhotos();
			for (FlickrPhoto flickrPhoto : flickrPhotos) {

				// Get the photo out of the database
				Photo photo = photoDao.findPhotoByFlickrId(flickrPhoto.getId());
				if (photo == null) {
					photo = new Photo();
					photo.setFlickrPhoto(flickrPhoto);
					photo.setPhotosetId(photosetId);
					photo.setFlickrPhotosetId(flickrPhotosetId);
					Long photoId = photoDao.updatePhoto(photo);
					toReverseGeocode.add(photoId);
				} else {
					FlickrPhoto currFlickrPhoto = photo.getFlickrPhoto();
					// If it's been updated more recently..
					if (flickrPhoto.getLastupdate().getTime() > currFlickrPhoto
							.getLastupdate().getTime()) {
						photo.setFlickrPhoto(flickrPhoto);
						photo.setPhotosetId(photosetId);
						photo.setFlickrPhotosetId(flickrPhotosetId);
						Long photoId = photoDao.updatePhoto(photo);
						toReverseGeocode.add(photoId);
					}
				}

			}
			request.setAttribute("photos", flickrPhotos);

			for (Long photoId : toReverseGeocode) {
				// TODO Task to Reverse Geocode
				TaskHelper.queueReverseGeocode(photoId);
			}
		}

		showView(request, response, Pages.HOME);
	}
}
