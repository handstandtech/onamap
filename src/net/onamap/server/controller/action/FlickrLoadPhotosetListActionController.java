package net.onamap.server.controller.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.onamap.server.controller.AbstractController;
import net.onamap.server.dao.PhotoDAOImpl;
import net.onamap.server.dao.PhotosetDAOImpl;
import net.onamap.server.dao.UserDAOImpl;
import net.onamap.server.util.SessionHelper;
import net.onamap.shared.model.User;
import net.onamap.shared.model.User.FlickrUserInfo;
import oauth.signpost.OAuthConsumer;

import com.google.inject.Singleton;
import com.handstandtech.flickr.server.FlickrHelper;
import com.handstandtech.flickr.shared.model.FlickrPhotosetInfos;

@SuppressWarnings("serial")
@Singleton
public class FlickrLoadPhotosetListActionController extends AbstractController {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		// new UserDAOImpl().deleteAllUsers();
		// new PhotoDAOImpl().deleteAllPhotos();
		// new PhotosetDAOImpl().deleteAllPhotosets();

		User user = SessionHelper.getCurrentUser(request);
		FlickrUserInfo flickrInfo = user.getFlickrInfo();
		if (flickrInfo != null) {
			OAuthConsumer consumer = FlickrHelper.getOAuthConsumer();
			consumer.setTokenWithSecret(flickrInfo.getToken(),
					flickrInfo.getTokenSecret());
			FlickrHelper flickr = new FlickrHelper(consumer);
			FlickrPhotosetInfos photosets = flickr.photosets_getList(flickrInfo
					.getId());
			request.getSession().setAttribute("photosets", photosets);
		}
		response.sendRedirect("/");
	}

}
