package com.handstandtech.flickr.shared.model.people;

import java.io.Serializable;

import com.handstandtech.flickr.shared.model.FlickrUser;

public class FindByEmailResult implements Serializable {

	/**
	 * Default Serialization UID
	 */
	private static final long serialVersionUID = 1L;
	
	private FlickrUser user;
	
	private String stat;
	
	public FindByEmailResult() {
		super();
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public String getStat() {
		return stat;
	}

	public void setUser(FlickrUser user) {
		this.user = user;
	}

	public FlickrUser getUser() {
		return user;
	}

}
