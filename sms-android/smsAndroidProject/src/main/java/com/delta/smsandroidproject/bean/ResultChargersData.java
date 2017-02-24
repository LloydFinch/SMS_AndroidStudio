package com.delta.smsandroidproject.bean;

import java.util.List;

public class ResultChargersData {
	private int Total;
	private List<EventListData> Chargers;
	public int getTotal() {
		return Total;
	}
	public List<EventListData> getChargers() {
		return Chargers;
	}
	@Override
	public String toString() {
		return "ResultChargersData [Total=" + Total + ", Chargers=" + Chargers
				+ "]";
	}
	
}
