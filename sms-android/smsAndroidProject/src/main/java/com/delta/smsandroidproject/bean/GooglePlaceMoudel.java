package com.delta.smsandroidproject.bean;

//parse the API GooglePlaces
public class GooglePlaceMoudel {

	private String status;
	private Result result;

	public GooglePlaceMoudel() {

	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public class Result {

		private String place_id;
		private String name;
		private String formatted_address;

		public String getPlace_id() {
			return place_id;
		}

		public void setPlace_id(String place_id) {
			this.place_id = place_id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getFormatted_address() {
			return formatted_address;
		}

		public void setFormatted_address(String formatted_address) {
			this.formatted_address = formatted_address;
		}

	}
}
