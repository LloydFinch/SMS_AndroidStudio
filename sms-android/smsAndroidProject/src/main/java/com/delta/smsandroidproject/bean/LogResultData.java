package com.delta.smsandroidproject.bean;

public class LogResultData {
	private String Result;
	private String Message;
	public String getResult() {
		return Result;
	}
	public void setResult(String result) {
		Result = result;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	@Override
	public String toString() {
		return "LogResultData [Result=" + Result + ", Message=" + Message + "]";
	}
	
	
}
