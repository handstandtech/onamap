package net.onamap.server.constants;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public enum Cookies {
	c_user;

	public static void debugPrintCookies(HttpServletRequest request) {
		Cookie[] allcookies = request.getCookies();
		if (allcookies != null) {
			for (Cookie cookie : allcookies) {
				System.out
						.println(String
								.format("Comment: %s | Domain: %s | Max Age: %s | Name: %s | Path: %s | Secure: %s | Value: %s | Version: %s",
										cookie.getComment(),
										cookie.getDomain(), cookie.getMaxAge(),
										cookie.getName(), cookie.getPath(),
										cookie.getSecure(), cookie.getValue(),
										cookie.getVersion()));

			}
		}
	}

	public static void settingCookie(HttpServletRequest request,
			HttpServletResponse response, Cookies cookieName, String cookieValue) {

		Cookie currentUserCookie = new Cookie(cookieName.name(), cookieValue);
		String domain = getDomainString(request);
		currentUserCookie.setDomain("." + domain);
		currentUserCookie.setPath("/");
		response.addCookie(currentUserCookie);

		System.out.println("SETTING COOKIE: [" + cookieName.name() + "|"
				+ cookieValue + "|" + domain + "]");
	}

	public static void expireCookie(HttpServletRequest request,
			HttpServletResponse response, Cookies cookieName) {
		String domain = getDomainString(request);
		Cookie currentUserCookie = new Cookie(cookieName.name(), "");
		currentUserCookie.setMaxAge(0);
		currentUserCookie.setDomain("." + domain);
		currentUserCookie.setPath("/");
		response.addCookie(currentUserCookie);
		System.out.println("EXPIRING COOKIE: [" + cookieName.name() + "|"
				+ domain + "]");
	}

	public static String getDomainString(HttpServletRequest request) {
		return getDomainString(request.getServerName());

	}

	public static String getDomainString(String serverName) {
		String domain = "";

		// Special Case for Appspot
		String APPSPOT_COM = ".appspot.com";
		if (serverName.endsWith(APPSPOT_COM)) {
			int idx = serverName.length() - APPSPOT_COM.length();
			String appspotPrefix = serverName.substring(0, idx);

			int idx2 = appspotPrefix.lastIndexOf(".");
			if (idx2 != -1) {
				domain = appspotPrefix.substring(idx2 + 1,
						appspotPrefix.length())
						+ APPSPOT_COM;
			} else {
				domain = appspotPrefix + APPSPOT_COM;
			}
		} else {
			// Otherwise
			int idx = serverName.lastIndexOf(".");
			if (idx != -1) {
				int idx2 = serverName.substring(0, idx).lastIndexOf(".");
				if (idx2 != -1) {
					domain = serverName
							.substring(idx2 + 1, serverName.length());
				} else {
					domain = serverName;
				}
			} else {
				domain = serverName;
			}
		}
		return domain;
	}

	public static String getCookieValue(HttpServletRequest request,
			Cookies cookieName) {
		Cookie[] allcookies = request.getCookies();
		if (allcookies != null) {
			for (Cookie cookie : allcookies) {
				if (cookie.getName().equals(cookieName.name())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
}
