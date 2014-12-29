package net.onamap.server.filter;

import com.google.inject.Singleton;
import net.onamap.server.constants.Cookies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

@Singleton
public class SubdomainHelper {

    private static Logger log = LoggerFactory.getLogger(SubdomainHelper.class
            .getName());

//    private static UserDAOImpl userDao = new UserDAOImpl();
//
//    /**
//     * Creates new SessionFilter
//     */
//    public SubdomainHelper() {
//    }
//
//    public void doFilter(ServletRequest req, ServletResponse resp,
//                         FilterChain chain) throws java.io.IOException, ServletException {
//        // Start Logged In Filter
//        HttpServletRequest request = (HttpServletRequest) req;
//        HttpServletResponse response = (HttpServletResponse) resp;
//
//        // Set Domain
//        String domain = Cookies.getDomainString(request);
//
//        String baseUrl = "";
//        if (domain.equalsIgnoreCase("localhost")) {
//            baseUrl = "http://localhost";
//        } else {
//            baseUrl = request.getScheme() + "://www." + domain;
//        }
//        int serverPort = request.getServerPort();
//        if (serverPort != 80) {
//            baseUrl += ":" + serverPort;
//        }
//        log.debug("Domain is: " + domain);
//        RequestHelper.set(request, RequestParams.DOMAIN_BASE_URL, baseUrl);
//
//
//        // Set Subdomain if Appropriate
//        String subdomain = getSubdomain(request);
//        if (!subdomain.isEmpty() && !subdomain.equalsIgnoreCase("www")) {
//            // Honor the subdomain!
//            System.out.println("SUBDOMAIN [" + subdomain + "]");
//            if (subdomain != null && !subdomain.isEmpty()) {
//                User user = userDao.findUserByUsername(subdomain);
//                if (user != null) {
//                    RequestHelper.set(request, RequestParams.SUBDOMAIN,
//                            subdomain);
//                    new CalculatePhotosetStatsActionController().doGet(request,
//                            response);
//                    log.info("Showing Profile View: " + subdomain);
//                    AbstractController.showView(request, response,
//                            Pages.PROFILE);
//                    return;
//                } else {
//                    log.info("Showing WWW View");
//                    response.sendRedirect(baseUrl);
//                }
//            }
//        }
//        chain.doFilter(req, resp);
//    }

    public static String getSubdomain(HttpServletRequest request) {
        String serverName = request.getServerName();
        String domain = Cookies.getDomainString(request);


        String subdomain = "";
        int serverNameLength = serverName.length();
        int domainLength = domain.length();
        if ((domainLength + 1) < serverNameLength) {
            subdomain = serverName.substring(0, serverNameLength
                    - (domainLength + 1));
        }
        log.info("subdomain: " + subdomain);
        return subdomain;
    }
}