package net.onamap.shared.model;

import com.googlecode.objectify.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.PrePersist;
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
	@PrePersist
	private void PrePersist() {
		Date now = new Date();
		if (createdDate == null) {
			createdDate = now;
		}
		lastLoginDate = now;
	}

    @Data
    @NoArgsConstructor
    @Embed
	public static class FlickrUserInfo implements Serializable {

		private static final long serialVersionUID = 1L;

		private String username;
		private String id;
        private String nsid;
		private String token;
		private String tokenSecret;
	}

}
