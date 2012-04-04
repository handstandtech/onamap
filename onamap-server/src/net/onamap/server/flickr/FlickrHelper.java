//package net.onamap.server.flickr;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import net.onamap.server.flickr.FlickrServerConstants;
//import oauth.signpost.OAuthConsumer;
//import oauth.signpost.basic.DefaultOAuthConsumer;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.handstandtech.flickr.shared.model.FlickrPerson;
//import com.handstandtech.flickr.shared.model.FlickrPhotoset;
//import com.handstandtech.flickr.shared.model.FlickrPhotosetInfos;
//import com.handstandtech.flickr.shared.model.FlickrUser;
//import com.handstandtech.flickr.shared.model.places.FlickrPlace;
//import com.handstandtech.restclient.server.RESTClient;
//import com.handstandtech.restclient.server.RESTUtil;
//import com.handstandtech.restclient.server.impl.RESTClientJavaNetImpl;
//import com.handstandtech.restclient.shared.model.RESTResult;
//import com.handstandtech.restclient.shared.model.RequestMethod;
//import com.handstandtech.restclient.shared.util.RESTURLUtil;
//
//public class FlickrHelper {
//
//	public static void main(String args[]) {
//
//	}
//
//	private static final Logger log = LoggerFactory
//			.getLogger(FlickrHelper.class);
//	private static final String BASE_URL = "http://api.flickr.com/services/rest/";
//
//	private static final String API_KEY = "4e506ac6996dc660cc68a64f714e7c6d";
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
//	public FlickrUser people_searchByEmail(String email) {
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("method", "flickr.people.findByEmail");
//		params.put("find_email", email);
//		params.put("format", "json");
//		params.put("nojsoncallback", "1");
//		params.put("api_key", API_KEY);
//
//		String url = RESTURLUtil.createFullUrl(BASE_URL, params);
//
//		RESTClient client = new RESTClientJavaNetImpl();
//		RESTResult restResult = client.request(RequestMethod.GET, url);
//
//		return FlickrUtils.getSearchByEmailResult(restResult);
//
//	}
//
//	public FlickrPhotosetInfos photosets_getList(String user_id) {
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("method", "flickr.photosets.getList");
//		if (user_id != null) {
//			params.put("user_id", user_id);
//		}
//		params.put("format", "json");
//		params.put("nojsoncallback", "1");
//		params.put("api_key", API_KEY);
//
//		String url = RESTURLUtil.createFullUrl(BASE_URL, params);
//
//		RESTClient client = new RESTClientJavaNetImpl();
//		RESTResult restResult = client.request(RequestMethod.GET, url);
//
//		String json = restResult.getResponseBody();
//
//		return FlickrUtils.getPhotosetInfosFromResult(restResult);
//	}
//
//	public FlickrPlace places_getInfo(String place_id) {
//
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("method", "flickr.places.getInfo");
//		params.put("place_id", place_id);
//		params.put("format", "json");
//		params.put("nojsoncallback", "1");
//		params.put("api_key", API_KEY);
//
//		String url = RESTURLUtil.createFullUrl(BASE_URL, params);
//
//		RESTClient client = new RESTClientJavaNetImpl();
//		RESTResult restResult = client.request(RequestMethod.GET, url);
//
//		return FlickrUtils.getPlacesInfoFromResult(restResult);
//	}
//
//	public FlickrPhotoset photosets_getPhotos(String photoset_id) {
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("method", "flickr.photosets.getPhotos");
//		params.put("photoset_id", photoset_id);
//		params.put("extras", "geo,date_taken,last_update,url_sq,url_t,url_s,url_m,url_o");
//		params.put("privacy_filter", "1");
//
//		params.put("format", "json");
//		params.put("nojsoncallback", "1");
//		params.put("api_key", API_KEY);
//
//		String url = RESTURLUtil.createFullUrl(BASE_URL, params);
//
//		RESTClient client = new RESTClientJavaNetImpl();
//		RESTResult restResult = client.request(RequestMethod.GET, url);
//
//		log.debug(restResult.toString());
//
//		return FlickrUtils.getPhotosetFromResult(restResult);
//	}
//
//	public FlickrPerson people_getInfo(String id) {
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("method", "flickr.people.getInfo");
//		params.put("user_id", id);
//		params.put("format", "json");
//		params.put("nojsoncallback", "1");
//		params.put("api_key", API_KEY);
//
//		RESTClient client = new RESTClientJavaNetImpl();
//		RESTResult result = client.request(RequestMethod.GET, BASE_URL);
//
//		return FlickrUtils.getPersonFromResult(result);
//	}
//}
