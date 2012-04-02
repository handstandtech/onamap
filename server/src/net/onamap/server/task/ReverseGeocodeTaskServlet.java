package net.onamap.server.task;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.onamap.server.controller.action.CalculatePhotosetStatsActionController;
import net.onamap.server.dao.OSMPlaceDAOImpl;
import net.onamap.server.dao.PhotoDAOImpl;
import net.onamap.shared.model.OSMPlace;
import net.onamap.shared.model.Photo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.inject.Singleton;
import com.handstandtech.restclient.server.RESTClient;
import com.handstandtech.restclient.server.impl.RESTClientAppEngineURLFetchImpl;
import com.handstandtech.restclient.shared.model.RESTResult;
import com.handstandtech.restclient.shared.model.RequestMethod;
import com.handstandtech.restclient.shared.util.RESTURLUtil;

@Singleton
public class ReverseGeocodeTaskServlet extends HttpServlet {

	public static final String PHOTO_ID_PARAM = "photo_id";

	private static Logger log = LoggerFactory
			.getLogger(ReverseGeocodeTaskServlet.class);

	private static PhotoDAOImpl photoDao = new PhotoDAOImpl();

	/**
	 * Default Serialization UID
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// Get the UserId
		String photoIdStr = request.getParameter(PHOTO_ID_PARAM);

		Long photoId = Long.parseLong(photoIdStr);
		Photo photo = photoDao.findPhoto(photoId);

		Map<String, String> params = new HashMap<String, String>();
		params.put("format", "json");
		Double lat = photo.getFlickrPhoto().getLatitude();
		Double lon = photo.getFlickrPhoto().getLongitude();

		RESTClient client = new RESTClientAppEngineURLFetchImpl();

		Gson gson = new Gson();
		if (lat != 0.0 && lon != 0.0) {
			params.put("lat", lat.toString());
			params.put("lon", lon.toString());

			String reverseGeocodeUrl = RESTURLUtil.createFullUrl(
					"http://open.mapquestapi.com/nominatim/v1/reverse", params);

			RESTResult result = client.request(RequestMethod.GET,
					reverseGeocodeUrl);
			System.out.println(result);
			OSMPlace place = gson.fromJson(result.getResponseBody(),
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
	}

}
