package net.onamap.server.flickr;//package net.onamap.server.flickr;

import com.handstandtech.restclient.server.auth.Authenticator;
import com.handstandtech.restclient.server.auth.oauth.JavaNetOAuthAuthenticator;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlickrAPIOAuth {

    protected static Logger log = LoggerFactory.getLogger(FlickrAPIOAuth.class
            .getName());

    private static final String FORMAT = "json";

    private static final String TWITTER_RATE_LIMIT_HEADER_KEY = "x-ratelimit-limit";
    private static final String TWITTER_RATE_LIMIT_REMAINING_HEADER_KEY = "x-ratelimit-remaining";
    private static final String TWITTER_RATE_LIMIT_RESET_HEADER_KEY = "x-ratelimit-reset";

    protected OAuthConsumer consumer;

    public FlickrAPIOAuth(OAuthConsumer consumer) {
        this.consumer = consumer;
    }

    public FlickrAPIOAuth(String requestUrl, String token, String tokenSecret) {
        this.consumer = FlickrServerConstants.INSTANCE.getOAuthConsumer(requestUrl);
        this.consumer.setTokenWithSecret(token, tokenSecret);
    }

    public FlickrAPIOAuth(String consumerKey, String consumerSecret,
                          String token, String tokenSecret) {
        this.consumer = new DefaultOAuthConsumer(consumerKey, consumerSecret);
        this.consumer.setTokenWithSecret(token, tokenSecret);
    }

    private Authenticator getAuthenticator() {
        return new JavaNetOAuthAuthenticator(consumer);
    }

}
