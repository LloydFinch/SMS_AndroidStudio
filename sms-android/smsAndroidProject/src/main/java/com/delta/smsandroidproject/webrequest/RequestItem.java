package com.delta.smsandroidproject.webrequest;

public class RequestItem {
	public String Id = "Id";
	public String NetowrkId = "NetowrkId";
	public String Image = "Image";
	public String Name = "Name";
	public String Company = "Company";
	public String Contact = "Contact";
	public String Address1 = "Address1";
	public String Address2 = "Address2";
	public String Zip = "Zip";
	public String City = "City";
	public String State = "State";
	public String Country = "Country";
	public String Phone = "Phone";
	public String Mobile = "Mobile";
	public String Mail = "Mail";
	public String Note = "Note";
	public String Lat = "Lat";
	public String Lon = "Lon";
	public String Capacity = "Capacity";
	private String mName;
	private String mValue;
	public RequestItem(String mName,String mValue){
		this.mName = mName;
		this.mValue = mValue;
		
	}
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public String getmValue() {
		return mValue;
	}
	public void setmValue(String mValue) {
		this.mValue = mValue;
	}
	@Override
	public String toString() {
		return "RequestItem [mName=" + mName + ", mValue=" + mValue + "]";
	}
	
	
	
	
	

}
