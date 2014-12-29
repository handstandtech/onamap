package net.onamap.server.constants;

import com.handstandtech.flickr.server.FlickrHelper;
import com.handstandtech.restclient.server.RESTClient;
import com.handstandtech.restclient.server.RESTClientProvider;
import com.handstandtech.restclient.server.auth.Authenticator;
import com.handstandtech.restclient.server.auth.oauth.JavaNetOAuthAuthenticator;
import com.handstandtech.restclient.server.auth.oauth.OAuthAuthenticatorProvider;
import com.handstandtech.restclient.server.impl.RESTClientJavaNetImpl;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;

public class FlickrConstants {
	// "Testing Java"
	private static final String API_KEY = "d32f7335f0a3dd8c63f4f3fc312a37bc";
	private static final String API_SECRET = "f6d7142634e935d1";

	private static RESTClientProvider restClientProvider = new RESTClientProvider() {

		@Override
		public RESTClient getNewClientInstance() {
			return new RESTClientJavaNetImpl();
		}
	};

	private static OAuthAuthenticatorProvider oauthProvider = new OAuthAuthenticatorProvider() {

		@Override
		public Authenticator getAuthenticator(OAuthConsumer consumer) {
			return new JavaNetOAuthAuthenticator(consumer);
		}

	};

//	public static FlickrHelper createFlickrHelper() {
//		OAuthConsumer consumer = new DefaultOAuthConsumer(API_KEY, API_SECRET);
//		FlickrHelper flickr = new FlickrHelper(consumer, restClientProvider,
//				oauthProvider);
//		return flickr;
//	}

	public static FlickrHelper createFlickrHelper(String token,
			String tokenSecret) {
		OAuthConsumer consumer = new DefaultOAuthConsumer(API_KEY, API_SECRET);
		consumer.setTokenWithSecret(token, tokenSecret);
		FlickrHelper flickr = new FlickrHelper(consumer, restClientProvider,
				oauthProvider);
		return flickr;
	}

	public static OAuthConsumer getOAuthConsumer() {
		return new DefaultOAuthConsumer(API_KEY, API_SECRET);
	}
}
