package com.delta.smsandroidproject.bean;

public class LocationInfoData {
	private  String Name;//location name
	private String Image;//image_path
	private String Lat;//lication_lat
	private String Lon;//location_lon
	private String Capacity;//grid_capacity
	private String BuildDate;//commissioning_data;
	private String Status;//location_status
	private String Contact;//contact_name
	private String Company;//company_name
	private String Address1;//location_addr1
	private String Address2;//location_addr2
	private String Zip;//zip_code
	private String City;//city_name
	private String State;//state_name
	private String Country;//country_name
	private String Phone;//phone_number
	private String Mobile;//mobile_number mobile
	private String Mail;//email
	private String Note;//note
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getImage() {
		return Image;
	}
	public void setImage(String image) {
		Image = image;
	}
	public String getLat() {
		return Lat;
	}
	public void setLat(String lat) {
		Lat = lat;
	}
	public String getLon() {
		return Lon;
	}
	public void setLon(String lon) {
		Lon = lon;
	}
	public String getCapacity() {
		return Capacity;
	}
	public void setCapacity(String capacity) {
		Capacity = capacity;
	}
	public String getBuildDate() {
		return BuildDate;
	}
	public void setBuildDate(String buildDate) {
		BuildDate = buildDate;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getContact() {
		return Contact;
	}
	public void setContact(String contact) {
		Contact = contact;
	}
	public String getCompany() {
		return Company;
	}
	public void setCompany(String company) {
		Company = company;
	}
	public String getAddress1() {
		return Address1;
	}
	public void setAddress1(String address1) {
		Address1 = address1;
	}
	public String getAddress2() {
		return Address2;
	}
	public void setAddress2(String address2) {
		Address2 = address2;
	}
	public String getZip() {
		return Zip;
	}
	public void setZip(String zip) {
		Zip = zip;
	}
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public String getCountry() {
		return Country;
	}
	public void setCountry(String country) {
		Country = country;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public String getMobile() {
		return Mobile;
	}
	public void setMobile(String mobile) {
		Mobile = mobile;
	}
	public String getMail() {
		return Mail;
	}
	public void setMail(String mail) {
		Mail = mail;
	}
	public String getNote() {
		return Note;
	}
	public void setNote(String note) {
		Note = note;
	}
	@Override
	public String toString() {
		return "LocationInfoData [Name=" + Name + ", Image=" + Image + ", Lat="
				+ Lat + ", Lon=" + Lon + ", Capacity=" + Capacity
				+ ", BuildDate=" + BuildDate + ", Status=" + Status
				+ ", Contact=" + Contact + ", Company=" + Company
				+ ", Address1=" + Address1 + ", Address2=" + Address2
				+ ", Zip=" + Zip + ", City=" + City + ", State=" + State
				+ ", Country=" + Country + ", Phone=" + Phone + ", Mobile="
				+ Mobile + ", Mail=" + Mail + ", Note=" + Note + "]";
	}
	


}
