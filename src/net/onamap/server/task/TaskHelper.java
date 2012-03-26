package net.onamap.server.task;

import java.util.Date;

import net.onamap.server.controller.action.FlickrLoadPhotosetActionController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Builder;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

public class TaskHelper {
	private static Logger log = LoggerFactory.getLogger(TaskHelper.class
			.getName());

	private static final String TASK = "/task/";
	public static final String REVERSE_GEOCODE_TASK_URL = TASK
			+ "reverse_geocode*";

	public static void queueReverseGeocode(Long photoId) {
		Queue queue = QueueFactory.getDefaultQueue();

		TaskOptions taskOptions = Builder.withDefaults();
		taskOptions.taskName("Reverse-Geocode-Photo-" + photoId + "--"
				+ new Date().getTime());
		taskOptions.url(REVERSE_GEOCODE_TASK_URL);

		// Set Parameter
		taskOptions.param(ReverseGeocodeTaskServlet.PHOTO_ID_PARAM,
				photoId.toString());
		taskOptions.method(Method.POST);

		log.info("Reverse Geocode Task Queued: " + photoId);
		queue.add(taskOptions);
	}
}
