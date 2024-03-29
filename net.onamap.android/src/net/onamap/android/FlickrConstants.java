package net.onamap.android;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import com.handstandtech.flickr.server.FlickrHelper;
import com.handstandtech.flickr.server.OAuthAuthenticatorProvider;
import com.handstandtech.flickr.server.RESTClientProvider;
import com.handstandtech.restclient.server.RESTClient;
import com.handstandtech.restclient.server.auth.ApacheCommonsOAuthAuthenticator;
import com.handstandtech.restclient.server.auth.Authenticator;
import com.handstandtech.restclient.server.impl.RESTClientCommonsImpl;

public class FlickrConstants {
	// "Testing Java"
	public static final String API_KEY = "d32f7335f0a3dd8c63f4f3fc312a37bc";
	private static final String API_SECRET = "f6d7142634e935d1";
	public static RESTClientProvider restClientProvider = new RESTClientProvider() {

		@Override
		public RESTClient getNewClientInstance() {
			return new RESTClientCommonsImpl();
		}
	};

	public static OAuthAuthenticatorProvider oauthProvider = new OAuthAuthenticatorProvider() {

		@Override
		public Authenticator getAuthenticator(OAuthConsumer consumer) {
			return new ApacheCommonsOAuthAuthenticator(consumer);
		}

	};

	public static FlickrHelper createFlickrHelper() {
		OAuthConsumer consumer = getConsumer();
		FlickrHelper flickr = new FlickrHelper(consumer, restClientProvider,
				oauthProvider);
		return flickr;
	}

	public static OAuthConsumer getConsumer() {
		return new CommonsHttpOAuthConsumer(API_KEY, API_SECRET);
	}

	public static FlickrHelper createFlickrHelper(String token,
			String tokenSecret) {
		OAuthConsumer consumer = getConsumer();
		consumer.setTokenWithSecret(token, tokenSecret);
		FlickrHelper flickr = new FlickrHelper(consumer, restClientProvider,
				oauthProvider);
		return flickr;
	}
}
