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
public class AboutPageController extends AbstractController {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		showView(request, response, Pages.ABOUT);
	}

}
