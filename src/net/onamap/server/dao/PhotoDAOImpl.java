package net.onamap.server.dao;

import java.util.List;

import net.onamap.shared.model.Photo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class PhotoDAOImpl extends AbstractDAOBase {

	private static Logger log = LoggerFactory.getLogger(PhotoDAOImpl.class);

	static {
		ObjectifyService.register(Photo.class);
	}

	public PhotoDAOImpl() {
	}

	public Long updatePhoto(Photo photo) {
		Long photoId = null;
		Objectify ofyTxn = ofyTrans();
		try {
			photoId = ofyTxn.put(photo).getId();
			ofyTxn.getTxn().commit();
		} finally {
			if (ofyTxn.getTxn().isActive()) {
				ofyTxn.getTxn().rollback();
			}
		}
		return photoId;
	}

	public Photo findPhoto(Long id) {
		Photo photo = null;
		try {
			photo = ofy().get(Photo.class, id);
		} catch (NotFoundException e) {

		}
		return photo;
	}

	public void deleteAllPhotos() {
		List<Key<Photo>> keys = ofy().query(Photo.class).listKeys();
		ofy().delete(keys);
	}

	public List<Photo> getPhotosInFlickrPhotoset(String photosetId) {
		Query<Photo> q = ofy().query(Photo.class);
		q.filter("flickrPhotosetId", photosetId);
		return q.list();

	}

	public List<Key<Photo>> getPhotoKeysInFlickrPhotoset(String photosetId) {
		Query<Photo> q = ofy().query(Photo.class);
		q.filter("flickrPhotosetId", photosetId);
		return q.listKeys();
	}

	public Photo findPhotoByFlickrId(String id) {
		Query<Photo> q = ofy().query(Photo.class);
		q.filter("flickrPhoto.id", id);
		try {
			return q.get();
		} catch (NotFoundException e) {

			return null;
		}
	}

	public List<Photo> getPhotosInPhotoset(Long photosetId) {
		Query<Photo> q = ofy().query(Photo.class);
		q.filter("photosetId", photosetId);
		return q.list();
	}
}