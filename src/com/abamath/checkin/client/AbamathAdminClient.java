package com.abamath.checkin.client;

import com.google.gwt.user.client.ui.Panel;

public class AbamathAdminClient implements AbamathClient {

	private AbamathServiceAsync service;
	
	public AbamathAdminClient(AbamathServiceAsync service) {
		this.service = service;
	}
	
	@Override
	public Panel getPanelForRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setupPanelForRoot() {
		// TODO Auto-generated method stub
		
	}

}
