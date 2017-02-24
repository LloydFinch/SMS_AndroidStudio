package com.delta.smsandroidproject.bean;

import java.io.Serializable;

public class NearByChargingLocData implements Serializable{
	private String location;
	private String distance;
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	
}
