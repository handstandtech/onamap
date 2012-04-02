package com.handstandtech.flickr.shared.model.photosets;

import java.io.Serializable;

import com.handstandtech.flickr.shared.model.FlickrPhotoset;

public class PhotosetsGetPhotosResult implements Serializable {

	/**
	 * Default Serialization UID
	 */
	private static final long serialVersionUID = 1L;
	
	private FlickrPhotoset photoset;
	
	private String stat;
	
	public PhotosetsGetPhotosResult() {
		super();
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public String getStat() {
		return stat;
	}

	public void setPhotoset(FlickrPhotoset photoset) {
		this.photoset = photoset;
	}

	public FlickrPhotoset getPhotoset() {
		return photoset;
	}

}
