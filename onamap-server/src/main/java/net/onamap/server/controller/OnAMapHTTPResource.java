package net.onamap.server.controller;

import com.google.inject.Singleton;
import com.sun.jersey.api.view.Viewable;
import net.onamap.server.constants.Pages;
import net.onamap.server.constants.RequestParams;
import net.onamap.server.constants.Urls;
import net.onamap.server.controller.action.CalculatePhotosetStatsActionController;
import net.onamap.server.controller.action.LoginActionController;
import net.onamap.server.dao.UserDAOImpl;
import net.onamap.server.filter.SubdomainHelper;
import net.onamap.server.util.RequestHelper;
import net.onamap.server.util.SessionHelper;
import net.onamap.shared.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@SuppressWarnings("serial")
@Singleton
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class OnAMapHTTPResource {

    private static final Logger log = LoggerFactory.getLogger(OnAMapHTTPResource.class.getName());


    @GET
    public Response welcome(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        User currentUser = SessionHelper.getCurrentUser(request);
        User cookieUser = RequestHelper.getCookieUser(request);

        String subdomain = SubdomainHelper.getSubdomain(request);
        if (subdomain != null && !subdomain.isEmpty() && !subdomain.equalsIgnoreCase("www")) {
            Response res = showProfile(request, subdomain);
            if (res != null) {
                return res;
            }
        }

        //Subdomain didn't work out...
        if (currentUser != null) {
            return Response.ok(new Viewable(Pages.HOME)).build();
        } else if (cookieUser != null) {
            LoginActionController.prepareForLogin(request, cookieUser.getUsername());
            return Response.ok(new Viewable(Pages.LOGIN)).build();
        } else {
            return Response.ok(new Viewable(Pages.INDEX)).build();
        }
    }

    @GET
    @Path("/profile/{username}")
    public Response welcome(@Context HttpServletRequest request,
                            @Context HttpServletResponse response,
                            @PathParam("username") String subdomain) {
        Response res = showProfile(request, subdomain);
        if (res != null) {
            RequestHelper.set(request, RequestParams.USERNAME,
                    subdomain);
            return res;
        }
        return Response.temporaryRedirect(URI.create(Urls.HOME)).build();
    }

    public Response showProfile(HttpServletRequest request, String subdomain) {
        UserDAOImpl userDAO = new UserDAOImpl();
        System.out.println("SUBDOMAIN [" + subdomain + "]");
        User subdomainUser = userDAO.findUserByUsername(subdomain);
        // Honor the subdomain!
        if (subdomainUser != null) {
            RequestHelper.set(request, RequestParams.SUBDOMAIN,
                    subdomain);
            new CalculatePhotosetStatsActionController().doGet(subdomainUser, request);
            log.info("Showing Profile View: " + subdomain);
            return Response.ok(new Viewable(Pages.PROFILE_BOOTSTRAP)).build();
        }
        return null;
    }

    @GET
    @Path("/login")
    public Response login(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        return Response.ok(new Viewable(Pages.LOGIN)).build();
    }


    @GET
    @Path("/signup")
    public Response signup(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        return Response.ok(new Viewable(Pages.SIGNUP)).build();
    }

    @GET
    @Path("/terms")
    public Response terms(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        return Response.ok(new Viewable(Pages.TERMS)).build();
    }

    @GET
    @Path("/about")
    public Response about(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        return Response.ok(new Viewable(Pages.ABOUT)).build();
    }

    @GET
    @Path("/privacy")
    public Response privacy(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        return Response.ok(new Viewable(Pages.PRIVACY)).build();
    }


}
