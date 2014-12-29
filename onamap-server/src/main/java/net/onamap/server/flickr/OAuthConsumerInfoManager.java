package net.onamap.server.flickr;

import java.util.ArrayList;
import java.util.List;

public class OAuthConsumerInfoManager {

	private List<OAuthConsumerInfoSet> consumerInfos = new ArrayList<OAuthConsumerInfoSet>();

	public OAuthConsumerInfoManager() {

	}

	public static class OAuthConsumerInfoSet {

		private String urlPrefix;
		private String name;
		private String callbackUri;
		private String consumerKey;
		private String consumerSecret;

		public OAuthConsumerInfoSet() {

		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setUrlPrefix(String urlPrefix) {
			this.urlPrefix = urlPrefix;
		}

		public String getUrlPrefix() {
			return urlPrefix;
		}

		public void setCallbackUri(String callbackUri) {
			this.callbackUri = callbackUri;
		}

		public String getCallbackUri() {
			return callbackUri;
		}

		public void setConsumerKey(String consumerKey) {
			this.consumerKey = consumerKey;
		}

		public String getConsumerKey() {
			return consumerKey;
		}

		public void setConsumerSecret(String consumerSecret) {
			this.consumerSecret = consumerSecret;
		}

		public String getConsumerSecret() {
			return consumerSecret;
		}
	}

	public List<OAuthConsumerInfoSet> getConsumerInfos() {
		return consumerInfos;
	}

	public void addConsumerInfoSet(OAuthConsumerInfoSet _consumerInfoSet) {
		consumerInfos.add(_consumerInfoSet);
	}
}
