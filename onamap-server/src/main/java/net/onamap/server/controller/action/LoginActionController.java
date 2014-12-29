package net.onamap.server.controller.action;

import com.google.inject.Singleton;
import com.sun.jersey.api.view.Viewable;
import net.onamap.server.constants.Pages;
import net.onamap.server.constants.Urls;
import net.onamap.server.controller.AbstractController;
import net.onamap.server.dao.UserDAOImpl;
import net.onamap.server.util.MD5Helper;
import net.onamap.server.util.RequestHelper;
import net.onamap.server.util.SessionHelper;
import net.onamap.shared.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;


@SuppressWarnings("serial")
@Singleton
@Path("/action/login")
@Produces(MediaType.TEXT_HTML)
public class LoginActionController extends AbstractController {
    private static final String PARAM_USER = "user";
    private static final String PARAM_PASS = "pass";
    private static UserDAOImpl userDao = new UserDAOImpl();

    private enum State {
        NONE, INVALID_LOGIN, SUCCESS
    }

    @GET
    public Response get(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        return Response.ok(new Viewable("/WEB-INF/pages" + Pages.LOGIN)).build();
    }

    @POST
    public Response post(@Context HttpServletRequest request, @Context HttpServletResponse response,
                         @FormParam(PARAM_USER) String emailOrUsername,
                         @FormParam(PARAM_PASS) String password) {

        State state = State.NONE;

        User user = null;
        if (!isNullOrEmpty(emailOrUsername) && !isNullOrEmpty(password)) {
            boolean isEmail = false;
            if (emailOrUsername.contains("@")) {
                isEmail = true;
            }
            if (isEmail) {
                user = userDao.findUserByEmail(emailOrUsername);
            } else {
                user = userDao.findUserByUsername(emailOrUsername);
            }

            if (user != null) {
                String passHash = MD5Helper.doDigest(password);

                if (passHash != null && passHash.equals(user.getPasswordHash())) {
                    // Successful Login
                    // Update user to set Last Login Date
                    userDao.updateUser(user);
                    state = State.SUCCESS;
                } else {
                    // Invalid Password
                    state = State.INVALID_LOGIN;
                }

            } else {
                // Invalid Email or Username
                state = State.INVALID_LOGIN;
            }

        } else {
            // TODO Invalid Email or Password
            state = State.INVALID_LOGIN;
        }

        switch (state) {
            case SUCCESS:
                SessionHelper.setCurrentUser(user, request, response);
                // Redirect to "Home"
//                return Response.temporaryRedirect(URI.create(Urls.HOME)).build();
                return Response.seeOther(URI.create(Urls.HOME)).build();
            default:
                prepareForLogin(request, emailOrUsername);

                // Invalid Login Credentials
                RequestHelper.set(request, State.INVALID_LOGIN, true);
                return Response.seeOther(URI.create(Urls.HOME)).build();
        }
    }

    public static void prepareForLogin(HttpServletRequest request,
                                       String emailOrUsername) {
        request.setAttribute(PARAM_USER, emailOrUsername);
    }
}
