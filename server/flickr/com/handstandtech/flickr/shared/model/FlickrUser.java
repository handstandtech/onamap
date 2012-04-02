package com.handstandtech.flickr.shared.model;

import java.io.Serializable;

import com.handstandtech.flickr.shared.model.content.FlickrStringContent;

public class FlickrUser implements Serializable {

	/**
	 * Default Serialization UID
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String nsid;

	private FlickrStringContent username;

	public FlickrUser() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNsid() {
		return nsid;
	}

	public void setNsid(String nsid) {
		this.nsid = nsid;
	}

	public FlickrStringContent getUsername() {
		return username;
	}

	public void setUsername(FlickrStringContent username) {
		this.username = username;
	}
}
