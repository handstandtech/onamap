package net.onamap.server.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.onamap.server.constants.SessionParams;
import net.onamap.server.dao.UserDAOImpl;
import net.onamap.shared.model.User;
import oauth.signpost.OAuthConsumer;

public class SessionHelper {

	public static User getCurrentUser(HttpServletRequest request) {
		return (User) getSession(request).getAttribute(
				SessionParams.CURRENT_USER.name());
	}

	public static void setCurrentUser(User user, HttpServletRequest request,
			HttpServletResponse response) {
		setSessionAttribute(request, SessionParams.CURRENT_USER.name(), user);
		CookieHelper.updateCurrentUserCookie(request, response, user);
	}

	private static void setSessionAttribute(HttpServletRequest request,
			String name, Object obj) {
		HttpSession session = getSession(request);
		synchronized (session) {
			session.setAttribute(name, obj);
		}
	}

	public static void setFlickrConsumer(HttpServletRequest request,
			OAuthConsumer consumer) {
		getSession(request).setAttribute(SessionParams.FLICKR_CONSUMER.name(),
				consumer);
	}

	public static OAuthConsumer getFlickrConsumer(HttpServletRequest request) {
		return (OAuthConsumer) getSession(request).getAttribute(
				SessionParams.FLICKR_CONSUMER.name());
	}

	private static HttpSession getSession(HttpServletRequest request) {
		return request.getSession();
	}

}
