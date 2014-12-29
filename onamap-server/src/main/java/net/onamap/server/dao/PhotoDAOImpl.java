package net.onamap.server.dao;

import com.googlecode.objectify.cmd.QueryKeys;
import lombok.NoArgsConstructor;
import net.onamap.shared.model.Photo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static net.onamap.server.objectify.OfyService.ofy;

@NoArgsConstructor
public class PhotoDAOImpl {

    private static Logger log = LoggerFactory.getLogger(PhotoDAOImpl.class);

    public String updatePhoto(Photo photo) {
        return ofy().save().entity(photo).now().getName();
    }

    public void deleteAllPhotos() {
        QueryKeys keys = ofy().load().type(Photo.class).keys();
        ofy().delete().keys(keys);
    }

    public List<Photo> getPhotosInFlickrPhotoset(String flickrPhotosetId) {
        return ofy().load().type(Photo.class).filter("flickrPhotosetId", flickrPhotosetId).list();

    }
    public Photo findPhotoByFlickrId(String id) {
        return ofy().load().type(Photo.class).id(id).now();
    }
}