package org.linkality.xacmlanalysr.ui.client;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * The controller's interface against which the server controller is
 * implemented.
 * 
 * @author root
 * 
 */
public interface ServerController extends RemoteService {
	String handleRequest(String req);
}
