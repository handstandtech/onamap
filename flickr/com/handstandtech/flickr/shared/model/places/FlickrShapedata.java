package com.handstandtech.flickr.shared.model.places;

import java.io.Serializable;

public class FlickrShapedata implements Serializable{

	/**
	 * Default Serialization UID
	 */
	private static final long serialVersionUID = 1L;
	
	private Double alpha;
	
	private Integer count_edges;
	
	private Integer count_points;
	
	private Long created;
	
	private Integer has_donuthole;
	
	private Integer is_donuthole;
	
	private FlickrPolylines polylines;
	
	public FlickrShapedata() {
		
	}

}
