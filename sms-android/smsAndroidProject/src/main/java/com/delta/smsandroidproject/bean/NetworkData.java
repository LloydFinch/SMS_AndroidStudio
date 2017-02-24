package com.delta.smsandroidproject.bean;


public class NetworkData {
	//{"Id":"1","Name":"Arctic Roads","Company":"Arctic Roads",
	//"Contact":"egge","Enable":true,"Image":"http://172.22.35.131:7001/EMEA/upload/logo/network/1461302177252.jpg"}
		private String Id;
		private String Name;
		private String Image;
		private String Company;
		
		private String Contact;
		private boolean Enable;
		
		public String getId() {
			return Id;
		}
		public void setId(String id) {
			Id = id;
		}
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
		
		
		public String getCompany() {
			return Company;
		}
		public void setCompany(String company) {
			Company = company;
		}
		public String getContact() {
			return Contact;
		}
		public void setContact(String contact) {
			Contact = contact;
		}
		public boolean isEnable() {
			return Enable;
		}
		public void setEnable(boolean enable) {
			Enable = enable;
		}
		@Override
		public String toString() {
			return "NetworkData [Id=" + Id + ", Name=" + Name + ", Image=" + Image
					+ ", Company=" + Company + ", Contact=" + Contact + ", Enable="
					+ Enable + "]";
		}
}
