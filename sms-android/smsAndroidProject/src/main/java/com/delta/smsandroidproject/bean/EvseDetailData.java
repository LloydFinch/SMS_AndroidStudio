package com.delta.smsandroidproject.bean;

public class EvseDetailData {
	private String User;
	private String Errroe;
	private String Description;
	private String Error;
	private String Solution;
	private String Value;
	private String Key;
	private String OldValue;
	private String NewValue;
	private String Status;

	public String getUser() {
		return User;
	}

	public void setUser(String user) {
		User = user;
	}

	public String getErrroe() {
		return Errroe;
	}

	public void setErrroe(String errroe) {
		Errroe = errroe;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getError() {
		return Error;
	}

	public void setError(String error) {
		Error = error;
	}

	public String getSolution() {
		return Solution;
	}

	public void setSolution(String solution) {
		Solution = solution;
	}

	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}

	public String getKey() {
		return Key;
	}

	public void setKey(String key) {
		Key = key;
	}

	public String getOldValue() {
		return OldValue;
	}

	public void setOldValue(String oldValue) {
		OldValue = oldValue;
	}

	public String getNewValue() {
		return NewValue;
	}

	public void setNewValue(String newValue) {
		NewValue = newValue;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	@Override
	public String toString() {
		return "EvseDetailData [User=" + User + ", Errroe=" + Errroe
				+ ", Description=" + Description + ", Error=" + Error
				+ ", Solution=" + Solution + ", Value=" + Value + ", Key="
				+ Key + ", OldValue=" + OldValue + ", NewValue=" + NewValue
				+ ", Status=" + Status + "]";
	}

}
