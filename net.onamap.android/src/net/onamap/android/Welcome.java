package net.onamap.android;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import oauth.signpost.OAuthConsumer;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.handstandtech.flickr.server.FlickrHelper;
import com.handstandtech.flickr.shared.model.FlickrUser;
import com.handstandtech.restclient.server.auth.ApacheCommonsOAuthAuthenticator;
import com.handstandtech.restclient.server.auth.Authenticator;
import com.handstandtech.restclient.server.impl.RESTClientCommonsImpl;
import com.handstandtech.restclient.server.model.RESTRequest;
import com.handstandtech.restclient.shared.model.RESTResult;
import com.handstandtech.restclient.shared.model.RequestMethod;
import com.handstandtech.restclient.shared.util.RESTURLUtil;

public class Welcome extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// RESTClientCommonsImpl client = new RESTClientCommonsImpl();
		// RESTResult result = client.request(RequestMethod.GET,
		// "http://google.com");
		// Log.d("", result.toString());

		go();

	}

	public void go() {
		String token = "72157629620912897-909707b0f728d0ef";
		String tokenSecret = "9754f52f99728b82";

		// TEST LOGIN
		FlickrHelper flickr = FlickrConstants.createFlickrHelper(token,
				tokenSecret);
		FlickrUser flickrUser = flickr.test_login();
		Toast.makeText(Welcome.this, flickrUser.getUsername().get_content(),
				Toast.LENGTH_SHORT).show();

		// UPLOAD PHOTO
		try {

			String title = "Handstand Tech";
			InputStream imageInputStream = getResources().getAssets().open(
					"PheonixInternationalRaceway.jpg");

			OAuthConsumer consumer = FlickrConstants.getConsumer();
			consumer.setTokenWithSecret(token, tokenSecret);
			Authenticator auth = new ApacheCommonsOAuthAuthenticator(consumer);

			Map<String, String> params = new HashMap<String, String>();
			String url = RESTURLUtil.createFullUrl(UPLOAD_API_ENDPOINT, params);

			RESTRequest restRequest = new RESTRequest(RequestMethod.POST, url,
					auth);

			RESTClientCommonsImpl client = new RESTClientCommonsImpl();

			MultipartEntity multipartEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);

			multipartEntity.addPart("photo", new InputStreamBody(
					imageInputStream, title));

			RESTResult result = client.request(restRequest, multipartEntity);
			System.out.println(result + "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static String UPLOAD_API_ENDPOINT = "http://api.flickr.com/services/upload/";
}