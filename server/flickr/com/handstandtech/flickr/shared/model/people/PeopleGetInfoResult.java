package com.handstandtech.flickr.shared.model.people;

import java.io.Serializable;

import com.handstandtech.flickr.shared.model.FlickrPerson;

public class PeopleGetInfoResult implements Serializable {

	/**
	 * Default Serialization UID
	 */
	private static final long serialVersionUID = 1L;
	
	private FlickrPerson person;
	
	private String stat;
	
	public PeopleGetInfoResult() {
		super();
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public String getStat() {
		return stat;
	}

	public void setPerson(FlickrPerson person) {
		this.person = person;
	}

	public FlickrPerson getPerson() {
		return person;
	}

}
