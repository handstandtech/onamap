package net.onamap.server.controller.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.onamap.server.constants.Pages;
import net.onamap.server.controller.AbstractController;
import net.onamap.server.dao.UserDAOImpl;
import net.onamap.server.util.SessionHelper;
import net.onamap.shared.model.User;

import com.google.inject.Singleton;

@SuppressWarnings("serial")
@Singleton
public class FlickrDisconnectActionController extends AbstractController {

	private static UserDAOImpl userDao = new UserDAOImpl();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		User user = SessionHelper.getCurrentUser(request);
		user.setFlickrInfo(null);
		Long userId = userDao.updateUser(user);
		user = userDao.findUser(userId);
		SessionHelper.setCurrentUser(user, request, response);
		response.sendRedirect("/");
	}
}
