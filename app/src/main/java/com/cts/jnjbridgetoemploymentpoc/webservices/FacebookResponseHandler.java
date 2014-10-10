package com.cts.jnjbridgetoemploymentpoc.webservices;

/**
 * Interface to handle Facebook response for both httpGet and httpPost requests
 * @author neerajareddy
 *
 */
public interface FacebookResponseHandler {

	public void handleResponse(Object response);
}
