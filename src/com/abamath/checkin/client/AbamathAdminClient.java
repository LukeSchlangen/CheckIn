package com.abamath.checkin.client;

import com.abamath.checkin.client.AbamathCheckinEntryPoint.Status;
import com.abamath.checkin.client.AbamathHomeClient.AbamathHomeClientUiBinder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AbamathAdminClient extends Composite implements AbamathClient {
	
	@UiTemplate("AbamathAdminClient.ui.xml")
	interface AbamathAdminClientUiBinder extends UiBinder<Widget, AbamathAdminClient> {}
	
	AbamathAdminClientUiBinder uiBinder = GWT.create(AbamathAdminClientUiBinder.class);

	private AbamathServiceAsync service;
	private AbamathCheckinEntryPoint entryPoint;
	
	@UiField
	protected VerticalPanel adminPanel;
	@UiField
	protected Button homeButton;
	@UiField
	protected TextBox nameField;
	@UiField
	protected ListBox colorField;
	@UiField
	protected Button addUserButton;
	
	public AbamathAdminClient(AbamathServiceAsync service, AbamathCheckinEntryPoint entryPoint) {
		this.service = service;
		this.entryPoint = entryPoint;
		initWidget(uiBinder.createAndBindUi(this));
		setupPanelForRoot();
	}
	
	@Override
	public Panel getPanelForRoot() {
		History.newItem("admin");
		return adminPanel;
	}

	@Override
	public void setupPanelForRoot() {
		homeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				entryPoint.setAdminStatus(Status.HOME);				
			}		
		});
		addUserButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String username = nameField.getText();
				String color = colorField.getItemText(colorField.getSelectedIndex());
				
				if (!username.isEmpty()) {
					service.addUser(username, color, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							// ERROR ERROR ERROR..........
							// log it!
						}

						@Override
						public void onSuccess(Void result) {
							nameField.setText("");
						}
					});
				}
			}
		});
	}

	@Override
	public void addCss() {
		// TODO Auto-generated method stub
		
	}

}
