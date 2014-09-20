package com.abamath.checkin.client;

import java.util.List;

import com.abamath.checkin.shared.User;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GoogleWebProject implements EntryPoint {

	private final GreetingServiceAsync service = GWT.create(GreetingService.class);
	private Panel addPanel;
	private Panel outPanel;
	private Panel inPanel;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		addPanel = new HorizontalPanel();
		outPanel = new VerticalPanel();
		inPanel = new VerticalPanel();
		
		outPanel.add(new Label("OUT"));
		inPanel.add(new Label("IN"));
		
		showButtons();
		
		addPanel.add(outPanel);
		addPanel.add(inPanel);
		RootPanel.get().add(addPanel);
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
					User user = result.get(i);
					
					Button button = new Button(user.getName());
					button.addClickHandler(new MyHandler(user));
					button.setSize("200px", "100px");
					
					if(user.getStatus().equals("In")) {
						inPanel.add(button);
					}
					else {
						outPanel.add(button);
					}
				}				
			}			
		});
	}
	
	private class MyHandler implements ClickHandler {
		private User user;
		
		public MyHandler(User user) {
			this.user = user;
		}
		
		public void onClick(ClickEvent event) {
			Button clicked = (Button) event.getSource();

			if(clicked.getParent().equals(inPanel)) {
				clicked.removeFromParent();
				outPanel.add(clicked);
				user.setStatus("Out");
			}
			else {
				clicked.removeFromParent();
				inPanel.add(clicked);
				user.setStatus("In");
			}
				
			sendClickToServer(user);
		}	

		private void sendClickToServer(User user) {
			service.buttonClick(user, new AsyncCallback<Void>() {
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
					//do nothing									
				}
			});
		}
	}
}
