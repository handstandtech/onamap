package net.onamap.server.controller.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Singleton;
import com.sun.jersey.api.view.Viewable;
import net.onamap.server.constants.Pages;
import net.onamap.server.constants.Urls;
import net.onamap.server.controller.AbstractController;
import net.onamap.server.dao.GMapsModelDAOImpl;
import net.onamap.server.dao.PhotoDAOImpl;
import net.onamap.server.dao.PhotosetDAOImpl;
import net.onamap.server.dao.UserDAOImpl;
import net.onamap.server.objectify.OfyFactory;
import net.onamap.shared.model.GMapsModel;
import net.onamap.shared.model.Photo;
import net.onamap.shared.model.Photoset;
import net.onamap.shared.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
@Singleton
@Path("/admin")
public class AdminResource extends AbstractController {

    private static Logger log = LoggerFactory
            .getLogger(FlickrLoadPhotosetActionController.class.getName());

    private static Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    @GET
    @Path("/")
    public Response admin(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        List<String> types = new ArrayList<>();
        for (Class clazz : OfyFactory.MODEL_TYPES) {
            types.add(clazz.getSimpleName());
        }

        request.setAttribute("types", types);
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

    @GET
    @Path("/models/{type}")
    public Response models(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("type") String type) {

        List models = new ArrayList();
        if (type.equalsIgnoreCase(GMapsModel.class.getSimpleName())) {
            models = new GMapsModelDAOImpl().getAll();
        } else if (type.equalsIgnoreCase(User.class.getSimpleName())) {
            models = new UserDAOImpl().getAllUsers();
        } else if (type.equalsIgnoreCase(Photo.class.getSimpleName())) {
            models = new PhotoDAOImpl().getAll();
        } else if (type.equalsIgnoreCase(Photoset.class.getSimpleName())) {
            models = new PhotosetDAOImpl().getAll();
        }

        request.setAttribute("models", gson.toJson(models));

        return Response.ok(new Viewable(Pages.ADMIN_MODELS)).build();
    }


}
