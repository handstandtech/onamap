package com.handstandtech.flickr.shared.model;

import java.io.Serializable;

import com.handstandtech.flickr.shared.model.content.FlickrStringContent;

public class FlickrPerson implements Serializable {

	/**
	 * Default Serialization UID
	 */
	private static final long serialVersionUID = 1L;

	private Integer iconfarm;

	private String iconserver;

	private String id;

	private Integer ispro;

	private FlickrStringContent location;

	private FlickrStringContent mobileurl;

	private String nsid;

	private String path_alias;

	private FlickrPhotos photos;

	private FlickrStringContent photosurl;

	private FlickrStringContent profileurl;

	private FlickrStringContent realname;

	private FlickrStringContent username;

	public FlickrPerson() {
		super();
	}

	public String getId() {
		return id;
	}

	public Integer getIconfarm() {
		return iconfarm;
	}

	public void setIconfarm(Integer iconfarm) {
		this.iconfarm = iconfarm;
	}

	public String getIconserver() {
		return iconserver;
	}

	public void setIconserver(String iconserver) {
		this.iconserver = iconserver;
	}

	public Integer getIspro() {
		return ispro;
	}

	public void setIspro(Integer ispro) {
		this.ispro = ispro;
	}

	public FlickrStringContent getLocation() {
		return location;
	}

	public void setLocation(FlickrStringContent location) {
		this.location = location;
	}

	public FlickrStringContent getMobileurl() {
		return mobileurl;
	}

	public void setMobileurl(FlickrStringContent mobileurl) {
		this.mobileurl = mobileurl;
	}

	public FlickrPhotos getPhotos() {
		return photos;
	}

	public void setPhotos(FlickrPhotos flickrPhotos) {
		this.photos = flickrPhotos;
	}


	public FlickrPhotos getFlickrPhotos() {
		return photos;
	}

	public void setFlickrPhotos(FlickrPhotos flickrPhotos) {
		this.photos = flickrPhotos;
	}

	public FlickrStringContent getPhotosurl() {
		return photosurl;
	}

	public void setPhotosurl(FlickrStringContent photosurl) {
		this.photosurl = photosurl;
	}

	public FlickrStringContent getProfileurl() {
		return profileurl;
	}

	public void setProfileurl(FlickrStringContent profileurl) {
		this.profileurl = profileurl;
	}

	public FlickrStringContent getRealname() {
		return realname;
	}

	public void setRealname(FlickrStringContent realname) {
		this.realname = realname;
	}

	public FlickrStringContent getUsername() {
		return username;
	}

	public void setUsername(FlickrStringContent username) {
		this.username = username;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNsid() {
		return nsid;
	}

	public void setNsid(String nsid) {
		this.nsid = nsid;
	}

	public void setPath_alias(String path_alias) {
		this.path_alias = path_alias;
	}

	public String getPath_alias() {
		return path_alias;
	}
}
