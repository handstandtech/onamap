package net.onamap.shared.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.PrePersist;
import java.io.Serializable;
import java.util.Date;

@Entity
@NoArgsConstructor
@Data
public class Photoset implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
	private String id;

    @Index
	private Long userId;

    private String title;

    private Integer count;

    private String description;

    @Index
	private Date lastUpdated;

	@PrePersist
	public void PrePersist() {
		Date now = new Date();
		this.lastUpdated = now;
	}

}