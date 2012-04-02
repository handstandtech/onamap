package net.onamap.shared.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import com.googlecode.objectify.annotation.Cached;

@Cached
public class User implements Serializable {

	static final long serialVersionUID = 1L;

	@Id
	private Long id;
	private String email;
	private String username;
	private Date createdDate;
	private Date lastLoginDate;
	private String passwordHash;
	
	private Long photosetId;

	@Embedded
	private FlickrUserInfo flickrInfo;

	public User() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	@SuppressWarnings("unused")
	@PrePersist
	private void PrePersist() {
		Date now = new Date();
		if (createdDate == null) {
			createdDate = now;
		}
		lastLoginDate = now;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public FlickrUserInfo getFlickrInfo() {
		return flickrInfo;
	}

	public void setFlickrInfo(FlickrUserInfo flickrInfo) {
		this.flickrInfo = flickrInfo;
	}

	public Long getPhotosetId() {
		return photosetId;
	}

	public void setPhotosetId(Long photosetId) {
		this.photosetId = photosetId;
	}

	public static class FlickrUserInfo implements Serializable {

		private static final long serialVersionUID = 1L;

		private String username;
		private String id;

		private String token;
		private String tokenSecret;

		public FlickrUserInfo() {

		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public String getTokenSecret() {
			return tokenSecret;
		}

		public void setTokenSecret(String tokenSecret) {
			this.tokenSecret = tokenSecret;
		}
	}

}
