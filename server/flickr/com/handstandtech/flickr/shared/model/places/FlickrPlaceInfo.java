package com.handstandtech.flickr.shared.model.places;

import com.handstandtech.flickr.shared.model.content.FlickrStringContent;


public class FlickrPlaceInfo extends FlickrStringContent implements Country {

	/**
	 * Default Serialization UID
	 */
	private static final long serialVersionUID = 1L;

	private Double latitude;

	private Double longitude;

	private String place_id;

	private String place_url;

	private String woeid;

	public FlickrPlaceInfo() {

	}

	@Override
	public Double getLatitude() {
		return latitude;
	}

	@Override
	public Double getLongitude() {
		return longitude;
	}

	@Override
	public String getName() {
		return _content;
	}

	@Override
	public String getWoeId() {
		return woeid;
	}

	@Override
	public String getPlaceId() {
		return place_id;
	}

	@Override
	public String getPlaceUrl() {
		return place_url;
	}

}