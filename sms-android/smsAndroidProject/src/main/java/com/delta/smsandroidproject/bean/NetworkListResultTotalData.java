package com.delta.smsandroidproject.bean;

import java.util.List;


public class NetworkListResultTotalData {
	private List<NetworkData> Result;
	private int Total;
	public List<NetworkData> getResults() {
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
