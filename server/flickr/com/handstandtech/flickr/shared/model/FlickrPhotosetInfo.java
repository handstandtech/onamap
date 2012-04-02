package com.handstandtech.flickr.shared.model;

import java.io.Serializable;

import com.handstandtech.flickr.shared.model.content.FlickrStringContent;

public class FlickrPhotosetInfo implements Serializable {

	/**
	 * Default Serialization UID
	 */
	private static final long serialVersionUID = 1L;

	private FlickrStringContent description;
	
	private Integer farm;
	
	private String id;
	
	private String photos;
	
	private String primary;
	
	private String secret;
	
	private String server;
	
	private FlickrStringContent title;
	
	private Integer videos;

	public FlickrPhotosetInfo() {
	}

	public FlickrStringContent getDescription() {
		return description;
	}

	public void setDescription(FlickrStringContent description) {
		this.description = description;
	}

	public Integer getFarm() {
		return farm;
	}

	public void setFarm(Integer farm) {
		this.farm = farm;
	}

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	public String getPrimary() {
		return primary;
	}

	public void setPrimary(String primary) {
		this.primary = primary;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public FlickrStringContent getTitle() {
		return title;
	}

	public void setTitle(FlickrStringContent title) {
		this.title = title;
	}

	public Integer getVideos() {
		return videos;
	}

	public void setVideos(Integer videos) {
		this.videos = videos;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}