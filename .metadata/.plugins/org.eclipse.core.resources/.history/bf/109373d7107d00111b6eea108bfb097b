/**
 * Handstand Technologies, LLC
 * http://handstandtech.com
 */
package com.handstandtech.restclient.server.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.handstandtech.restclient.server.auth.Authenticator;
import com.handstandtech.restclient.shared.model.RESTResult;
import com.handstandtech.restclient.shared.model.RequestAuthentication;
import com.handstandtech.restclient.shared.model.RequestMethod;

/**
 * Uses App Engine's URL Fetch Classes
 * 
 * @author Sam Edwards
 * @version 2011.03.09
 */
public class RESTClientAppEngineURLFetchImpl extends RESTClientBaseImpl {

	public RESTClientAppEngineURLFetchImpl() {
		super();
	}

	protected RESTResult internalDoRequest(RequestMethod method, String urlString, Authenticator auth, byte[] payload, Map<String, String> headers) throws IOException {

		List<HTTPHeader> requestHeaders = new ArrayList<HTTPHeader>();
		if (headers != null) {
			for (String key : headers.keySet()) {
				String value = headers.get(key);
				requestHeaders.add(new HTTPHeader(key, value));
			}
		}

		RESTResult result = new RESTResult();
		URLFetchService fetchService = URLFetchServiceFactory.getURLFetchService();

		URL url;
		try {
			result.setRequestUrl(urlString);
			url = new URL(urlString);

			FetchOptions fetchOptions = FetchOptions.Builder.doNotValidateCertificate();
			// 20 second deadline
			fetchOptions.setDeadline(1000 * 25.0);
			HTTPRequest httpRequest = new HTTPRequest(url, HTTPMethod.valueOf(method.name()), fetchOptions);

			result.setRequestMethod(method);

			for (HTTPHeader header : requestHeaders) {
				httpRequest.addHeader(header);
			}

			if (payload != null) {
				httpRequest.setPayload(payload);
			}
			// Authenticate if required
			if (auth != null) {
				result.setRequestAuthentication(auth.getRequestAuthenticationType());
			} else {
				result.setRequestAuthentication(RequestAuthentication.NONE);
			}

			Future<HTTPResponse> future = fetchService.fetchAsync(httpRequest);

			// Other Stuff Can Happen Here!!!
			// log.info("Do stuff here while the calls are happening, we'll process the result when we are done here.  If the fetch isn't done yet, we'll wait for it to finish.");

			try {
				HTTPResponse response = future.get();
				URL finalUrl = response.getFinalUrl();
				if (finalUrl != null) {
					// if redirects are followed, this returns the final URL we
					// are
					// redirected to
					result.setFinalUrl(finalUrl.toString());
				}

				byte[] content = response.getContent();
				result.setResponseLength(content.length);
				InputStream byis = new ByteArrayInputStream(content);
				InputStreamReader inputStreamReader = new InputStreamReader(byis, "UTF-8");
				BufferedReader reader = new BufferedReader(inputStreamReader);
				String line = null;
				StringBuffer responseBody = new StringBuffer();
				while ((line = reader.readLine()) != null) {
					responseBody.append(line + "\n");
				}

				result.setResponseBody(responseBody.toString());

				// 200, 404, 500, etc
				int responseCode = response.getResponseCode();
				result.setResponseCode(responseCode);

				List<HTTPHeader> responseHeaders = response.getHeaders();
				for (HTTPHeader header : responseHeaders) {
					String headerName = header.getName();
					String headerValue = header.getValue();
					result.addResponseHeader(headerName, headerValue);
				}
			} catch (InterruptedException e) {

				e.printStackTrace();
			} catch (ExecutionException e) {

				e.printStackTrace();
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		return result;
	}
}
