package com.handstandtech.flickr.shared.model.content;

import java.io.Serializable;

public class FlickrStringContent implements Serializable {

	/**
	 * Default Serialization UID
	 */
	private static final long serialVersionUID = 1L;

	protected String _content;

	public FlickrStringContent() {
		super();
	}

	public void set_content(String _content) {
		this._content = _content;
	}

	public String get_content() {
		return _content;
	}

	@Override
	public String toString() {
		return _content;
	}

}
