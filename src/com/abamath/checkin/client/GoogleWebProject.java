package com.abamath.checkin.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.abamath.checkin.shared.User;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GoogleWebProject implements EntryPoint {

	private final Map<String, String> userClicked = new HashMap<String, String>();
	
	private final GreetingServiceAsync service = GWT.create(GreetingService.class);
	private Panel outPanel = new VerticalPanel();
	private Panel inPanel = new VerticalPanel();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		showButtons();

	}
	
	private void showButtons() {
		service.getUsers(new AsyncCallback<List<User>>() {
			@Override
			public void onFailure(Throwable caught) {
				//probably do some error handling here
				//
				//
				//...eventually			
			}
			@Override
			public void onSuccess(List<User> result) {
				for(int i = 0; i < result.size(); i++) {
					Button button = new Button();
					if(result.get(i).getStatus().equals("in")) {
						inPanel.add(button.asWidget());
					}
					else {
						outPanel.add(button.asWidget());
					}
					
					button.addClickHandler(new MyHandler());
				}				
			}			
		});
	}
	
	private class MyHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			sendClickToServer();
		}	

		private void sendClickToServer() {
			service.greetServer(userClicked, new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					//probably do some error handling here
					//
					//...again...
					//
					//...eventually			
				}
				@Override
				public void onSuccess(Void result) {
					showButtons();									
				}
			});
		}
	}
}
