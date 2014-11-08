package com.abamath.checkin.shared;

import java.io.Serializable;
import java.util.Comparator;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;

public class User implements Serializable, Comparator<User> {

	private static final long serialVersionUID = 1L;
	private String name;
	private String color;
	private String status;
	private String time;
	private boolean timerStatus;
	//private Timer timer;

	public User(){}

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getColor() {
		return color;
	}
	public void setStatus(String status) {
		this.status = status;
	}	
	public String getStatus() {
		return status;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTime() {
		return time;
	}
	public void startTimer(Button button, String n)
	{
		final String name = n;
		timerStatus = true;
		final Button b = button;
		Timer timer = new Timer() 
		{

			int seconds = 0; 
			int minutes = 0;
			int hours = 0;
			String timerTime;
			String minutesString;
			String secondsString;
			String hoursString;

			@Override
			public void run() {
				if (timerStatus)
				{
					if (seconds == 60)
					{
						minutes = minutes +1;
						seconds = 0;

					}
					else if (minutes == 60)
					{
						hours = hours +1;
						minutes = 0;

					}
					else if(hours == 24)
					{
						hours = 0;
					}
					hoursString = hours + "";
					minutesString = minutes +"";
					secondsString = seconds + "";
					if( hours <10)
					{
						hoursString = "0" + hours;
					}
					if(minutes <10)
					{
						minutesString = "0" + minutes;
					}
					if(seconds <10)
					{
						secondsString = "0" + seconds;
					}

					seconds++;

					timerTime = hoursString + " : " + minutesString + " : "+  secondsString + " Hours";
					b.setHTML("<namelabel>" + name + "</namelabel><br/>" + timerTime );
				}
				else
				{
					seconds = 0; 
					minutes = 0;
					hours = 0;
					b.setHTML ("<namelabel>" + name + "</namelabel><br/>" + ((Integer.parseInt(time)/60)) + " : " +(Integer.parseInt(time)%60) + " : " + "0"+ " Hours");
				}
			}
		};
		timer.scheduleRepeating(1000);
	}
	public void stopTimer()
	{
		timerStatus = false;
	}

	@Override
	public int compare(User userOne, User userTwo) {		
		return userOne.getName().compareTo(userTwo.getName());
	}
}
