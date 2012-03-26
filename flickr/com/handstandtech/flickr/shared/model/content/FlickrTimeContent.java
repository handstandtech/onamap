package com.handstandtech.flickr.shared.model.content;

import java.io.Serializable;
import java.util.Date;

public class FlickrTimeContent implements Serializable {

	/**
	 * Default Serialization UID
	 */
	private static final long serialVersionUID = 1L;

	private String _content;

	public FlickrTimeContent() {
		super();
	}

	public void set_content(String _content) {
		this._content = _content;
	}

	public String get_content() {
		return _content;
	}

	public Long getTime() {
		return Long.parseLong(_content);
	}
	
	public Date getDate() {
		Long time = getTime();
		System.out.println(time);
		Date date = new Date(time*1000L);
		System.out.println(date);
		return date;
	}

}
