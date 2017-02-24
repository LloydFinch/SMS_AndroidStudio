package com.delta.smsandroidproject.bean;

//if the responseCode not equal 200, parse the response by the bean
public class NetResult {
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

}
