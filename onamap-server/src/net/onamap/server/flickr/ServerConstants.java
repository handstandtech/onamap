package net.onamap.server.flickr;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.handstandtech.restclient.server.auth.oauth.OAuthConsumerInfoManager;
import com.handstandtech.restclient.server.auth.oauth.OAuthConsumerInfoManager.OAuthConsumerInfoSet;

public abstract class ServerConstants {

	protected OAuthConsumerInfoManager oauthManager = new OAuthConsumerInfoManager();

	protected static Logger log = LoggerFactory.getLogger(ServerConstants.class);

	public String getClientId(String requestUrl) {
		for (OAuthConsumerInfoSet consumerInfo : oauthManager.getConsumerInfos()) {
			if (requestUrl.contains(consumerInfo.getUrlPrefix())) {
				return consumerInfo.getConsumerKey();
			}
		}

		log.error("Could not find an appropriate consumer key info: " + requestUrl);
		return null;
	}

	public String getCallbackUri(HttpServletRequest request) {
		String requestUrl = request.getRequestURL().toString();
		for (OAuthConsumerInfoSet consumerInfo : oauthManager.getConsumerInfos()) {
			if (requestUrl.contains(consumerInfo.getUrlPrefix())) {
				return consumerInfo.getCallbackUri();
			}
		}

		log.error("Could not find an appropriate consumer secret info: " + requestUrl);
		return null;
	}

	public String getClientSecret(String requestUrl) {
		for (OAuthConsumerInfoSet consumerInfo : oauthManager.getConsumerInfos()) {
			if (requestUrl.contains(consumerInfo.getUrlPrefix())) {
				return consumerInfo.getConsumerSecret();
			}
		}

		log.error("Could not find an appropriate consumer secret info: " + requestUrl);
		return null;
	}
}
