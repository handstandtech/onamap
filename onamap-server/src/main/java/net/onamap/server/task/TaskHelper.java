package net.onamap.server.task;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Builder;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class TaskHelper {
	private static Logger log = LoggerFactory.getLogger(TaskHelper.class
			.getName());

	public static void queueReverseGeocode(String flickrPhotoId) {
		Queue queue = QueueFactory.getDefaultQueue();

		TaskOptions taskOptions = Builder.withDefaults();
		taskOptions.taskName("Reverse-Geocode-Photo-" + flickrPhotoId + "--"
				+ new Date().getTime());
		taskOptions.url("/tasks/reverse_geocode");

		// Set Parameter
		taskOptions.param(ReverseGeocodeTaskServlet.PHOTO_ID_PARAM,
                flickrPhotoId);
		taskOptions.method(Method.POST);

		log.info("Reverse Geocode Task Queued: " + flickrPhotoId);
		queue.add(taskOptions);
	}
}
