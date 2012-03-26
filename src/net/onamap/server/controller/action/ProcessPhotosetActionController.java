//package net.onamap.server.controller.action;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.mortbay.log.Log;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import net.onamap.server.controller.AbstractController;
//import net.onamap.server.dao.OSMPlaceDAOImpl;
//import net.onamap.server.dao.PhotoDAOImpl;
//import net.onamap.shared.model.OSMPlace;
//import net.onamap.shared.model.Photo;
//
//import com.google.gson.Gson;
//import com.google.inject.Singleton;
//import com.handstandtech.flickr.shared.model.FlickrPhoto;
//import com.handstandtech.restclient.server.RESTClient;
//import com.handstandtech.restclient.server.impl.RESTClientAppEngineURLFetchImpl;
//import com.handstandtech.restclient.shared.model.RESTResult;
//import com.handstandtech.restclient.shared.model.RequestMethod;
//import com.handstandtech.restclient.shared.util.RESTURLUtil;
//
//@SuppressWarnings("serial")
//@Singleton
//public class ProcessPhotosetActionController extends AbstractController {
//	private static Logger log = LoggerFactory
//			.getLogger(ProcessPhotosetActionController.class.getName());
//	private static PhotoDAOImpl photoDao = new PhotoDAOImpl();
//
//	public void doGet(HttpServletRequest request, HttpServletResponse response)
//			throws IOException {
//
//		List<Photo> photos = (List<Photo>) request.getAttribute("toGeocode");
//
//		
//	}
//
//}
