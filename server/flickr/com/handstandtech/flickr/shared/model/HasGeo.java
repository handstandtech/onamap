package com.handstandtech.flickr.shared.model;

import java.io.Serializable;

public interface HasGeo extends Serializable {
	public Double getLatitude();

	public Double getLongitude();
}
