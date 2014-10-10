package com.abamath.checkin.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AbamathAuthenticationClient implements AbamathClient {

	private AbamathServiceAsync service;
	private AbamathCheckinEntryPoint entryPoint;
	
	//create account elements
	private Panel authPanel;
	private Label createText;
	private TextBox createEmail;
	private PasswordTextBox createPassword;
	private Button createSubmit;
	
	private Button engageLogin;
	
	//login elements
	private DialogBox loginBox;
	private Panel loginPanel;
	private TextBox loginEmail;
	private PasswordTextBox loginPassword;
	private Button loginSubmit;
	private Button loginCancel;
	
	private static final String CREATE_LABEL_TEXT = "Create an account";
	
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
		setupLoginBox();
		loginBox.hide();
		createText = new Label();
		createText.setText(CREATE_LABEL_TEXT);
		createEmail = new TextBox();
		createPassword = new PasswordTextBox();
		createSubmit = new Button("Submit");
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
		
		engageLogin = new Button("Login");
		engageLogin.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				loginBox.show();				
			}			
		});
		
		authPanel.add(createText);
		authPanel.add(createEmail);
		authPanel.add(createPassword);
		authPanel.add(createSubmit);
		authPanel.add(engageLogin);
	}
	
	private void setupLoginBox() {
		loginBox = new DialogBox();
		loginPanel = new VerticalPanel();
		loginEmail = new TextBox();
		loginPassword = new PasswordTextBox();
		loginSubmit = new Button("Login");
		loginCancel = new Button("Cancel");
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
							entryPoint.setAuthenticationStatus(true);
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
		loginCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				loginEmail.setText("");
				loginPassword.setText("");
				loginBox.hide();
			}
			
		});
			
		loginPanel.add(loginEmail);
		loginPanel.add(loginPassword);
		loginPanel.add(loginSubmit);
		loginPanel.add(loginCancel);
		loginBox.add(loginPanel);
		loginBox.center();
	}
}
