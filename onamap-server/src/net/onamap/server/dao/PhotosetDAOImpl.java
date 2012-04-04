package net.onamap.server.dao;

import java.util.List;

import net.onamap.shared.model.Photoset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class PhotosetDAOImpl extends AbstractDAOBase {

	private static Logger log = LoggerFactory.getLogger(PhotosetDAOImpl.class);

	static {
		ObjectifyService.register(Photoset.class);
	}

	public PhotosetDAOImpl() {
	}

	public Long updatePhotoset(Photoset photoset) {
		Long photosetId = null;
		Objectify ofyTxn = ofyTrans();
		try {
			photosetId = ofyTxn.put(photoset).getId();
			ofyTxn.getTxn().commit();
		} finally {
			if (ofyTxn.getTxn().isActive()) {
				ofyTxn.getTxn().rollback();
			}
		}
		return photosetId;
	}

	public Photoset findPhotoset(Long id) {
		Photoset photoset = null;
		try {
			photoset = ofy().get(Photoset.class, id);
		} catch (NotFoundException e) {

		}
		return photoset;
	}

	public void deleteAllPhotosets() {
		List<Key<Photoset>> keys = ofy().query(Photoset.class).listKeys();
		ofy().delete(keys);
	}

	public Photoset findPhotosetForUserId(Long id) {
		Query<Photoset> q = ofy().query(Photoset.class);
		q.filter("userId", id);
		return q.get();
	}

	public Photoset findPhotosetByFlickrId(String flickrPhotosetId) {
		Query<Photoset> q = ofy().query(Photoset.class);
		q.filter("flickrPhotosetId", flickrPhotosetId);
		try {
			return q.get();
		} catch (NotFoundException e) {
			return null;
		}
	}

}