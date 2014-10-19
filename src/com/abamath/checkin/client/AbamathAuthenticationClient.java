package com.abamath.checkin.client;

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
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AbamathAuthenticationClient extends Composite implements AbamathClient {
	
	@UiTemplate("AbamathAuthenticationClient.ui.xml")
	interface AbamathAuthenticationClientUiBinder extends UiBinder<Widget, AbamathAuthenticationClient> {}
	
	AbamathAuthenticationClientUiBinder uiBinder = GWT.create(AbamathAuthenticationClientUiBinder.class);

	private AbamathServiceAsync service;
	private AbamathCheckinEntryPoint entryPoint;
	
	@UiField
	protected VerticalPanel authPanel;
	
	//create account elements
	@UiField
	protected VerticalPanel createPanel;
	@UiField
	protected TextBox createEmail;
	@UiField
	protected PasswordTextBox createPassword;
	@UiField
	protected Button createSubmit;
	
	//login elements
	@UiField
	protected VerticalPanel loginPanel;
	@UiField
	protected TextBox loginEmail;
	@UiField
	protected PasswordTextBox loginPassword;
	@UiField
	protected Button loginSubmit;
	
	public AbamathAuthenticationClient(AbamathServiceAsync service, AbamathCheckinEntryPoint entryPoint) {
		this.service = service;
		this.entryPoint = entryPoint;
		initWidget(uiBinder.createAndBindUi(this));
		setupPanelForRoot();
	}
	
	@Override
	public Panel getPanelForRoot() {		
		History.newItem("auth");
		return authPanel;
	}

	@Override
	public void setupPanelForRoot() {
		setupCreateBox();
		setupLoginBox();
		addCss();
	}
	
	private void setupCreateBox() {
		createSubmit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				service.createAccount(createEmail.getText(), createPassword.getText(), new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						//hooray for lack of error handling...
						//maybe implement a logging feature?
						createEmail.setText("ERROR");
					}

					@Override
					public void onSuccess(Boolean result) {
						if(result) {
							createEmail.setText("");
							createPassword.setText("");
						}
						else {
							createEmail.setText("BAD NAME");
							createPassword.setText("");
						}
					}					
				});			
			}			
		});
		
	}
	
	private void setupLoginBox() {
		loginSubmit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				service.authenticate(loginEmail.getText(), loginPassword.getText(), new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
					@Override
					public void onSuccess(Boolean result) {
						if(result) {
							//auth succeeded
							entryPoint.setAuthenticationStatus(true, loginEmail.getText());
							loginEmail.setText("");
							loginPassword.setText("");
						}
						else {
							//bad auth attempt
							loginEmail.setText("");
							loginPassword.setText("");
						}						
					}					
				});				
			}			
		});
	}
	
	@Override
	public void addCss() {
		//Add any css elements here
	}
}
