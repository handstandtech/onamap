package com.handstandtech.flickr.shared.model;

import java.io.Serializable;

import com.handstandtech.flickr.shared.model.content.FlickrDateContent;
import com.handstandtech.flickr.shared.model.content.FlickrNumberContent;
import com.handstandtech.flickr.shared.model.content.FlickrTimeContent;

public class FlickrPhotos implements Serializable {

	/**
	 * Default Serialization UID
	 */
	private static final long serialVersionUID = 1L;

	private FlickrNumberContent count;

	private FlickrTimeContent firstdate;

	private FlickrDateContent firstdatetaken;

	public FlickrNumberContent getCount() {
		return count;
	}

	public void setCount(FlickrNumberContent count) {
		this.count = count;
	}

	public FlickrTimeContent getFirstdate() {
		return firstdate;
	}

	public void setFirstdate(FlickrTimeContent firstdate) {
		this.firstdate = firstdate;
	}

	public FlickrDateContent getFirstdatetaken() {
		return firstdatetaken;
	}

	public void setFirstdatetaken(FlickrDateContent firstdatetaken) {
		this.firstdatetaken = firstdatetaken;
	}

	public FlickrPhotos() {
	}
}