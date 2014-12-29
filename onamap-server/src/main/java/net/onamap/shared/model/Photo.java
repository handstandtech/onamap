package net.onamap.shared.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.handstandtech.flickr.shared.model.FlickrPhoto;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.onamap.shared.model.OSMPlace.OSMAddress;

import javax.persistence.PrePersist;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Photo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private Date datetaken;

    private Double latitude;

    private Double longitude;

    private String title;

    private String url_sq;
    private String url_t;
    private String url_s;
    private String url_m;
    private String url_o;

    @Index
    private String flickrPhotosetId;

    private OSMAddress address;

    private Long osmPlaceId;

    private Long flickrLastUpdatedTime;

    private Date lastUpdated;

    public Photo(FlickrPhoto flickrPhoto) {
        setFlickrPhoto(flickrPhoto);
    }

    @PrePersist
    public void PrePersist() {
        this.lastUpdated = new Date();
    }

    public Date getLastUpdated() {
        if (lastUpdated == null) {
            lastUpdated = new Date();
        }
        return lastUpdated;
    }


    public void setFlickrPhoto(FlickrPhoto p) {
        this.id = p.getId();
        this.url_m = p.getUrl_m();
        this.url_o = p.getUrl_o();
        this.url_s = p.getUrl_s();
        this.url_sq = p.getUrl_sq();
        this.url_t = p.getUrl_t();
        this.datetaken = p.getDatetaken();
        this.flickrLastUpdatedTime = p.getLastupdate();
        this.latitude = p.getLatitude();
        this.longitude = p.getLongitude();
        this.title = p.getTitle();
    }

}