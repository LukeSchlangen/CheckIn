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
	private HTMLPanel outPanel;
	private HTMLPanel inPanel;

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

	    HTML html = new HTML("Abamath Check In System", true);

	    // Add them to the root panel.
	    VerticalPanel headPanel = new VerticalPanel();
	    headPanel.add(html);
	    RootPanel.get().add(headPanel);
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
					
					Button button = new Button("<nameLabel>" + user.getName() + "</nameLabel><br/>" + user.getTime() + " Hours");
					button.addClickHandler(new MyHandler(user));
					button.setSize("200px", "100px");
					
					//button styling
					switch (user.getColor()) {
		            case "Yellow":  button.addStyleName("yellow");;
		                     break;
		            case "Green":  button.addStyleName("green");;
		                     break;
		            case "Red":  button.addStyleName("red");;
		                     break;
		            default: button.addStyleName("blue");;
		                     break;
		        }
					
					if(user.getStatus().equals("In")) {
						inPanel.add(button);
						//inPanelButtonCount++;
					}
					else {
						outPanel.add(button);
						//outPanelButtonCount++;
					}
				}
				
				inPanel.addStyleName("htmlPanel");
				outPanel.addStyleName("htmlPanel");
				inPanel.addStyleName("inPanel");
				outPanel.addStyleName("outPanel");
				
				inPanel = ResizePanel(inPanel);
				outPanel = ResizePanel(outPanel);

				
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
				//user.setTime(timeDiff(in, out));
			}
			else {
				clicked.removeFromParent();
				inPanel.add(clicked);
				user.setStatus("In");
				in = new Timestamp (new Date().getTime());
			}
			
			inPanel = ResizePanel(inPanel);
			outPanel = ResizePanel(outPanel);
						
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
	
	public HTMLPanel ResizePanel(HTMLPanel panel){
		int panelButtonCount = panel.getWidgetCount();
		String panelWidth = Math.max((panelButtonCount * 50)/200 * 200, 200) + "px";
		panel.setWidth(panelWidth);
		return panel;
	}
}
