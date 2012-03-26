package com.handstandtech.flickr.shared.model.places;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.handstandtech.flickr.shared.model.HasGeo;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class FlickrPlace implements HasGeo{

	/**
	 * Default Serialization UID
	 */
	private static final long serialVersionUID = 1L;
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String place_id;
	
	@Persistent
	private FlickrPlaceInfo country;
	
	@Persistent
	private FlickrPlaceInfo county;
	
	@Persistent
	private Integer has_shapedata;
	
	@Persistent
	private Double latitude;
	
	@Persistent
	private Double longitude;
	
	@Persistent
	private FlickrPlaceInfo locality;
	
	@Persistent
	private String name;
	
	@Persistent
	private String woeid;
	
	@Persistent
	private String place_type;
	
	@Persistent
	private Integer place_type_id;
	
	@Persistent
	private String place_url;
	
	@Persistent
	private FlickrPlaceInfo region;
	
	@Persistent
	private FlickrShapedata shapedata;
	
	@Persistent
	private String timezone;
	
	public FlickrPlace() {
		
	}

	@Override
	public Double getLatitude() {
		return latitude;
	}

	@Override
	public Double getLongitude() {
		return longitude;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public FlickrPlaceInfo getCountry() {
		return country;
	}

	public FlickrPlaceInfo getCounty() {
		return county;
	}

	public Integer getHas_shapedata() {
		return has_shapedata;
	}

	public FlickrPlaceInfo getLocality() {
		return locality;
	}

	public String getPlace_id() {
		return place_id;
	}

	public String getPlace_type() {
		return place_type;
	}

	public Integer getPlace_type_id() {
		return place_type_id;
	}

	public String getPlace_url() {
		return place_url;
	}

	public FlickrPlaceInfo getRegion() {
		return region;
	}

	public FlickrShapedata getShapedata() {
		return shapedata;
	}

	public String getTimezone() {
		return timezone;
	}

	public String getWoeid() {
		return woeid;
	}

}
