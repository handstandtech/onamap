package net.onamap.server.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.onamap.server.constants.Pages;
import net.onamap.server.filter.LoggedInFilter;

@SuppressWarnings("serial")
public abstract class RequiresLoginAbstractController extends AbstractController {

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		showView(request, response, Pages.ERROR);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		showView(request, response, Pages.ERROR);
	}

	@Override
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		showView(request, response, Pages.ERROR);
	}

	@Override
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		showView(request, response, Pages.ERROR);
	}
}
