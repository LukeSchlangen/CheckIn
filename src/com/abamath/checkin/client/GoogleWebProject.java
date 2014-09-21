package com.abamath.checkin.client;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.abamath.checkin.shared.User;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
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
		outPanel = new HTMLPanel("");
		inPanel = new HTMLPanel("");
		
		outPanel.add(new Label("OUT"));
		inPanel.add(new Label("IN"));
		
		showButtons();
	    // Create a Label and an HTML widget.
	    Label lbl = new Label("This is just text.  It will not be interpreted "
	      + "as <html>.");

	    HTML html = new HTML(
	      "This is <b>HTML</b>.  It will be interpreted as such if you specify "
	        + "the <span style='font-family:fixed'>asHTML</span> flag.", true);

	    // Add them to the root panel.
	    VerticalPanel panel = new VerticalPanel();
	    panel.add(lbl);
	    panel.add(html);
	    RootPanel.get().add(panel);
		HorizontalPanel hp = new HorizontalPanel();
		HTML html2 = new HTML("<p>This is html with a <a href='www.google.com'>link</a></p>");
		hp.add(html2); // adds the widget to the panel
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
				int inPanelButtonCount = 0;
				int outPanelButtonCount = 0;
				for(int i = 0; i < result.size(); i++) {
					User user = result.get(i);
					
					Button button = new Button(user.getName());
					button.addClickHandler(new MyHandler(user));
					button.setSize("200px", "100px");
					
					if(user.getStatus().equals("In")) {
						inPanel.add(button);
						inPanelButtonCount++;
					}
					else {
						outPanel.add(button);
						outPanelButtonCount++;
					}
				}	

				String inPanelWidth = Math.max(inPanelButtonCount * 50, 200) + "px";
				String outPanelWidth = Math.max(outPanelButtonCount * 50, 200) + "px";
				inPanel.setWidth(inPanelWidth);
				outPanel.setWidth(outPanelWidth);
				
			}			
		});
	}
	
	private class MyHandler implements ClickHandler {
		private User user;
		private Timestamp in;
		private Timestamp out;
		
		public MyHandler(User user) {
			this.user = user;
		}
		
		public void onClick(ClickEvent event) {
			Button clicked = (Button) event.getSource();

			if(clicked.getParent().equals(inPanel)) {
				clicked.removeFromParent();
				outPanel.add(clicked);
				user.setStatus("Out");
				out = new Timestamp(new Date().getTime());
				user.setTime(timeDiff(in, out));
			}
			else {
				clicked.removeFromParent();
				inPanel.add(clicked);
				user.setStatus("In");
				in = new Timestamp (new Date().getTime());
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
		
		private String timeDiff(Timestamp in, Timestamp out) {
			long diff = out.getTime() - in.getTime();
			long currentTime = Long.parseLong(user.getTime()) * 60000;
			return String.valueOf((diff + currentTime) / 60000);
		}
	}
}
