package net.onamap.server.controller.action;

import com.google.inject.Singleton;
import net.onamap.server.constants.Urls;
import net.onamap.server.controller.AbstractController;
import net.onamap.server.util.SessionHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@SuppressWarnings("serial")
@Singleton
@Path("/action/logout")
@Produces(MediaType.TEXT_HTML)
public class LogoutActionController extends AbstractController {

    @GET
    public Response get(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        System.out.println("LOGOUT");
        log.debug("curent user: " + SessionHelper.getCurrentUser(request));
        request.getSession().setAttribute("photosets", null);
        SessionHelper.setCurrentUser(null, request, response);
        return Response.temporaryRedirect(URI.create(Urls.HOME)).build();
    }
}
