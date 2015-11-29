package net.onamap.server.dao;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.QueryKeys;
import lombok.NoArgsConstructor;
import net.onamap.shared.model.GMapsModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;

import static net.onamap.server.objectify.OfyService.ofy;

@NoArgsConstructor
public class GMapsModelDAOImpl {

    private static Logger log = LoggerFactory.getLogger(GMapsModelDAOImpl.class);


    public Long update(GMapsModel place) {
        return ofy().save().entity(place).now().getId();
    }

    public void deleteAllOSMPlaces() {
        QueryKeys keys = ofy().load().type(GMapsModel.class).keys();
        ofy().delete().keys(keys);
    }

    public GMapsModel findPlace(Long id) {
        return ofy().load().type(GMapsModel.class).id(id).now();
    }

    public Map<Key<GMapsModel>, GMapsModel> get(
            HashSet<Key<GMapsModel>> placeIds) {
        return ofy().load().keys(placeIds);
    }
}