package net.onamap.server.util;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import net.onamap.server.constants.RequestParams;
import net.onamap.shared.model.User;

public class RequestHelper {

	public static void set(HttpServletRequest request, Object param,
			Serializable value) {
		request.setAttribute(param.toString(), value);
	}

	public static String getStringValue(HttpServletRequest request,
			RequestParams param) {
		return (String) request.getAttribute(param.toString());
	}

	public static Object get(HttpServletRequest request, RequestParams param) {
		return request.getAttribute(param.toString());
	}

	public static User getCookieUser(HttpServletRequest request) {
		return (User) get(request, RequestParams.COOKIE_USER);
	}

	public static Long getLongValue(HttpServletRequest request,
			RequestParams param) {
		return (Long) request.getAttribute(param.toString());
	}

}
