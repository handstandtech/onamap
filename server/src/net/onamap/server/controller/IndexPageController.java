package net.onamap.server.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.onamap.server.constants.Pages;
import net.onamap.server.controller.action.LoginActionController;
import net.onamap.server.util.RequestHelper;
import net.onamap.server.util.SessionHelper;
import net.onamap.shared.model.User;

import com.google.inject.Singleton;

@SuppressWarnings("serial")
@Singleton
public class IndexPageController extends AbstractController {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		User currentUser = SessionHelper.getCurrentUser(request);
		User cookieUser = RequestHelper.getCookieUser(request);
		if (currentUser != null) {
			showView(request, response, Pages.HOME);
		} else if (cookieUser != null) {
			LoginActionController.prepareForLogin(request, cookieUser.getUsername());
			showView(request, response, Pages.LOGIN);
		} else {
			showView(request, response, Pages.INDEX);
		}
	}

}
