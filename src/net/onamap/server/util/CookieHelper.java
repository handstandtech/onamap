package net.onamap.server.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.onamap.server.constants.Cookies;
import net.onamap.shared.model.User;

public class CookieHelper {

	public static void updateCurrentUserCookie(HttpServletRequest request,
			HttpServletResponse response, User user) {
		if (user == null) {
			Cookies.expireCookie(request, response, Cookies.c_user);
		} else {
			Cookies.settingCookie(request, response, Cookies.c_user, user.getId()
					.toString());
		}
	}

	
}
