package com.handstandtech.flickr.shared.model.places;

import java.io.Serializable;
import java.util.List;

import com.handstandtech.flickr.shared.model.content.FlickrStringContent;

public class FlickrPolylines implements Serializable {

	/**
	 * Default Serialization UID
	 */
	private static final long serialVersionUID = 1L;
	
	private List<FlickrStringContent> polyline;
	
	private FlickrStringContent shapefile;
	
	public FlickrPolylines() {
		
	}
	
	

}
