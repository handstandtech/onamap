package net.onamap.server.task;

import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.onamap.server.dao.GMapsModelDAOImpl;
import net.onamap.server.dao.PhotoDAOImpl;
import net.onamap.shared.model.CityStateCountry;
import net.onamap.shared.model.GMapsModel;
import net.onamap.shared.model.Photo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;


@SuppressWarnings("serial")
@Singleton
@Path("/tasks")
@Slf4j
public class ReverseGeocodeTaskServlet {

    public static final String PHOTO_ID_PARAM = "photo_id";

    private static PhotoDAOImpl photoDao = new PhotoDAOImpl();

    /**
     * Default Serialization UID
     */
    private static final long serialVersionUID = 1L;

    private static final GMaps gmaps = new GMaps();
    private static GMapsModelDAOImpl gmapsDao = new GMapsModelDAOImpl();

    @POST
    @Path("/reverse_geocode")
    public Response reverseGeocode(@Context HttpServletRequest request, @Context HttpServletResponse response, @FormParam(PHOTO_ID_PARAM) String photoIdStr) {
        Photo photo = photoDao.findPhotoByFlickrId(photoIdStr);

        double lat = photo.getLatitude();
        double lon = photo.getLongitude();
        log.debug("LatLng: " + lat + "," + lon);


        GMapsModel gMapsModel = gmapsDao.findPlaceByLatLng(lat, lon);
        if (gMapsModel == null) {
            gMapsModel = gmaps.getGMapsModel(lat, lon);
        }


        if (gMapsModel != null) {
            GMapsModelDAOImpl mapsModelDAO = new GMapsModelDAOImpl();
            Long placeId = mapsModelDAO.update(gMapsModel);

            CityStateCountry cityStateCountry = getCityStateCountry(gMapsModel);
            photo.setCityStateCountry(cityStateCountry);
            photo.setGmapsId(placeId);
            photoDao.updatePhoto(photo);
        }
        return Response.ok().build();
    }

    private CityStateCountry getCityStateCountry(GMapsModel gMapsModel) {
        CityStateCountry csc = new CityStateCountry();
//        csc.setCity(gMapsModel.getCity().getLongName());
//        csc.setState(gMapsModel.getState().getLongName());
//        csc.setCountry(gMapsModel.getCountry().getLongName());
        return csc;
    }

}
