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
	
	protected enum Status {
		HOME,
		CHECKIN,
		AUTH,
		ADMIN
	}
	

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		//Set up the clients
		checkinClient = new AbamathCheckinClient(service, this);
		authClient = new AbamathAuthenticationClient(service, this);
		adminClient = new AbamathAdminClient(service, this);
		homeClient = new AbamathHomeClient(service, this);
		
		//History token logic
		if(History.getToken().equalsIgnoreCase("home")) {
			RootPanel.get().add(checkinClient.getPanelForRoot());
		}
		else if(History.getToken().equalsIgnoreCase("admin")) {
			RootPanel.get().add(adminClient.getPanelForRoot());
		}
		else {
			RootPanel.get().add(authClient.getPanelForRoot());
		}
	}
	
	private void clear() {
		RootPanel.get().clear();
	}
	
	protected void setAuthenticationStatus(Status status) {
		clear();
		if(status == Status.HOME) {
			RootPanel.get().add(homeClient.getPanelForRoot());
			History.newItem("home");
		}
	}
	
	protected void setHomeStatus(Status status) {
		clear();
		if(status == Status.CHECKIN) {
			RootPanel.get().add(checkinClient.getPanelForRoot());
			History.newItem("checkin");
		}
		else if(status == Status.AUTH) {
			RootPanel.get().add(authClient.getPanelForRoot());
			History.newItem("auth");
		}
		else if(status == Status.ADMIN) {
			RootPanel.get().add(adminClient.getPanelForRoot());
			History.newItem("admin");
		}
		else {
			//log some sort of error message,
			//as this case shouldn't happen unless
			//something breaks
		}
	}
	
	protected void setCheckinStatus(Status status) {
		clear();
		if(status == Status.HOME) {
			RootPanel.get().add(homeClient.getPanelForRoot());
		}
	}
	
	protected void setAdminUser(String adminUser) {
		this.adminUser = adminUser;
	}
	protected String getAdminUser() {
		return adminUser;
	}
}
