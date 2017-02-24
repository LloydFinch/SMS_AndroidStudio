package com.delta.smsandroidproject.bean;

import java.util.List;

public class LocResultTotalData {
	private List<ChargerLocationData> Result;
	private int Total;
	public List<ChargerLocationData> getResults() {
		return Result;
	}
	public int getTotal() {
		return Total;
	}
	@Override
	public String toString() {
		return "NetworkData [Result=" + Result + ", Total=" + Total + "]";
	}
}
