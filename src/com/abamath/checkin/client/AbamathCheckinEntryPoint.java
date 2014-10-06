package com.abamath.checkin.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AbamathCheckinEntryPoint implements EntryPoint {

	private final AbamathServiceAsync service = GWT.create(AbamathService.class);
	private AbamathCheckinClient checkinClient;
	private AbamathAuthenticationClient authClient;
	private AbamathAdminClient adminClient;
	
	private boolean isAuthenticated = false;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		//Set up the clients
		checkinClient = new AbamathCheckinClient(service);
		authClient = new AbamathAuthenticationClient(service, this);
		adminClient = new AbamathAdminClient(service);
		
		//Verify a browser cookie to see
		//if our user needs to authenticate
		isAuthenticated = getAuthenticationStatus();
		
		//History token logic
		if(History.getToken().equalsIgnoreCase("home") && isAuthenticated) {
			RootPanel.get().add(checkinClient.getPanelForRoot());
		}
		else if(History.getToken().equalsIgnoreCase("admin") && isAuthenticated) {
			RootPanel.get().add(adminClient.getPanelForRoot());
		}
		else {
			RootPanel.get().add(authClient.getPanelForRoot());
		}
		
	
		
	}
	
	private void clear() {
		RootPanel.get().clear();
	}
	
	public void setAuthenticationStatus(boolean status) {
		isAuthenticated = status;
		
		if(status == true) {
			clear();
			RootPanel.get().add(checkinClient.getPanelForRoot());
			History.newItem("home");
		}
	}
	
	public boolean getAuthenticationStatus() {

		return true;
	}
}
