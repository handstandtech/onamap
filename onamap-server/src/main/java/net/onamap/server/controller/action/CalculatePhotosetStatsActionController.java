package net.onamap.server.controller.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.onamapmodels.AddressComponent;
import com.google.maps.onamapmodels.AddressComponentType;
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
        private String url_m;
        private String title;
        private Double lat;
        private Double lng;
        private String link;
        private Long datetaken;
        private Long placeId;

        public PhotoLite(String flickrId, Photo photo, Long placeId) {
            url_sq = photo.getUrl_sq();
            url_s = photo.getUrl_s();
            url_m = photo.getUrl_m();
            lat = photo.getLatitude();
            lng = photo.getLongitude();
            if (photo.getDatetaken() != null) {
                this.datetaken = photo.getDatetaken().getTime();
            }
            title = photo.getTitle();
            link = "https://www.flickr.com/photos/" + flickrId + "/" + photo.getId();
            this.placeId = placeId;
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

        FlickrUserInfo flickrUserInfo = subdomainUser.getFlickrInfo();
        String flickrPhotosetId = subdomainUser.getFlickrPhotosetId();
        log.info("Photoset: " + flickrPhotosetId);
        // Perform Analysis

        // Create a map of the photos
        int maxPhotosInState = 0;
        Photoset photoset = photosetDao.findPhotoset(flickrPhotosetId);


        Collection<Photo> photosByIdInDB = photoDao.getPhotosByIds(photoset.getPhotoIds());

        HashSet<Long> gmapsIds = new HashSet<>();
        for (Photo photo : photosByIdInDB) {
            if (photo != null) {
                Long gmapsId = photo.getGmapsId();
                if (gmapsId != null) {
                    gmapsIds.add(gmapsId);
                }
            }
        }

        Map<Long, GMapsModel> gMapsModelMap = new GMapsModelDAOImpl().getByIds(gmapsIds);

        Map<String, PhotoLite> photosMap = new HashMap<String, PhotoLite>();
        LocationGroup world = new LocationGroup();
        for (Photo photo : photosByIdInDB) {
            Long placeId = photo.getGmapsId();
            photosMap.put(photo.getId(), new PhotoLite(flickrUserInfo.getId(), photo, placeId));

            CityStateCountry address = null;

            GMapsModel gMapsModel = gMapsModelMap.get(placeId);
            if (gMapsModel != null) {
                address = getCityStateCountry(gMapsModel);
            }

            if (address != null) {
                String countryName = address.getCountry();
                String stateName = address.getState();
                String cityName = address.getCity();

                boolean hasCountry = !isNullOrEmpty(countryName);
                boolean hasState = !isNullOrEmpty(stateName);
                boolean hasCity = !isNullOrEmpty(cityName);

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


        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

        Map json = new HashMap();
        json.put("states", UnitedStates.getMap());
        json.put("photosMap", photosMap);
        json.put("maxPhotosInState", maxPhotosInState);
        json.put("world", world);

        String jsonString = gson.toJson(json);

        request.setAttribute("json", jsonString);
    }

    private CityStateCountry getCityStateCountry(GMapsModel gMapsModel) {
        CityStateCountry cityStateCountry = new CityStateCountry();

        ShortAndLongName city = getShortAndLongName(gMapsModel.getGeocodingResult().addressComponents, CITY_COMPONENT_TYPES);
        ShortAndLongName state = getShortAndLongName(gMapsModel.getGeocodingResult().addressComponents, STATE_COMPONENT_TYPES);
        ShortAndLongName country = getShortAndLongName(gMapsModel.getGeocodingResult().addressComponents, COUNTRY_COMPONENT_TYPES);

        if (city != null) {
            cityStateCountry.setCity(city.getLongName());
        }
        if (state != null) {
            cityStateCountry.setState(state.getLongName());
        }
        if (country != null) {
            cityStateCountry.setCountry(country.getLongName());
        }

        return cityStateCountry;
    }


    private static final List<AddressComponentType> CITY_COMPONENT_TYPES = Arrays.asList(AddressComponentType.LOCALITY, AddressComponentType.SUBLOCALITY, AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_2, AddressComponentType.POLITICAL);
    private static final List<AddressComponentType> STATE_COMPONENT_TYPES = Arrays.asList(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1);
    private static final List<AddressComponentType> COUNTRY_COMPONENT_TYPES = Arrays.asList(AddressComponentType.COUNTRY);

    private ShortAndLongName getShortAndLongName(List<AddressComponent> addressComponents, List<AddressComponentType> toMatchComponentTypes) {
        for (AddressComponent addressComponent : addressComponents) {
            for (AddressComponentType addressComponentType : addressComponent.types) {
                if (toMatchComponentTypes.contains(addressComponentType)) {
                    return getGMapsName(addressComponent);
                }
            }
        }
        return null;
    }

    ShortAndLongName getGMapsName(AddressComponent addressComponent) {
        ShortAndLongName name = new ShortAndLongName();

        name.setShortName(addressComponent.shortName);
        name.setLongName(addressComponent.longName);

        return name;
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
