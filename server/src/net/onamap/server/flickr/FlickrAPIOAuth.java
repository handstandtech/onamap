//package net.onamap.server.flickr;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//
//import oauth.signpost.OAuthConsumer;
//import oauth.signpost.basic.DefaultOAuthConsumer;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.handstandtech.restclient.server.RESTClient;
//import com.handstandtech.restclient.server.auth.Authenticator;
//import com.handstandtech.restclient.server.auth.OAuthAuthenticator;
//import com.handstandtech.restclient.server.impl.RESTClientJavaNetImpl;
//import com.handstandtech.restclient.shared.model.RESTResult;
//import com.handstandtech.restclient.shared.model.RequestMethod;
//import com.handstandtech.restclient.shared.util.RESTURLUtil;
//
//public class FlickrAPIOAuth {
//
//	protected static Logger log = LoggerFactory.getLogger(FlickrAPIOAuth.class
//			.getName());
//
//	private static final String FORMAT = "json";
//
//	private static final String TWITTER_RATE_LIMIT_HEADER_KEY = "x-ratelimit-limit";
//	private static final String TWITTER_RATE_LIMIT_REMAINING_HEADER_KEY = "x-ratelimit-remaining";
//	private static final String TWITTER_RATE_LIMIT_RESET_HEADER_KEY = "x-ratelimit-reset";
//
//	protected OAuthConsumer consumer;
//
//	public FlickrAPIOAuth(OAuthConsumer consumer) {
//		this.consumer = consumer;
//	}
//
//	public FlickrAPIOAuth(String requestUrl, String token, String tokenSecret) {
//		this.consumer = FlickrServerConstants.INSTANCE.getOAuthConsumer(requestUrl);
//		this.consumer.setTokenWithSecret(token, tokenSecret);
//	}
//
//	public FlickrAPIOAuth(String consumerKey, String consumerSecret,
//			String token, String tokenSecret) {
//		this.consumer = new DefaultOAuthConsumer(consumerKey, consumerSecret);
//		this.consumer.setTokenWithSecret(token, tokenSecret);
//	}
//
//	/**
//	 * Calls and gets a list of twitter friends as long as there are less than
//	 * 100
//	 * 
//	 * @param idListStr
//	 * @return
//	 */
//	private List get(List<Long> ids) {
//		
//		RESTClient client = new RESTClientJavaNetImpl();
//		Map<String, String> params2 = new HashMap<String, String>();
//
//		String friendsInfoUrl = RESTURLUtil.createFullUrl(
//				"http://api.twitter.com/1/users/lookup." + FORMAT, params2);
//		log.debug("Calling URL: " + friendsInfoUrl);
//		RESTResult friendInfoResult = client.request(RequestMethod.GET,
//				friendsInfoUrl, getAuthenticator());
//		// log.trace(friendInfoResult.toString());
//
//		friendInfoResult.getResponseBody();
//
//		try {
//			Map<String, String> headers = friendInfoResult.getResponseHeaders();
//			log.debug(headers.get(TWITTER_RATE_LIMIT_REMAINING_HEADER_KEY)
//					+ " of " + headers.get(TWITTER_RATE_LIMIT_HEADER_KEY)
//					+ " remaining.  Resets at "
//					+ headers.get(TWITTER_RATE_LIMIT_RESET_HEADER_KEY));
//		} catch (Exception e) {
//			log.error("Had a Problem reading the response headers!");
//		}
//
//		return null;
//
//	}
//
//	private Authenticator getAuthenticator() {
//		return new OAuthAuthenticator(consumer);
//	}
//
//}
