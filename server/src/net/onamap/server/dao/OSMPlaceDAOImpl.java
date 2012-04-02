package net.onamap.server.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.onamap.shared.model.OSMPlace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.ObjectifyService;

public class OSMPlaceDAOImpl extends AbstractDAOBase {

	private static Logger log = LoggerFactory.getLogger(OSMPlaceDAOImpl.class);

	static {
		ObjectifyService.register(OSMPlace.class);
	}

	public OSMPlaceDAOImpl() {
		super();
	}

	public Long updateOSMPlace(OSMPlace place) {
		return ofyTrans().put(place).getId();
	}

	public void deleteAllOSMPlaces() {
		List<Key<OSMPlace>> keys = ofy().query(OSMPlace.class).listKeys();
		ofy().delete(keys);
	}

	public OSMPlace findOSMPlace(Long key) {
		try {
			return ofy().get(OSMPlace.class, key);
		} catch (NotFoundException e) {
			return null;
		}
	}

	public Map<Key<OSMPlace>, OSMPlace> getPlaces(
			HashSet<Key<OSMPlace>> placeIds) {
		return ofy().get(placeIds);
	}
}