package com.handstandtech.flickr.shared.model.content;

import java.io.Serializable;

public class FlickrNumberContent implements Serializable {

	/**
	 * Default Serialization UID
	 */
	private static final long serialVersionUID = 1L;

	private String _content;
	
	public FlickrNumberContent() {
		super();
	}

	public void set_content(String _content) {
		this._content = _content;
	}

	public String get_content() {
		return _content;
	}

}
