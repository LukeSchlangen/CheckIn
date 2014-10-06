package com.abamath.checkin.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AbamathAuthenticationClient implements AbamathClient {

	private AbamathServiceAsync service;
	private AbamathCheckinEntryPoint entryPoint;
	
	private Panel authPanel;
	private TextBox username;
	private PasswordTextBox password;
	private Button submit;
	
	public AbamathAuthenticationClient(AbamathServiceAsync service, AbamathCheckinEntryPoint entryPoint) {
		this.service = service;
		this.entryPoint = entryPoint;
		setupPanelForRoot();
	}
	
	@Override
	public Panel getPanelForRoot() {		
		History.newItem("auth");
		return authPanel;
	}

	@Override
	public void setupPanelForRoot() {
		authPanel = new VerticalPanel();
		username = new TextBox();
		password = new PasswordTextBox();
		submit = new Button("Submit");
		submit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				service.authenticate(username.getText(), password.getText(), new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						//hooray for lack of error handling...
						//maybe implement a logging feature?						
					}

					@Override
					public void onSuccess(Boolean result) {
						if(result) {
							entryPoint.setAuthenticationStatus(true);
						}
						else {
							username.setText("");
							password.setText("");
						}
					}
					
				});			
			}			
		});
		
		authPanel.add(username);
		authPanel.add(password);
		authPanel.add(submit);
	}
	
	

}
