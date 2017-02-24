package com.delta.smsandroidproject.bean;

import java.util.List;

//use in the firmware dialog
public class FirmwareModel {
	private List<FirmWare> Results;
	private String Total;

	public List<FirmWare> getResults() {
		return Results;
	}

	public void setResults(List<FirmWare> results) {
		Results = results;
	}

	public String getTotal() {
		return Total;
	}

	public void setTotal(String total) {
		Total = total;
	}

	public class FirmWare {
		private String FirmwareId;
		private String Name;
		private String UploadDate;
		private String Version;
		private String ChargerNumber;
		private String ChargerType;
		private String ChargerTypeName;

		public String getFirmwareId() {
			return FirmwareId;
		}

		public void setFirmwareId(String firmwareId) {
			FirmwareId = firmwareId;
		}

		public String getName() {
			return Name;
		}

		public void setName(String name) {
			Name = name;
		}

		public String getUploadDate() {
			return UploadDate;
		}

		public void setUploadDate(String uploadDate) {
			UploadDate = uploadDate;
		}

		public String getVersion() {
			return Version;
		}

		public void setVersion(String version) {
			Version = version;
		}

		public String getChargerNumber() {
			return ChargerNumber;
		}

		public void setChargerNumber(String chargerNumber) {
			ChargerNumber = chargerNumber;
		}

		public String getChargerType() {
			return ChargerType;
		}

		public void setChargerType(String chargerType) {
			ChargerType = chargerType;
		}

		public String getChargerTypeName() {
			return ChargerTypeName;
		}

		public void setChargerTypeName(String chargerTypeName) {
			ChargerTypeName = chargerTypeName;
		}

	}
}
