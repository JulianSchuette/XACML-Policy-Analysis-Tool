package org.linkality.xacmlanalysr.ui.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The controller's interface that is used at the client (JS) side for making
 * asynchronous requests to it.
 * 
 * @author root
 * 
 */
public interface ServerControllerAsync {
	public void handleRequest(String s, AsyncCallback callback);
}