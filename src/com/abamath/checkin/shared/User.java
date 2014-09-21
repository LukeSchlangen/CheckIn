package com.abamath.checkin.shared;

import java.io.Serializable;

public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String name;
	private String color;
	private String status;
	private String time;
	
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

}
