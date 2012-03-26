package com.handstandtech.flickr.server;

import java.util.HashMap;
import java.util.Map;

import net.onamap.server.constants.Urls;
import net.onamap.server.flickr.OAuthConsumerInfoManager;
import net.onamap.server.flickr.OAuthConsumerInfoManager.OAuthConsumerInfoSet;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.handstandtech.flickr.shared.model.FlickrPerson;
import com.handstandtech.flickr.shared.model.FlickrPhotoset;
import com.handstandtech.flickr.shared.model.FlickrPhotosetInfos;
import com.handstandtech.flickr.shared.model.FlickrUser;
import com.handstandtech.flickr.shared.model.places.FlickrPlace;
import com.handstandtech.restclient.server.RESTClient;
import com.handstandtech.restclient.server.auth.Authenticator;
import com.handstandtech.restclient.server.auth.OAuthAuthenticator;
import com.handstandtech.restclient.server.impl.RESTClientJavaNetImpl;
import com.handstandtech.restclient.shared.model.RESTResult;
import com.handstandtech.restclient.shared.model.RequestMethod;
import com.handstandtech.restclient.shared.util.RESTURLUtil;

public class FlickrHelper {

	public static void main(String args[]) {

	}

	private static final Logger log = LoggerFactory
			.getLogger(FlickrHelper.class);
	private static final String BASE_URL = "http://api.flickr.com/services/rest/";

	// "Testing Java"
	private static final String API_KEY = "d32f7335f0a3dd8c63f4f3fc312a37bc";
	private static final String API_SECRET = "f6d7142634e935d1";

	protected OAuthConsumer consumer;

	public FlickrHelper() {

	}

	public FlickrHelper(OAuthConsumer consumer) {
		this.consumer = consumer;
	}

	public FlickrHelper(String requestUrl, String token, String tokenSecret) {
		this.consumer = getOAuthConsumer();
		this.consumer.setTokenWithSecret(token, tokenSecret);
	}

	public FlickrHelper(String consumerKey, String consumerSecret,
			String token, String tokenSecret) {
		this.consumer = new DefaultOAuthConsumer(consumerKey, consumerSecret);
		this.consumer.setTokenWithSecret(token, tokenSecret);
	}

	private Authenticator getAuthenticator() {
		if (consumer != null) {
			return new OAuthAuthenticator(consumer);
		} else {
			return null;
		}
	}

	public FlickrUser people_searchByEmail(String email) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("method", "flickr.people.findByEmail");
		params.put("find_email", email);
		params.put("format", "json");
		params.put("nojsoncallback", "1");
		params.put("api_key", API_KEY);

		String url = RESTURLUtil.createFullUrl(BASE_URL, params);

		RESTClient client = new RESTClientJavaNetImpl();
		RESTResult restResult = client.request(RequestMethod.GET, url,
				getAuthenticator());

		return FlickrUtils.getSearchByEmailResult(restResult);

	}

	public FlickrPhotosetInfos photosets_getList(String user_id) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("method", "flickr.photosets.getList");
		if (user_id != null) {
			params.put("user_id", user_id);
		}
		params.put("format", "json");
		params.put("nojsoncallback", "1");
		params.put("api_key", API_KEY);

		String url = RESTURLUtil.createFullUrl(BASE_URL, params);

		RESTClient client = new RESTClientJavaNetImpl();
		RESTResult restResult = client.request(RequestMethod.GET, url,
				getAuthenticator());
		
		log.info(restResult.toString());

		return FlickrUtils.getPhotosetInfosFromResult(restResult);
	}

	public FlickrPlace places_getInfo(String place_id) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("method", "flickr.places.getInfo");
		params.put("place_id", place_id);
		params.put("format", "json");
		params.put("nojsoncallback", "1");
		params.put("api_key", API_KEY);

		String url = RESTURLUtil.createFullUrl(BASE_URL, params);

		RESTClient client = new RESTClientJavaNetImpl();
		RESTResult restResult = client.request(RequestMethod.GET, url,
				getAuthenticator());

		return FlickrUtils.getPlacesInfoFromResult(restResult);
	}

	public FlickrPhotoset photosets_getPhotos(String photoset_id) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("method", "flickr.photosets.getPhotos");
		params.put("photoset_id", photoset_id);
		params.put("extras",
				"geo,date_taken,last_update,url_sq,url_t,url_s,url_m,url_o");
		
		//Privacy Filter is Off (We can see private photos)
		params.put("privacy_filter", "0");

		params.put("format", "json");
		params.put("nojsoncallback", "1");
		params.put("api_key", API_KEY);

		String url = RESTURLUtil.createFullUrl(BASE_URL, params);

		RESTClient client = new RESTClientJavaNetImpl();
		RESTResult restResult = client.request(RequestMethod.GET, url,
				getAuthenticator());

		log.debug(restResult.toString());

		return FlickrUtils.getPhotosetFromResult(restResult);
	}

	public FlickrPerson people_getInfo(String id) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("method", "flickr.people.getInfo");
		params.put("user_id", id);
		params.put("format", "json");
		params.put("nojsoncallback", "1");
		params.put("api_key", API_KEY);

		RESTClient client = new RESTClientJavaNetImpl();
		RESTResult result = client.request(RequestMethod.GET, BASE_URL,
				getAuthenticator());

		return FlickrUtils.getPersonFromResult(result);
	}

	public FlickrUser getCurrentUserInfo() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("method", "flickr.test.login");
		params.put("format", "json");
		params.put("nojsoncallback", "1");
		params.put("api_key", API_KEY);

		RESTClient client = new RESTClientJavaNetImpl();
		String url = RESTURLUtil.createFullUrl(BASE_URL, params);
		RESTResult result = client.request(RequestMethod.GET, url,
				getAuthenticator());

		log.info(result.toString());

		return FlickrUtils.getFlickrUserFromResult(result);
	}

	private static OAuthConsumerInfoManager oauthManager = new OAuthConsumerInfoManager();
	static {
		oauthManager.addConsumerInfoSet(getLocalhostConsumerInfo());
	}

	private static OAuthConsumerInfoSet getLocalhostConsumerInfo() {
		OAuthConsumerInfoSet localhost = new OAuthConsumerInfoSet();
		localhost.setName("Flickr - 127.0.0.1:8888");
		localhost.setCallbackUri(Urls.FLICKR_OAUTH_CALLBACK_URI);
		localhost.setConsumerKey(API_KEY);
		localhost.setConsumerSecret(API_SECRET);
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

	public static OAuthConsumer getOAuthConsumer() {
		return new DefaultOAuthConsumer(API_KEY, API_SECRET);
	}

}
