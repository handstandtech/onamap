package com.handstandtech.flickr.shared.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;

public class FlickrPhoto implements Serializable, HasGeo {

	/**
	 * Default Serialization UID
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private Date datetaken;

	private Double latitude;

	private Double longitude;

	private String title;

	private Long lastupdate;

	private String url_sq;
	private String url_t;
	private String url_s;
	private String url_m;
	private String url_o;

	public FlickrPhoto() {
	}

	public Date getDatetaken() {
		return datetaken;
	}

	public void setDatetaken(Date datetaken) {
		this.datetaken = datetaken;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	@Override
	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl_sq() {
		return url_sq;
	}

	public void setUrl_sq(String url_sq) {
		this.url_sq = url_sq;
	}

	public String getUrl_t() {
		return url_t;
	}

	public void setUrl_t(String url_t) {
		this.url_t = url_t;
	}

	public String getUrl_s() {
		return url_s;
	}

	public void setUrl_s(String url_s) {
		this.url_s = url_s;
	}

	public String getUrl_m() {
		return url_m;
	}

	public void setUrl_m(String url_m) {
		this.url_m = url_m;
	}

	public String getUrl_o() {
		return url_o;
	}

	public void setUrl_o(String url_o) {
		this.url_o = url_o;
	}

	public Date getLastupdate() {
		return new Date(lastupdate);
	}

	public void setLastupdate(Long lastupdate) {
		this.lastupdate = lastupdate;
	}
}