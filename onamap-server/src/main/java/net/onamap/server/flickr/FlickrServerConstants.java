package net.onamap.server.flickr;

import com.handstandtech.restclient.server.auth.oauth.OAuthConsumerInfoManager.OAuthConsumerInfoSet;
import net.onamap.server.constants.Urls;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlickrServerConstants extends ServerConstants {
	protected static Logger log = LoggerFactory.getLogger(FlickrAPIOAuth.class
			.getName());

	public static FlickrServerConstants INSTANCE = new FlickrServerConstants();


	private FlickrServerConstants() {
		log.info("Initializing Flickr OAuth Constants");
		oauthManager.addConsumerInfoSet(getLocalhostConsumerInfo());
	}

	private static OAuthConsumerInfoSet getLocalhostConsumerInfo() {
		OAuthConsumerInfoSet localhost = new OAuthConsumerInfoSet();
		localhost.setName("Flickr - 127.0.0.1:8888");
		localhost.setCallbackUri(Urls.FLICKR_OAUTH_CALLBACK_URI);
		localhost.setConsumerKey("d32f7335f0a3dd8c63f4f3fc312a37bc");
		localhost
				.setConsumerSecret("f6d7142634e935d1");
		localhost.setUrlPrefix("localhost");
		return localhost;
	}

	public static OAuthProvider getFlickrOAuthProvider() {
		String baseUrl = "http://www.flickr.com/services/oauth/";
		OAuthProvider provider = new DefaultOAuthProvider(baseUrl
				+ "request_token", baseUrl + "access_token", baseUrl
				+ "authorize");
		return provider;
	}

	public OAuthConsumer getOAuthConsumer(String requestUrl) {
		String consumerKey = FlickrServerConstants.INSTANCE
				.getClientId(requestUrl);
		String consumerSecret = FlickrServerConstants.INSTANCE
				.getClientSecret(requestUrl);
		return new DefaultOAuthConsumer(consumerKey, consumerSecret);
	}
}
