package net.onamap.server.controller.action;

import com.google.inject.Singleton;
import com.sun.jersey.api.view.Viewable;
import net.onamap.server.constants.Pages;
import net.onamap.server.constants.Urls;
import net.onamap.server.controller.AbstractController;
import net.onamap.server.dao.GMapsModelDAOImpl;
import net.onamap.server.dao.PhotoDAOImpl;
import net.onamap.server.dao.PhotosetDAOImpl;
import net.onamap.server.dao.UserDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.net.URI;

@SuppressWarnings("serial")
@Singleton
@Path("/admin")
public class AdminResource extends AbstractController {

    private static Logger log = LoggerFactory
            .getLogger(FlickrLoadPhotosetActionController.class.getName());

    @GET
    @Path("/")
    public Response admin(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        return Response.ok(new Viewable(Pages.ADMIN)).build();
    }

    @POST
    @Path("/delete")
    public Response deleteGet(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        log.info("Delete");

        new PhotosetDAOImpl().deleteAllPhotosets();
        new PhotoDAOImpl().deleteAllPhotos();
        new UserDAOImpl().deleteAllUsers();
        new GMapsModelDAOImpl().deleteAll();

        return Response.seeOther(URI.create(Urls.ADMIN)).build();
    }

}
