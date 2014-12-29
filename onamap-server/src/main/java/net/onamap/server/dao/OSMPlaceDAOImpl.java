package net.onamap.server.dao;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.QueryKeys;
import lombok.NoArgsConstructor;
import net.onamap.shared.model.OSMPlace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;

import static net.onamap.server.objectify.OfyService.ofy;

@NoArgsConstructor
public class OSMPlaceDAOImpl {

    private static Logger log = LoggerFactory.getLogger(OSMPlaceDAOImpl.class);


    public Long updateOSMPlace(OSMPlace place) {
        return ofy().save().entity(place).now().getId();
    }

    public void deleteAllOSMPlaces() {
        QueryKeys keys = ofy().load().type(OSMPlace.class).keys();
        ofy().delete().keys(keys);
    }

    public OSMPlace findOSMPlace(Long id) {
        return ofy().load().type(OSMPlace.class).id(id).now();
    }

    public Map<Key<OSMPlace>, OSMPlace> getPlaces(
            HashSet<Key<OSMPlace>> placeIds) {
        return ofy().load().keys(placeIds);
    }
}