package com.abamath.checkin.client;

import com.abamath.checkin.client.AbamathCheckinEntryPoint.Status;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AbamathHomeClient extends Composite implements AbamathClient {

	@UiTemplate("AbamathHomeClient.ui.xml")
	interface AbamathHomeClientUiBinder extends UiBinder<Widget, AbamathHomeClient> {}
	
	AbamathHomeClientUiBinder uiBinder = GWT.create(AbamathHomeClientUiBinder.class);
	
	private AbamathServiceAsync service;
	private AbamathCheckinEntryPoint entryPoint;
	
	@UiField
	protected VerticalPanel homePanel;
	@UiField
	protected Button myCheckIn;
	@UiField
	protected Button adminButton;
	@UiField
	protected Button logoutButton;
	
	public AbamathHomeClient(AbamathServiceAsync service, AbamathCheckinEntryPoint entryPoint) {
		this.service = service;
		this.entryPoint = entryPoint;
		initWidget(uiBinder.createAndBindUi(this));
		setupPanelForRoot();
	}
	
	@Override
	public Panel getPanelForRoot() {
		History.newItem("home");
		return homePanel;
	}

	@Override
	public void setupPanelForRoot() {
		myCheckIn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				entryPoint.setHomeStatus(Status.CHECKIN);				
			}		
		});
		logoutButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				entryPoint.setHomeStatus(Status.AUTH);				
			}			
		});
		adminButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				entryPoint.setHomeStatus(Status.ADMIN);
			}		
		});
	}

	@Override
	public void addCss() {
		// TODO Auto-generated method stub
		
	}

}
