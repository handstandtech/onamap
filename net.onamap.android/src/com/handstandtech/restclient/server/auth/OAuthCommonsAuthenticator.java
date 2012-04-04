package com.handstandtech.restclient.server.auth;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.methods.HttpUriRequest;

import com.handstandtech.restclient.server.auth.Authenticator;
import com.handstandtech.restclient.shared.model.RequestAuthentication;

public abstract class OAuthCommonsAuthenticator implements Authenticator {

	private OAuthConsumer consumer;

	public OAuthCommonsAuthenticator(OAuthConsumer consumer) {
		this.setConsumer(consumer);
	}

	public void setConsumer(OAuthConsumer consumer) {
		this.consumer = consumer;
	}

	public OAuthConsumer getConsumer() {
		return consumer;
	}

	@Override
	public void authenticate(Object connectionObject) {
		if (connectionObject instanceof HttpUriRequest) {
			HttpUriRequest connection = (HttpUriRequest) connectionObject;
			if (consumer != null) {
				try {
					consumer.sign(connection);
				} catch (OAuthMessageSignerException e) {

					e.printStackTrace();
				} catch (OAuthExpectationFailedException e) {

					e.printStackTrace();
				} catch (OAuthCommunicationException e) {

					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public RequestAuthentication getRequestAuthenticationType() {
		return RequestAuthentication.OAUTH;
	}

}
