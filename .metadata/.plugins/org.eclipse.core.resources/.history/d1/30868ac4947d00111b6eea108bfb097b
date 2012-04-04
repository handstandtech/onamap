/**
 * Handstand Technologies, LLC
 * http://handstandtech.com
 */
package com.handstandtech.restclient.server;

import java.io.IOException;
import java.util.Map;

import com.handstandtech.restclient.server.auth.Authenticator;
import com.handstandtech.restclient.server.model.RESTRequest;
import com.handstandtech.restclient.shared.model.RESTResult;
import com.handstandtech.restclient.shared.model.RequestMethod;

/**
 * Ability to make REST Calls
 * 
 * @author Sam Edwards
 * @version 2011.03.10
 */
public interface RESTClient {

	public abstract RESTResult request(RequestMethod method, String urlString);

	public abstract RESTResult request(RequestMethod method, String urlString,
			Authenticator auth);

	public abstract RESTResult requestWithBody(RequestMethod method,
			String urlString, Authenticator auth, byte[] payload);

	public abstract RESTResult requestWithBody(RequestMethod method,
			String urlString, byte[] payload);

	public abstract RESTResult request(RequestMethod get, String fullUrl,
			Map<String, String> headers);

	public abstract RESTResult request(RESTRequest request);

}