package net.onamap.server.controller.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.googlecode.objectify.Key;
import net.onamap.server.constants.RequestParams;
import net.onamap.server.controller.AbstractController;
import net.onamap.server.dao.OSMPlaceDAOImpl;
import net.onamap.server.dao.PhotoDAOImpl;
import net.onamap.server.dao.PhotosetDAOImpl;
import net.onamap.server.dao.UserDAOImpl;
import net.onamap.server.util.RequestHelper;
import net.onamap.server.util.UnitedStates;
import net.onamap.shared.model.OSMPlace;
import net.onamap.shared.model.OSMPlace.OSMAddress;
import net.onamap.shared.model.Photo;
import net.onamap.shared.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;

@SuppressWarnings("serial")
public class CalculatePhotosetStatsActionController extends AbstractController {

	private static PhotoDAOImpl photoDao = new PhotoDAOImpl();
	private static PhotosetDAOImpl photosetDao = new PhotosetDAOImpl();
	private static UserDAOImpl userDao = new UserDAOImpl();
	private static OSMPlaceDAOImpl osmPlaceDAO = new OSMPlaceDAOImpl();

	private static Logger log = LoggerFactory
			.getLogger(FlickrLoadPhotosetActionController.class.getName());

	// var world = {
	// photos : [ 1, 2, 3, 4 ],
	// places : {
	// "US" : {
	// photos : [ 1, 2, 3, 4 ],
	// places : {
	// photos : [ 1, 2, 3, 4 ],
	// "Virginia" : {
	// places : {
	// "Arlington" : [ 1, 2, 3, 4, 5 ]
	// }
	// },
	// "North Carolina" : {
	// photos : [ 1, 2, 3, 4 ],
	// places : {
	// "Durham" : [ 1, 2, 3, 4, 5 ]
	// }
	// },
	// "South Carolina" : {
	// photos : [ 1, 2, 3, 4, 5 ],
	// places : {
	// "Greenville" : {
	// photos : [ 1, 2, 3, 4, 5 ],
	// places : undefined
	// },
	// "Charleston" : [ 1, 2 ]
	// }
	// }
	// }
	// }
	// }
	// };

	public class LocationGroup implements Serializable {

		private Map<String, LocationGroup> places;
		private List<String> photos;

		public void addPhoto(Photo photo) {
			if (photos == null) {
				photos = new ArrayList<String>();
			}
			photos.add(photo.getId());
		}

		public LocationGroup getPlace(String key) {
			LocationGroup location = getPlaces().get(key);
			if (location == null) {
				location = new LocationGroup();
			}
			return location;
		}

		private Map<String, LocationGroup> getPlaces() {
			if (places == null) {
				places = new HashMap<String, LocationGroup>();
			}
			return places;
		}

		public void putPlace(String key, LocationGroup location) {
			getPlaces().put(key, location);
		}

		public int getPhotoCount() {
			int size = 0;
			if (photos != null) {
				size = photos.size();
			}
			return size;
		}

	}

	public class PhotoLite {

		private String url_sq;
		private String url_s;
		private String title;
		private Double lat;
		private Double lng;

		public PhotoLite(Photo photo) {
			url_sq = photo.getUrl_sq();
			url_s = photo.getUrl_s();
			lat = photo.getLatitude();
			lng = photo.getLongitude();
			title = photo.getTitle();
		}
	}

	public void doGet(HttpServletRequest request){
		log.info("Calculate Stats here");

		String flickrPhotosetId = RequestHelper.getStringValue(request,
				RequestParams.PHOTOSET_ID);
		if (flickrPhotosetId == null) {
			String subdomain = RequestHelper.getStringValue(request,
					RequestParams.SUBDOMAIN);
			if (!isNullOrEmpty(subdomain)) {
				User user = userDao.findUserByUsername(subdomain);
				if (user != null) {
                    flickrPhotosetId = user.getFlickrPhotosetId();
				}
			}
		}

		if (flickrPhotosetId != null) {
			doWork(request, flickrPhotosetId);
		}
	}

	private void doWork(HttpServletRequest request, String flickrPhotosetId) {
		log.info("Photoset: " + flickrPhotosetId);
		// Perform Analysis

		// Create a map of the photos

		int maxPhotosInState = 0;
		List<Photo> photos = photoDao.getPhotosInFlickrPhotoset(flickrPhotosetId);
		Map<String, PhotoLite> photosMap = new HashMap<String, PhotoLite>();
		LocationGroup world = new LocationGroup();
		for (Photo photo : photos) {
			photosMap.put(photo.getId(), new PhotoLite(photo));
			OSMAddress address = photo.getAddress();

			if (address != null) {
				String countryName = address.getCountry();
				String stateName = address.getState();
				String cityName = address.getCity();

				boolean hasCountry = !isNullOrEmpty(countryName);
				boolean hasState = !isNullOrEmpty(stateName);
				boolean hasCity = !isNullOrEmpty(cityName);

				world.addPhoto(photo);
				if (hasCountry) {
					LocationGroup country = world.getPlace(countryName);
					country.addPhoto(photo);
					if (hasState) {
						LocationGroup state = country.getPlace(stateName);
						state.addPhoto(photo);
						if (hasCity) {
							LocationGroup city = country.getPlace(cityName);
							city.addPhoto(photo);
							state.putPlace(cityName, city);
						}
						country.putPlace(stateName, state);

						int statePhotocount = state.getPhotoCount();
						if (statePhotocount > maxPhotosInState) {
							maxPhotosInState = statePhotocount;
						}
					}
					world.putPlace(countryName, country);
				}
			}
		}

		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		Map json = new HashMap();
		json.put("states", UnitedStates.getMap());
		// json.put("usMap", usMap);
		// json.put("worldMap", worldMap);
		// json.put("placeToPhotosMap", placeToPhotosMap);
		json.put("photosMap", photosMap);
		json.put("maxPhotosInState", maxPhotosInState);
		// json.put("placeMap", placeMap);
		json.put("world", world);

		String jsonString = gson.toJson(json);

		request.setAttribute("json", jsonString);
	}

	private Map<Long, OSMPlace> getPlaceMap(HashSet<Key<OSMPlace>> placeIds) {

		Map<Long, OSMPlace> placeMap = new HashMap<Long, OSMPlace>();
		Map<Key<OSMPlace>, OSMPlace> places = osmPlaceDAO.getPlaces(placeIds);
		for (Key<OSMPlace> key : places.keySet()) {
			placeMap.put(key.getId(), places.get(key));
		}
		return placeMap;
	}

	// private Map<String, List<String>> getPhotosInState(
	// Map<Long, List<Photo>> placeToPhotosMap,
	// Map<Long, OSMPlace> placeMap) {
	// Map<String, List<Photo>> statePhotosMap = new HashMap<String,
	// List<Photo>>();
	// for (OSMPlace place : placeMap.values()) {
	// OSMAddress address = place.getAddress();
	// String country = address.getCountry();
	//
	// }
	// return statePhotosMap;
	// }

	private Map<Long, List<String>> getPlaceToPhotosMap(
			Map<String, Photo> photosMap, Map<Long, OSMPlace> placeMap) {
		Map<Long, List<String>> placeToPhotosMap = new HashMap<Long, List<String>>();

		for (Photo photo : photosMap.values()) {
			log.info("Stats On: " + photo.getTitle());
			Long key = photo.getOsmPlaceId();
			if (key != null) {
				List<String> photosInMap = placeToPhotosMap.get(key);
				if (photosInMap == null) {
					photosInMap = new ArrayList<String>();
				}
				photosInMap.add(photo.getId());
				placeToPhotosMap.put(key, photosInMap);
			}
		}
		return placeToPhotosMap;
	}
}
