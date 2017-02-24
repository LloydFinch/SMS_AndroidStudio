package com.delta.smsandroidproject.bean;

import java.io.Serializable;
import java.util.List;

import com.delta.smsandroidproject.bean.GoogleDirectionModel.Routes.Legs;

//use to show on the card
public class BestRouteModel implements Serializable {

	private static final long serialVersionUID = 2402233828639719727L;
	private long distance;
	private double fare;
	private String fareValue;
	private String visit;
	private List<String> placeID;
	private String copyrights;
	private String[] warnings;
	private long duration;
	private List<Legs> legs;

	public BestRouteModel() {

	}

	public long getDistance() {
		return distance;
	}

	public void setDistance(long distance) {
		this.distance = distance;
	}

	public double getFare() {
		return fare;
	}

	public void setFare(double fare) {
		this.fare = fare;
	}

	public String getFareValue() {
		return fareValue;
	}

	public void setFareValue(String fareValue) {
		this.fareValue = fareValue;
	}

	public String getVisit() {
		return visit;
	}

	public void setVisit(String visit) {
		this.visit = visit;
	}

	public List<String> getPlaceID() {
		return placeID;
	}

	public void setPlaceID(List<String> placeID) {
		this.placeID = placeID;
	}

	public String getCopyrights() {
		return copyrights;
	}

	public void setCopyrights(String copyrights) {
		this.copyrights = copyrights;
	}

	public String[] getWarnings() {
		return warnings;
	}

	public void setWarnings(String[] warnings) {
		this.warnings = warnings;
	}

	public List<Legs> getLegs() {
		return legs;
	}

	public void setLegs(List<Legs> legs) {
		this.legs = legs;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}
}
