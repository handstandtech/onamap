package net.onamap.shared.model;

import com.googlecode.objectify.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Cache
@NoArgsConstructor
@Data
@Entity
public class User implements Serializable {

	static final long serialVersionUID = 1L;

	@Id
	private Long id;

    @Index
	private String email;

    @Index
	private String username;

	private Date createdDate;
	private Date lastLoginDate;
	private String passwordHash;

    @Index
	private String flickrPhotosetId;

	private FlickrUserInfo flickrInfo;

	@SuppressWarnings("unused")
	@OnSave
	private void PrePersist() {
		Date now = new Date();
		if (createdDate == null) {
			createdDate = now;
		}
		lastLoginDate = now;
	}



}
