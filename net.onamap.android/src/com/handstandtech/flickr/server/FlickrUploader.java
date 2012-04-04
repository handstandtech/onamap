package com.handstandtech.flickr.server;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oauth.signpost.OAuthConsumer;

import com.handstandtech.restclient.server.auth.Authenticator;
import com.handstandtech.restclient.server.model.RESTRequest;
import com.handstandtech.restclient.shared.model.RequestMethod;
import com.handstandtech.restclient.shared.util.RESTURLUtil;

public class FlickrUploader {

	public static Map<String, Object> upload(Object photo, String title,
			String description, List<String> tags, Boolean is_public,
			Boolean is_friend, Boolean is_family,
			FlickrSafetyLevel safety_level, FlickrContentType content_type,
			Boolean hidden, Authenticator auth) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("photo", photo);

		// if (title != null && title.length() > 0) {
		// try {
		// map.put("title", URLEncoder.encode(title, "UTF-8"));
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
		// }
		// if (description != null && description.length() > 0) {
		// try {
		// map.put("description", URLEncoder.encode(description, "UTF-8"));
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
		// }
		// if (content_type != null) {
		// map.put("content_type", content_type.getValue() + "");
		// }
		// if (hidden != null) {
		// String value = null;
		// if (hidden.booleanValue()) {
		// value = "2";
		// } else {
		// value = "1";
		// }
		//
		// map.put("hidden", value);
		// }
		// if (safety_level != null) {
		// map.put("safety_level", safety_level.getValue() + "");
		// }

		return map;

	}
}
