package net.onamap.server.controller.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.googlecode.objectify.Key;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.onamap.server.controller.AbstractController;
import net.onamap.server.dao.GMapsModelDAOImpl;
import net.onamap.server.dao.PhotoDAOImpl;
import net.onamap.server.dao.PhotosetDAOImpl;
import net.onamap.server.dao.UserDAOImpl;
import net.onamap.server.util.UnitedStates;
import net.onamap.shared.model.*;
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
    private static GMapsModelDAOImpl osmPlaceDAO = new GMapsModelDAOImpl();

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

    @Data
    @NoArgsConstructor
    @ToString
    public static class LocationGroup implements Serializable {

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

    @Data
    @NoArgsConstructor
    public static class PhotoLite {

        private String url_sq;
        private String url_s;
        private String title;
        private Double lat;
        private Double lng;
        private String link;
        private Long datetaken;

        public PhotoLite(String flickrId, Photo photo) {
            url_sq = photo.getUrl_sq();
            url_s = photo.getUrl_s();
            lat = photo.getLatitude();
            lng = photo.getLongitude();
            if (photo.getDatetaken() != null) {
                this.datetaken = photo.getDatetaken().getTime();
            }
            title = photo.getTitle();
            link = "https://www.flickr.com/photos/" + flickrId + "/" + photo.getId();
        }
    }

    public void doGet(User subdomainUser, HttpServletRequest request) {
        log.info("Calculate Stats here");
        String flickrPhotosetId = subdomainUser.getFlickrPhotosetId();
        if (subdomainUser != null && flickrPhotosetId != null) {
            doWork(request, subdomainUser);
        }
    }

    private void doWork(HttpServletRequest request, User subdomainUser) {

        FlickrUserInfo flickrInfo = subdomainUser.getFlickrInfo();
        String flickrPhotosetId = subdomainUser.getFlickrPhotosetId();
        log.info("Photoset: " + flickrPhotosetId);
        // Perform Analysis

        // Create a map of the photos

        int maxPhotosInState = 0;
        Photoset photoset = photosetDao.findPhotoset(flickrPhotosetId);


        Map<String, Photo> photosByIdInDB = photoDao.getPhotosByIds(photoset.getPhotoIds());
        Map<String, PhotoLite> photosMap = new HashMap<String, PhotoLite>();
        LocationGroup world = new LocationGroup();
        for (Photo photo : photosByIdInDB.values()) {
            photosMap.put(photo.getId(), new PhotoLite(flickrInfo.getId(), photo));
            CityStateCountry address = photo.getCityStateCountry();

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
                            LocationGroup city = state.getPlace(cityName);
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
        gson.toJson(UnitedStates.getMap());
        json.put("photosMap", photosMap);
        gson.toJson(photosMap);
        json.put("maxPhotosInState", maxPhotosInState);
        gson.toJson(maxPhotosInState);
        json.put("world", world);
        gson.toJson(world.getPhotos());
        gson.toJson(world.getPlaces());

        String jsonString = gson.toJson(json);

        request.setAttribute("json", jsonString);
    }

    private Map<Long, GMapsModel> getPlaceMap(HashSet<Key<GMapsModel>> placeIds) {

        Map<Long, GMapsModel> placeMap = new HashMap<Long, GMapsModel>();
        Map<Key<GMapsModel>, GMapsModel> places = osmPlaceDAO.get(placeIds);
        for (Key<GMapsModel> key : places.keySet()) {
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
            Map<String, Photo> photosMap, Map<Long, GMapsModel> placeMap) {
        Map<Long, List<String>> placeToPhotosMap = new HashMap<Long, List<String>>();

        for (Photo photo : photosMap.values()) {
            log.info("Stats On: " + photo.getTitle());
            Long key = photo.getGmapsId();
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
