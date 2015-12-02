package net.onamap.server.dao;

import com.googlecode.objectify.cmd.QueryKeys;
import lombok.NoArgsConstructor;
import net.onamap.shared.model.Photoset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static net.onamap.server.objectify.OfyService.ofy;

@NoArgsConstructor
public class PhotosetDAOImpl {

    private static Logger log = LoggerFactory.getLogger(PhotosetDAOImpl.class);


    public String updatePhotoset(Photoset photoset) {
        return ofy().save().entity(photoset).now().getName();
    }

    public Photoset findPhotoset(String id) {
        return ofy().load().type(Photoset.class).id(id).now();
    }

    public void deleteAllPhotosets() {
        QueryKeys keys = ofy().load().type(Photoset.class).keys();
        ofy().delete().keys(keys);
    }

    public Photoset findPhotosetForUserId(Long id) {
        return ofy().load().type(Photoset.class).filter("userId", id).first().now();
    }

    public Photoset findPhotosetByFlickrId(String flickrPhotosetId) {
        return ofy().load().type(Photoset.class).filter("flickrPhotosetId", flickrPhotosetId).first().now();
    }

    public List<Photoset> getAll() {
            return ofy().load().type(Photoset.class).list();
    }
}