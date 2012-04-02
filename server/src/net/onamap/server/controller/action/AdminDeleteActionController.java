package net.onamap.server.controller.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.onamap.server.constants.Urls;
import net.onamap.server.controller.AbstractController;
import net.onamap.server.dao.PhotoDAOImpl;
import net.onamap.server.dao.PhotosetDAOImpl;
import net.onamap.server.dao.UserDAOImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

@SuppressWarnings("serial")
@Singleton
public class AdminDeleteActionController extends AbstractController {

	private static Logger log = LoggerFactory
			.getLogger(FlickrLoadPhotosetActionController.class.getName());

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		log.info("Delete");

		new PhotosetDAOImpl().deleteAllPhotosets();
		new PhotoDAOImpl().deleteAllPhotos();
		new UserDAOImpl().deleteAllUsers();
		
		response.sendRedirect(Urls.ADMIN);
	}

}
