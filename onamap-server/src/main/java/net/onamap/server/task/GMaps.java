package net.onamap.server.task;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Singleton;
import com.google.maps.onamapmodels.AddressComponentType;
import com.google.maps.onamapmodels.GeocodingResult;
import com.handstandtech.restclient.server.RESTClient;
import com.handstandtech.restclient.server.impl.RESTClientJavaNetImpl;
import com.handstandtech.restclient.server.model.RESTRequest;
import com.handstandtech.restclient.shared.model.RESTResult;
import com.handstandtech.restclient.shared.model.RequestMethod;
import com.handstandtech.restclient.shared.util.RESTURLUtil;
import lombok.extern.slf4j.Slf4j;
import net.onamap.shared.model.GMapsModel;

import javax.ws.rs.Path;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("serial")
@Singleton
@Path("/tasks")
@Slf4j
public class GMaps {

    static String GOOGLE_MAPS_API_KEY = "AIzaSyAmkYEpXWUPxtrjGp0nOB9Buk9V2lXeLKg";

    private static Gson gson;

    public GMaps() {
        gson = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(AddressComponentType.class,
                        new UppercaseEnumAdapter())
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }


    public static void main(String[] args) throws Exception {
        Double lat = 37.562651;
        Double lng = -77.500163;

        GMapsModel gMapsModel = new GMaps().getGMapsModel(lat, lng);

        System.out.println("----------------");
        System.out.println(gson.toJson(gMapsModel));
    }


    public GMapsModel getGMapsModel(Double lat, Double lng) {
        GMapsModel model = null;

        try {
            GeocodingResult[] results = makeRestCall(lat, lng);

            if (results != null && results.length > 0) {
                model = new GMapsModel();
                GeocodingResult result = results[0];
                model.setGeocodingResult(result);
                model.setLat(lat);
                model.setLng(lng);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.debug("MODEL for " + lat + ", " + lng + " - " + gson.toJson(model));

        return model;
    }

    //----------
    GeocodingResult[] makeRestCall(double lat, double lng) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("latlng", lat + "," + lng);
        params.put("key", GOOGLE_MAPS_API_KEY);
        log.debug("LatLng: " + params);

//        RESTClient client = new RESTClientAppEngineURLFetchImpl();
        RESTClient client = new RESTClientJavaNetImpl();

        String reverseGeocodeUrl = RESTURLUtil.createFullUrl(
                "https://maps.googleapis.com/maps/api/geocode/json", params);

        RESTRequest restRequest = new RESTRequest(RequestMethod.GET, reverseGeocodeUrl);
        log.info("REST REQUEST: \n" + restRequest + "");
        RESTResult restResult = client.request(restRequest);
        log.info("REST RESPONSE: \n" + restResult);
        GMapsResponseWrapper wrapper = gson.fromJson(restResult.getResponseBody(),
                GMapsResponseWrapper.class);
        return wrapper.getResults();
    }
}
