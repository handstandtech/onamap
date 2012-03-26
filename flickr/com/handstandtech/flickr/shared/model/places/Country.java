package com.handstandtech.flickr.shared.model.places;

import com.handstandtech.flickr.shared.model.HasGeo;

public interface Country extends HasGeo {
	public String getName();

	public String getWoeId();

	public String getPlaceId();

	public String getPlaceUrl();
}
