package com.handstandtech.flickr.shared.model;

import java.io.Serializable;
import java.util.List;

public class FlickrPhotoset implements Serializable, HasPhotos{

	/**
	 * Default Serialization UID
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String owner;
	
	private String ownername;
	
	private Integer page;
	
	private Integer pages;
	
	private Integer per_page;
	
	private List<FlickrPhoto> photo;

	public FlickrPhotoset() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwnername() {
		return ownername;
	}

	public void setOwnername(String ownername) {
		this.ownername = ownername;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	public Integer getPer_page() {
		return per_page;
	}

	public void setPer_page(Integer per_page) {
		this.per_page = per_page;
	}

	@Override
	public List<FlickrPhoto> getPhotos() {
		return photo;
	}
	
	
}