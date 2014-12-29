package net.onamap.server.dao;//package net.onamap.server.dao;
//
//import java.util.Collection;
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.googlecode.objectify.Key;
//import com.googlecode.objectify.ObjectifyService;
//import com.googlecode.objectify.Query;
//import com.googlecode.objectify.util.DAOBase;
//import com.handstandtech.flickr.shared.model.FlickrPhoto;
//
//public class FlickrPhotoDAOImpl extends DAOBase {
//
//	private static Logger log = LoggerFactory
//			.getLogger(FlickrPhotoDAOImpl.class);
//
//	static {
//		ObjectifyService.register(FlickrPhoto.class);
//	}
//
//	public FlickrPhotoDAOImpl() {
//
//	}
//
//	public void updateFlickrPhotos(Collection<FlickrPhoto> photos) {
//		ofy().put(photos);
//	}
//
//	public List<FlickrPhoto> getFlickrPhotosInPhotoset(String photosetId) {
//		Query<FlickrPhoto> q = ofy().query(FlickrPhoto.class);
//		q.filter("photosetId", photosetId);
//		return q.list();
//	}
//
//	public void deleteAllFlickrPhotos() {
//		List<Key<FlickrPhoto>> keys = ofy().query(FlickrPhoto.class).listKeys();
//		ofy().delete(keys);
//	}
//
//	public FlickrPhoto findFlickrPhoto(String id) {
//		return ofy().get(FlickrPhoto.class, id);
//	}
//
//	public void updateFlickrPhoto(FlickrPhoto photo) {
//		ofy().put(photo);
//	}
//}