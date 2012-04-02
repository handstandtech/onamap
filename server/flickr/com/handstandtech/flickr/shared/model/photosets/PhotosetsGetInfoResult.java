package com.handstandtech.flickr.shared.model.photosets;

import java.io.Serializable;

import com.handstandtech.flickr.shared.model.FlickrPerson;
import com.handstandtech.flickr.shared.model.FlickrPhotosetInfo;

public class PhotosetsGetInfoResult implements Serializable {

	/**
	 * Default Serialization UID
	 */
	private static final long serialVersionUID = 1L;
	
	private FlickrPhotosetInfo photoset;
	
	private String stat;
	
	public PhotosetsGetInfoResult() {
		super();
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public String getStat() {
		return stat;
	}

	public void setPhotoset(FlickrPhotosetInfo photoset) {
		this.photoset = photoset;
	}

	public FlickrPhotosetInfo getPhotoset() {
		return photoset;
	}

}
