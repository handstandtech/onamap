package net.onamap.shared.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.PrePersist;

public class Photoset implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	private String flickrPhotosetId;

	private Long userId;

	private Date lastUpdated;

	public Photoset() {
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	@PrePersist
	public void PrePersist() {
		Date now = new Date();
		this.lastUpdated = now;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFlickrPhotosetId() {
		return flickrPhotosetId;
	}

	public void setFlickrPhotosetId(String flickrPhotosetId) {
		this.flickrPhotosetId = flickrPhotosetId;
	}

}