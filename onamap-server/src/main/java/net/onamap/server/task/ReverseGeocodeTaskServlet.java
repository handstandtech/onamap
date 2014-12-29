package net.onamap.server.task;

import com.google.gson.Gson;
import com.google.inject.Singleton;
import com.handstandtech.restclient.server.RESTClient;
import com.handstandtech.restclient.server.impl.RESTClientAppEngineURLFetchImpl;
import com.handstandtech.restclient.server.model.RESTRequest;
import com.handstandtech.restclient.shared.model.RESTResult;
import com.handstandtech.restclient.shared.model.RequestMethod;
import com.handstandtech.restclient.shared.util.RESTURLUtil;
import net.onamap.server.dao.OSMPlaceDAOImpl;
import net.onamap.server.dao.PhotoDAOImpl;
import net.onamap.shared.model.OSMPlace;
import net.onamap.shared.model.Photo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("serial")
@Singleton
@Path("/tasks")
public class ReverseGeocodeTaskServlet {

    public static final String PHOTO_ID_PARAM = "photo_id";

    private static Logger log = LoggerFactory
            .getLogger(ReverseGeocodeTaskServlet.class);

    private static PhotoDAOImpl photoDao = new PhotoDAOImpl();

    /**
     * Default Serialization UID
     */
    private static final long serialVersionUID = 1L;

    @POST
    @Path("/reverse_geocode")
    public Response reverseGeocode(@Context HttpServletRequest request, @Context HttpServletResponse response, @FormParam(PHOTO_ID_PARAM) String photoIdStr) {
        Photo photo = photoDao.findPhotoByFlickrId(photoIdStr);

        Map<String, String> params = new HashMap<String, String>();
        params.put("format", "json");
        Double lat = photo.getLatitude();
        Double lon = photo.getLongitude();

        RESTClient client = new RESTClientAppEngineURLFetchImpl();

        Gson gson = new Gson();
        if (lat != 0.0 && lon != 0.0) {
            params.put("lat", lat.toString());
            params.put("lon", lon.toString());

            String reverseGeocodeUrl = RESTURLUtil.createFullUrl(
                    "http://open.mapquestapi.com/nominatim/v1/reverse", params);

            RESTRequest restRequest = new RESTRequest(RequestMethod.GET, reverseGeocodeUrl);
            log.info("REST REQUEST: \n" + restRequest + "");
            RESTResult restResult = client.request(restRequest);
            log.info("REST RESPONSE: \n" + restResult);
            OSMPlace place = gson.fromJson(restResult.getResponseBody(),
                    OSMPlace.class);
            OSMPlaceDAOImpl osmPlaceDAO = new OSMPlaceDAOImpl();
            Long placeId = osmPlaceDAO.updateOSMPlace(place);

            photo.setOsmPlaceId(placeId);
            photo.setAddress(place.getAddress());
            photoDao.updatePhoto(photo);
        }

        // TODO: When all are done
        // new CalculatePhotosetStatsActionController().doGet(request,
        // response);
        return Response.ok().build();
    }

}
