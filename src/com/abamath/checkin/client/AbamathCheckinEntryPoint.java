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
	private AbamathHomeClient homeClient;
	
	private String adminUser;
	
	private boolean isAuthenticated = false;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		//Set up the clients
		checkinClient = new AbamathCheckinClient(service, this);
		authClient = new AbamathAuthenticationClient(service, this);
		adminClient = new AbamathAdminClient(service, this);
		homeClient = new AbamathHomeClient(service, this);
		
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
	
	protected void setAuthenticationStatus(boolean isAuthenticated, String adminUser) {
		this.isAuthenticated = isAuthenticated;
		this.adminUser = adminUser;
		
		if(isAuthenticated == true) {
			clear();
			RootPanel.get().add(homeClient.getPanelForRoot());
			History.newItem("home");
		}
	}
	
	protected boolean getAuthenticationStatus() {

		return true;
	}
	
	protected void setHomeStatus(String status) {
		clear();
		if(status.equals("CHECKIN_SYSTEM")) {
			RootPanel.get().add(checkinClient.getPanelForRoot());
		}
		else if(status.equals("AUTHENTICATION")) {
			RootPanel.get().add(authClient.getPanelForRoot());
		}		
	}
	
	protected String getAdminUser() {
		return adminUser;
	}
}
