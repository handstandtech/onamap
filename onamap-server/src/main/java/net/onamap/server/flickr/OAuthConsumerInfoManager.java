package net.onamap.server.flickr;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class OAuthConsumerInfoManager {

	private List<OAuthConsumerInfoSet> consumerInfos = new ArrayList<OAuthConsumerInfoSet>();

    @Data
    @NoArgsConstructor
	public static class OAuthConsumerInfoSet {

		private String urlPrefix;
		private String name;
		private String callbackUri;
		private String consumerKey;
		private String consumerSecret;
	}

}
