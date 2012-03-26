package net.onamap.shared.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import net.onamap.shared.model.OSMPlace.OSMAddress;

import com.handstandtech.flickr.shared.model.FlickrPhoto;

public class Photo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@Embedded
	private FlickrPhoto flickrPhoto;

	private String flickrPhotosetId;
	
	private Long photosetId;

	@Embedded
	private OSMAddress address;

	private Long osmPlaceId;

	private Date lastUpdated;

	public Photo() {
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

	public FlickrPhoto getFlickrPhoto() {
		return flickrPhoto;
	}

	public void setFlickrPhoto(FlickrPhoto flickrPhoto) {
		this.flickrPhoto = flickrPhoto;
	}

	public Long getOsmPlaceId() {
		return osmPlaceId;
	}

	public void setOsmPlaceId(Long osmPlaceId) {
		this.osmPlaceId = osmPlaceId;
	}

	public String getFlickrPhotosetId() {
		return flickrPhotosetId;
	}

	public void setFlickrPhotosetId(String flickrPhotosetId) {
		this.flickrPhotosetId = flickrPhotosetId;
	}

	public OSMAddress getAddress() {
		return address;
	}

	public void setAddress(OSMAddress address) {
		this.address = address;
	}

	public Long getPhotosetId() {
		return photosetId;
	}

	public void setPhotosetId(Long photosetId) {
		this.photosetId = photosetId;
	}

}