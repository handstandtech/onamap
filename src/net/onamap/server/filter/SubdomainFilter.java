package net.onamap.server.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.onamap.server.constants.Cookies;
import net.onamap.server.constants.Pages;
import net.onamap.server.constants.RequestParams;
import net.onamap.server.controller.AbstractController;
import net.onamap.server.controller.action.CalculatePhotosetStatsActionController;
import net.onamap.server.dao.UserDAOImpl;
import net.onamap.server.util.RequestHelper;
import net.onamap.shared.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

@Singleton
public class SubdomainFilter extends AbstractFilter {

	private static Logger log = LoggerFactory.getLogger(SubdomainFilter.class
			.getName());

	private static UserDAOImpl userDao = new UserDAOImpl();

	/** Creates new SessionFilter */
	public SubdomainFilter() {
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws java.io.IOException, ServletException {
		// Start Logged In Filter
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		// Set Domain
		String domain = Cookies.getDomainString(request);
		String baseUrl = request.getScheme() + "://www." + domain;
		int serverPort = request.getServerPort();
		if (serverPort != 80) {
			baseUrl += ":" + serverPort;
		}
		log.debug("Domain is: "+ domain);
		RequestHelper.set(request, RequestParams.DOMAIN_BASE_URL, baseUrl);

		// Set Subdomain if Appropriate
		String subdomain = getSubdomain(request);
		if (!subdomain.isEmpty() && !subdomain.equalsIgnoreCase("www")) {
			// Honor the subdomain!
			System.out.println("SUBDOMAIN [" + subdomain + "]");
			if (subdomain != null && !subdomain.isEmpty()) {
				User user = userDao.findUserByUsername(subdomain);
				if (user != null) {
					RequestHelper.set(request, RequestParams.SUBDOMAIN,
							subdomain);
					new CalculatePhotosetStatsActionController().doGet(request,
							response);
					AbstractController.showView(request, response,
							Pages.PROFILE);
					return;
				} else {
					response.sendRedirect(baseUrl);
				}
			}
		}
		chain.doFilter(req, resp);
	}

	private String getSubdomain(HttpServletRequest request) {
		String serverName = request.getServerName();
		String domain = Cookies.getDomainString(request);

		log.info("Domain: " + domain);

		String subdomain = "";
		int serverNameLength = serverName.length();
		int domainLength = domain.length();
		if ((domainLength + 1) < serverNameLength) {
			subdomain = serverName.substring(0, serverNameLength
					- (domainLength + 1));
		}
		return subdomain;
	}
}