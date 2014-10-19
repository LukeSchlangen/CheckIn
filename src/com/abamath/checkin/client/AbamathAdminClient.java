package com.abamath.checkin.client;

import com.google.gwt.user.client.ui.Panel;

public class AbamathAdminClient implements AbamathClient {

	private AbamathServiceAsync service;
	private AbamathCheckinEntryPoint entryPoint;
	
	public AbamathAdminClient(AbamathServiceAsync service, AbamathCheckinEntryPoint entryPoint) {
		this.service = service;
		this.entryPoint = entryPoint;
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

	@Override
	public void addCss() {
		// TODO Auto-generated method stub
		
	}

}
