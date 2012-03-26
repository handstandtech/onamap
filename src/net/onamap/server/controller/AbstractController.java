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
public abstract class AbstractController extends HttpServlet {

	protected static Logger log = LoggerFactory.getLogger(LoggedInFilter.class
			.getName());

	
	public static void showView(HttpServletRequest request,
			HttpServletResponse response, String destination) {
		System.out.println("View [" + destination + "]\n");

		try {
			RequestDispatcher dispatcher = request
					.getRequestDispatcher(destination);
			dispatcher.include(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void forward(HttpServletRequest request,
			HttpServletResponse response, String destination) {
		System.out.println("Forwarding Request [" + destination + "]\n");

		try {
			RequestDispatcher dispatcher = request
					.getRequestDispatcher(destination);
			dispatcher.forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isNullOrEmpty(String string) {
		return string == null || string.isEmpty();
	}

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
