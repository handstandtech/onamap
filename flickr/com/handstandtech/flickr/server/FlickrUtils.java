package com.handstandtech.flickr.server;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.handstandtech.flickr.shared.model.FlickrPerson;
import com.handstandtech.flickr.shared.model.FlickrPhotoset;
import com.handstandtech.flickr.shared.model.FlickrPhotosetInfos;
import com.handstandtech.flickr.shared.model.FlickrUser;
import com.handstandtech.flickr.shared.model.people.FindByEmailResult;
import com.handstandtech.flickr.shared.model.people.PeopleGetInfoResult;
import com.handstandtech.flickr.shared.model.photosets.PhotosetsGetListResult;
import com.handstandtech.flickr.shared.model.photosets.PhotosetsGetPhotosResult;
import com.handstandtech.flickr.shared.model.places.FlickrPlace;
import com.handstandtech.flickr.shared.model.places.PlacesGetInfoResult;
import com.handstandtech.restclient.shared.model.RESTResult;

public class FlickrUtils {

	public static FlickrPlace getPlacesInfoFromResult(RESTResult restResult) {

		String userJson = restResult.getResponseBody();

		Gson gson = getFlickrGson();
		PlacesGetInfoResult result = gson.fromJson(userJson, PlacesGetInfoResult.class);

		if (result.getStat().equals("ok")) {
			return result.getPlace();
		} else {
			return null;
		}
	}

	private static Gson getFlickrGson() {
		Gson gson = new Gson();
		gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
			public Date deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2)
					throws JsonParseException {
				String date = arg0.getAsString();
				String formatStr = "yyyy-MM-dd HH:mm:ss";
				SimpleDateFormat df = new SimpleDateFormat(formatStr);
				try {
					return df.parse(date);
				} catch (ParseException e) {
					throw new JsonParseException("Cannot parse date: " + e.getMessage(), e);
				}
			}

		}).create();
		return gson;
	}

	public static FlickrPerson getPersonFromResult(RESTResult restResult) {
		Gson gson = getFlickrGson();
		PeopleGetInfoResult result = gson.fromJson(restResult.getResponseBody(), PeopleGetInfoResult.class);

		if (result.getStat().equals("ok")) {
			return result.getPerson();
		} else {
			return null;
		}
	}
	
	public static FlickrUser getFlickrUserFromResult(RESTResult restResult) {
		Gson gson = getFlickrGson();
		FindByEmailResult result = gson.fromJson(restResult.getResponseBody(), FindByEmailResult.class);

		if (result.getStat().equals("ok")) {
			return result.getUser();
		} else {
			return null;
		}
	}

	public static FlickrPhotoset getPhotosetFromResult(RESTResult restResult) {
		Gson gson = getFlickrGson();
		PhotosetsGetPhotosResult photosetResult = gson.fromJson(restResult.getResponseBody(),
				PhotosetsGetPhotosResult.class);

		if (photosetResult.getStat().equals("ok")) {
			return photosetResult.getPhotoset();
		} else {
			return null;
		}
	}

	public static FlickrPhotosetInfos getPhotosetInfosFromResult(RESTResult restResult) {
		Gson gson = getFlickrGson();
		PhotosetsGetListResult result = gson.fromJson(restResult.getResponseBody(), PhotosetsGetListResult.class);

		if (result.getStat().equals("ok")) {
			return result.getPhotosets();
		} else {
			return null;
		}
	}

	public static FlickrUser getSearchByEmailResult(RESTResult restResult) {
		Gson gson = getFlickrGson();
		FindByEmailResult result = gson.fromJson(restResult.getResponseBody(), FindByEmailResult.class);

		if (result.getStat().equals("ok")) {
			return result.getUser();
		} else {
			return null;
		}
	}

}
