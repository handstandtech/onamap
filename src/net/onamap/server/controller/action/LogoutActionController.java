package net.onamap.server.controller.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.onamap.server.constants.Urls;
import net.onamap.server.controller.AbstractController;
import net.onamap.server.util.SessionHelper;

import com.google.inject.Singleton;

@SuppressWarnings("serial")
@Singleton
public class LogoutActionController extends AbstractController {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		System.out.println("LOGOUT");
		log.debug("curent user: " + SessionHelper.getCurrentUser(request));
		request.getSession().setAttribute("photosets", null);
		SessionHelper.setCurrentUser(null, request, response);
		response.sendRedirect(Urls.HOME);
	}
}
