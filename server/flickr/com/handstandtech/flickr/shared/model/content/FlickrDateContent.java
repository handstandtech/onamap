package com.handstandtech.flickr.shared.model.content;

import java.io.Serializable;
import java.util.Date;

public class FlickrDateContent implements Serializable {

	/**
	 * Default Serialization UID
	 */
	private static final long serialVersionUID = 1L;

	private Date _content;
	
	public FlickrDateContent() {
		super();
	}

	public void set_content(Date _content) {
		this._content = _content;
	}

	public Date get_content() {
		return _content;
	}
	
	public Date getDate() {
		return _content;
	}

}
