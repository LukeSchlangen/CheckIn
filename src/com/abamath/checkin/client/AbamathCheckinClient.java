package com.abamath.checkin.client;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.abamath.checkin.client.AbamathCheckinEntryPoint.Status;
import com.abamath.checkin.shared.User;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;

public class AbamathCheckinClient implements AbamathClient {

	private AbamathServiceAsync service;
	private AbamathCheckinEntryPoint entryPoint;

	private static final String[] colors = new String[]{"Yellow","Green","Red", "Blue"};

	private Panel checkinPanel;
	private HTMLPanel outPanel;
	private HTMLPanel inPanel;
	private Label outHeader;
	private Label inHeader;
	private Button returnHome;

	public AbamathCheckinClient(AbamathServiceAsync service, AbamathCheckinEntryPoint entryPoint) {
		this.service = service;
		this.entryPoint = entryPoint;
		setupPanelForRoot();
	}

	@Override
	public Panel getPanelForRoot() {
		History.newItem("home");
		return checkinPanel;
	}

	@Override
	public void setupPanelForRoot() {
		checkinPanel = new HorizontalPanel();
		outPanel = new HTMLPanel("");
		inPanel = new HTMLPanel("");

		outHeader = new Label("OUT");
		inHeader = new Label("IN");

		outPanel.add(outHeader);
		inPanel.add(inHeader);

		showButtons();
		addCss();

		checkinPanel.add(outPanel);

		
		returnHome = new Button("Home");
		returnHome.addStyleName("return-home-button");
		returnHome.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				entryPoint.setCheckinStatus(Status.HOME);
				
			}
			
		});
		
		
		checkinPanel.add(returnHome);
		checkinPanel.add(inPanel);
	}

	private void showButtons() {
		String adminUser = entryPoint.getAdminUser();
		service.getUsers(adminUser, new AsyncCallback<List<User>>() {
			@Override
			public void onFailure(Throwable caught) {
				//probably do some error handling here
				//
				//
				//...eventually
			}
			@Override
			public void onSuccess(List<User> result) {
				Collections.sort(result, new User());
				for(int i = 0; i < result.size(); i++) {
					User user = result.get(i);
					String displayTime =  stringTimeFormatter(user);
					Button button = new Button("<namelabel>" + user.getName() + "</namelabel><br/>" + displayTime );
					button.addClickHandler(new MyHandler(user));
					addButtonCss(button, user);

					if(user.getStatus().equals("Out")) {
						outPanel.add(button);
					}
					else {
						user.setStatus("Out");
						outPanel.add(button);
					}
				}				
			}			
		});
	}
	private String stringTimeFormatter(User user)
	{
		String result = "";
		int hours = ((Integer.parseInt(user.getTime())/60)) ;
		int minutes = ((Integer.parseInt(user.getTime())%60));
		String hoursString;
		String minutesString;
		if(hours<10)
		{
			hoursString = "0" +  hours ;
		}
		else
		{
			hoursString = hours + "";
		}
		if(minutes < 10)
		{
			minutesString = "0" + minutes;
		}
		else
		{
			minutesString = minutes +  "";
		}
		result = hoursString + " : " + minutesString + " : " +"00 " + "Hours" ;
		return result;
	}
	@Override
	public void addCss() {
		outPanel.addStyleName("panel");
		inPanel.addStyleName("panel");
		outPanel.addStyleName("outPanel");
		inPanel.addStyleName("inPanel");
		outHeader.addStyleName("panel-heading");
		inHeader.addStyleName("panel-heading");
		
	}

	public void addButtonCss(Button button, User user) {
		button.addStyleName("btn-default");
		button.addStyleName("col-xs-12");
		button.addStyleName("col-sm-6");
		button.addStyleName("col-md-4");
		button.addStyleName("col-lg-3");
		button.addStyleName("col-xl-2");


		if(Integer.parseInt(user.getTime())<1200){
			switch (user.getColor()) {
			case "Yellow":  button.addStyleName("yellow");
			break;
			case "Green":  button.addStyleName("green");
			break;
			case "Red":  button.addStyleName("red");
			break;
			case "Blue": button.addStyleName("blue");
			break;
			default:
				int random = (int)(Math.random() * 4);
				user.setColor(colors[random]);
				button.addStyleName(colors[random].toLowerCase());
				break;
			}
		}
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
				user.stopTimer();
			}
			else {
				clicked.removeFromParent();
				inPanel.add(clicked);
				user.setStatus("In");
				in = new Timestamp (new Date().getTime());
				user.getName();
				user.startTimer(clicked,user.getName());
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
