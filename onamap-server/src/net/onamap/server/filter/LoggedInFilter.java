package net.onamap.server.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.onamap.server.constants.Cookies;
import net.onamap.server.constants.RequestParams;
import net.onamap.server.dao.UserDAOImpl;
import net.onamap.server.util.CookieHelper;
import net.onamap.server.util.RequestHelper;
import net.onamap.server.util.SessionHelper;
import net.onamap.shared.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.utils.SystemProperty;
import com.google.inject.Singleton;

@Singleton
public class LoggedInFilter implements Filter {

	private static Logger log = LoggerFactory.getLogger(LoggedInFilter.class
			.getName());

	private FilterConfig config;

	private static UserDAOImpl userDao = new UserDAOImpl();

	// private ArrayList<String> secureLocations;

	/** Creates new SessionFilter */
	public LoggedInFilter() {
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("Instance created of " + getClass().getName());
		this.config = filterConfig;
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws java.io.IOException, ServletException {
		// Start Logged In Filter
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		Cookies.debugPrintCookies(request);

		storePageType(request);

		storeIsInProduction(request);

		String username = null;

		User currentUser = SessionHelper.getCurrentUser(request);
		if (currentUser != null) {
			CookieHelper
					.updateCurrentUserCookie(request, response, currentUser);
			username = currentUser.getUsername();
		} else {
			String cookieUserIdStr = Cookies.getCookieValue(request,
					Cookies.c_user);
			if (cookieUserIdStr != null && !cookieUserIdStr.isEmpty()) {
				try {
					Long cookieUserId = Long.parseLong(cookieUserIdStr);
					User cookieUser = userDao.findUser(cookieUserId);
					RequestHelper.set(request, RequestParams.COOKIE_USER,
							cookieUser);
					username = cookieUser.getUsername();
				} catch (Exception e) {
					// The Cookie User was not valid
				}
			}
		}

		if (username != null) {
			String domain = Cookies.getDomainString(request);
			String baseUrl = request.getScheme() + "://" + username + "."
					+ domain;
			int serverPort = request.getServerPort();
			if (serverPort != 80) {
				baseUrl += ":" + serverPort;
			}
			RequestHelper.set(request, RequestParams.CURRENT_USERS_PAGE_URL,
					baseUrl);
		}

		// Continue
		chain.doFilter(req, resp);
	}

	private void storePageType(HttpServletRequest request) {
		String userAgentString = request.getHeader("User-Agent");
		log.debug("User-Agent: " + userAgentString);
		if (userAgentString != null) {
			// TODO: Support a Mobile Version
			// if (userAgentString.contains("Android")) {
			// log.debug("ANDROID");
			// RequestHelper.setUserAgent(request, "Android");
			// } else if (userAgentString.toLowerCase().contains("iphone")) {
			// log.debug("IPHONE");
			// RequestHelper.setUserAgent(request, "iPhone");
			// } else {
			// // Normal Request
			// }
		} else {
			log.warn("User-Agent was null.");
		}
	}

	private void storeIsInProduction(HttpServletRequest request) {
		// See if we are in production mode
		try {
			if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
				RequestHelper.set(request, RequestParams.IS_PRODUCTION, true);
			}
		} catch (Exception e) {
			// Do nothing
		}
	}

	@Override
	public void destroy() {
		/*
		 * called before the Filter instance is removed from service by the web
		 * container
		 */
	}
}