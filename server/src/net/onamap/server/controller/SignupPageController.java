package net.onamap.server.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.onamap.server.constants.Pages;

import com.google.inject.Singleton;

@SuppressWarnings("serial")
@Singleton
public class SignupPageController extends AbstractController {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		showView(request, response, Pages.SIGNUP);
	}

}
