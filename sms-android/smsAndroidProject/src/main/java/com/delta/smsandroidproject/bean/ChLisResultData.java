package com.delta.smsandroidproject.bean;

import java.util.ArrayList;

public class ChLisResultData {
	private ArrayList<ChargerInfoData> Result;
	private String Total;
	public ArrayList<ChargerInfoData> getResult() {
		return Result;
	}
	public void setResult(ArrayList<ChargerInfoData> result) {
		Result = result;
	}
	public String getTotal() {
		return Total;
	}
	public void setTotal(String total) {
		Total = total;
	}
	@Override
	public String toString() {
		return "Result [Result=" + Result + ", Total=" + Total + "]";
	}
	

}
