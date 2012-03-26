package com.handstandtech.flickr.shared.model.places;

import java.io.Serializable;

public class PlacesGetInfoResult implements Serializable {

	/**
	 * Default Serialization UID
	 */
	private static final long serialVersionUID = 1L;
	
	private FlickrPlace place;
	
	private String stat;
	
	public PlacesGetInfoResult() {
		super();
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public String getStat() {
		return stat;
	}

	public void setPlace(FlickrPlace place) {
		this.place = place;
	}

	public FlickrPlace getPlace() {
		return place;
	}

}
