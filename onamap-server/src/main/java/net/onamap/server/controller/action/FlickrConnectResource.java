package net.onamap.server.controller.action;

import com.google.inject.Singleton;
import com.handstandtech.flickr.server.FlickrHelper;
import com.handstandtech.flickr.server.FlickrPerm;
import com.handstandtech.flickr.shared.model.FlickrUser;
import lombok.NoArgsConstructor;
import net.onamap.server.constants.FlickrConstants;
import net.onamap.server.constants.Pages;
import net.onamap.server.constants.RequestParams;
import net.onamap.server.constants.Urls;
import net.onamap.server.dao.UserDAOImpl;
import net.onamap.server.util.RequestHelper;
import net.onamap.server.util.SessionHelper;
import net.onamap.shared.model.User;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import oauth.signpost.http.HttpParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * @author Sam Edwards
 */
@Singleton
@NoArgsConstructor
@Path("/flickr/oauth")
public class FlickrConnectResource extends HttpServlet {

	private static Logger log = LoggerFactory
			.getLogger(FlickrConnectResource.class.getName());

	/**
	 * Default Serialization UID
	 */
	private static final long serialVersionUID = 1L;


    @GET
    @Path("/connect")
    public Response connect(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        log.info("Flickr OAuth Login...");
        try {

            OAuthConsumer consumer = FlickrConstants.getOAuthConsumer();

            log.info("Consumer Key: " + consumer.getConsumerKey());
            log.info("Consumer Secret: " + consumer.getConsumerSecret());

            OAuthProvider provider = FlickrHelper
                    .getFlickrOAuthProvider(FlickrPerm.WRITE);

            log.debug("Fetching request token from Flickr...");

            String baseUrl = RequestHelper.getStringValue(request,
                    RequestParams.DOMAIN_BASE_URL);
            String authUrl = provider.retrieveRequestToken(consumer, baseUrl
                    + "/flickr/oauth/callback");

            SessionHelper.setFlickrConsumer(request, consumer);

            log.info("Auth URL: " + authUrl);

            return Response.seeOther(URI.create(authUrl)).build();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        return Response.temporaryRedirect(URI.create(Urls.HOME)).build();
    }


    @GET
    @Path("/disconnect")
    public Response disconnect(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        User user = SessionHelper.getCurrentUser(request);
        user.setFlickrInfo(null);
        UserDAOImpl userDao = new UserDAOImpl();
        Long userId = userDao.updateUser(user);
        user = userDao.findUser(userId);
        SessionHelper.setCurrentUser(user, request, response);
        return Response.temporaryRedirect(URI.create(Pages.HOME)).build();
    }

    @GET
    @Path("/callback")
    public Response get(@Context HttpServletRequest request, @Context HttpServletResponse response, @QueryParam("oauth_token") String oauth_token, @QueryParam("oauth_verifier") String oauth_verifier) {
        log.debug("Callback, flickr.");

        try {
            log.debug("OAuth Token: " + oauth_token);
            log.debug("OAuth Verifier: " + oauth_verifier);

            OAuthConsumer consumer = SessionHelper.getFlickrConsumer(request);
            OAuthProvider provider = FlickrHelper.getFlickrOAuthProvider(FlickrPerm.WRITE);

            provider.setOAuth10a(true);
            provider.retrieveAccessToken(consumer, oauth_verifier);

            // ---------------------
            String token = consumer.getToken();
            String tokenSecret = consumer.getTokenSecret();
            HttpParameters requestParams = consumer.getRequestParameters();

            log.info("CONSUMER_KEY: " + consumer.getConsumerKey());
            log.info("CONSUMER_SECRET: " + consumer.getConsumerSecret());
            log.info("TOKEN: " + token);
            log.info("TOKEN_SECRET: " + tokenSecret);
            log.info("REQUEST_PARAMS: " + requestParams.size());
            // ---------------------

            for (String paramKey : requestParams.keySet()) {
                log.info(paramKey + " [" + requestParams.get(paramKey) + "]");
            }

            FlickrHelper flickr = FlickrConstants.createFlickrHelper(token, tokenSecret);
            FlickrUser flickrUser = flickr.test_login();

            User.FlickrUserInfo flickrInfo = new User.FlickrUserInfo();
            flickrInfo.setToken(token);
            flickrInfo.setNsid(flickrUser.getNsid());
            flickrInfo.setTokenSecret(tokenSecret);
            flickrInfo.setUsername(flickrUser.getUsername().get_content());
            flickrInfo.setId(flickrUser.getId());

            User user = SessionHelper.getCurrentUser(request);
            user.setFlickrInfo(flickrInfo);

            log.info("Updating Current User with Flickr Info");
            UserDAOImpl userDao = new UserDAOImpl();
            Long userId = userDao.updateUser(user);
            user = userDao.findUser(userId);

            SessionHelper.setCurrentUser(user, request, response);

            // Clear out the Oauth consumer from the session
            SessionHelper.setFlickrConsumer(request, null);

            return Response.temporaryRedirect(URI.create(Urls.HOME)).build();
        } catch (OAuthMessageSignerException e) {
            e.printStackTrace();
        } catch (OAuthExpectationFailedException e) {
            e.printStackTrace();
        } catch (OAuthCommunicationException e) {
            e.printStackTrace();
        } catch (OAuthNotAuthorizedException e) {
            e.printStackTrace();
        }
        return Response.temporaryRedirect(URI.create(Urls.HOME)).build();
    }


}
