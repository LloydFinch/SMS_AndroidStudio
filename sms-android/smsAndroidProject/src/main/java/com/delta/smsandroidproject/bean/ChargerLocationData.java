package com.delta.smsandroidproject.bean;

import java.io.Serializable;
import java.util.List;

/**
 * location list
 * 
 * @author Jianzao.Zhang
 * 
 */
public class ChargerLocationData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8064634144412459917L;
	
	/**
	 * status状态
	 */
	public static final int ERROR = -1;
	public static final int OK = 1;
	public static final int WARNING = 4;
	public static final int FAULT = 5;
	public static final int EMERGENCY = 6;
	
	private List<LocationList> Networks;

	public class LocationList {

	}

	private String Id;
	private String Name;
	private double Lat;
	private double Lon;
	private int Status;
	private int EvseTotal;
	private int EvseUsing;
	private String Image;
	private String ChargerTotal;
	private double distance;// 自定义

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		this.Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		this.Name = name;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		this.Status = status;
	}

	public int getEvseTotal() {
		return EvseTotal;
	}

	public void setEvseTotal(int evseTotal) {
		this.EvseTotal = evseTotal;
	}

	public int getEvseUsing() {
		return EvseUsing;
	}

	public void setEvseUsing(int evseUsing) {
		this.EvseUsing = evseUsing;
	}

	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		Image = image;
	}

	public List<LocationList> getNetworks() {
		return Networks;
	}

	public void setNetworks(List<LocationList> networks) {
		Networks = networks;
	}

	public double getLat() {
		return Lat;
	}

	public void setLat(double lat) {
		Lat = lat;
	}

	public double getLon() {
		return Lon;
	}

	public void setLon(double lon) {
		Lon = lon;
	}

	public String getChargerTotal() {
		return ChargerTotal;
	}

	public void setChargerTotal(String chargerTotal) {
		ChargerTotal = chargerTotal;
	}

	@Override
	public String toString() {
		return "ChargerLocationData [Networks=" + Networks + ", Id=" + Id
				+ ", Name=" + Name + ", Lat=" + Lat + ", Lon=" + Lon
				+ ", Status=" + Status + ", EvseTotal=" + EvseTotal
				+ ", EvseUsing=" + EvseUsing + ", Image=" + Image
				+ ", ChargerTotal=" + ChargerTotal + ", distance=" + distance
				+ "]";
	}

	
}
