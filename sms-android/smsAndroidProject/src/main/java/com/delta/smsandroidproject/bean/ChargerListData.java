package com.delta.smsandroidproject.bean;

import java.io.Serializable;
import java.util.Arrays;

public class ChargerListData implements Serializable{
	private String Name;
	private String Image;
	private String Capacity;
	private String Status;
	private String ChargeBoxId;
	private String Endpoint;
	private String SerialNumber;
	private String Type;
	private String Firmware;
	private Evse[] Evse;
	// private String evse;
	private String note;

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

	public String getCapacity() {
		return Capacity;
	}

	public void setCapacity(String capacity) {
		Capacity = capacity;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getChargeBoxId() {
		return ChargeBoxId;
	}

	public void setChargeBoxId(String chargeBoxId) {
		ChargeBoxId = chargeBoxId;
	}

	public String getEndpoint() {
		return Endpoint;
	}

	public void setEndpoint(String endpoint) {
		Endpoint = endpoint;
	}

	public String getSerialNumber() {
		return SerialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		SerialNumber = serialNumber;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getFirmware() {
		return Firmware;
	}

	public void setFirmware(String firmware) {
		Firmware = firmware;
	}

	public Evse[] getEvse() {
		return Evse;
	}

	public void setEvse(Evse[] evse) {
		evse = evse;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return "ChargerListData [Name=" + Name + ", Image=" + Image
				+ ", Capacity=" + Capacity + ", Status=" + Status
				+ ", ChargeBoxId=" + ChargeBoxId + ", Endpoint=" + Endpoint
				+ ", SerialNumber=" + SerialNumber + ", Type=" + Type
				+ ", Firmware=" + Firmware + ", Evse=" + Arrays.toString(Evse)
				+ ", note=" + note + "]";
	}

	public class Evse implements Serializable{
		private String Id;
		private String Name;
		private String Status;
		private Connectors[] Connector;

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

		public String getStatus() {
			return Status;
		}

		public void setStatus(String status) {
			Status = status;
		}

		public Connectors[] getConnector() {
			return Connector;
		}

		public void setConnector(Connectors[] connector) {
			connector = Connector;
		}

		@Override
		public String toString() {
			return "Evse [Id=" + Id + ", Name=" + Name + ", Status=" + Status
					+ ", Connector=" + Arrays.toString(Connector) + "]";
		}

		public class Connectors implements Serializable{
			private String Id;
			private String Name;
			private String Type;
			private String Status;
			
			public Connectors(String id, String name, String type, String status) {
				this.Id =id;
				this.Name = name;
				this.Type = type;
				this.Status =status;
			}

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

			public String getType() {
				return Type;
			}

			public void setType(String type) {
				Type = type;
			}

			public String getStatus() {
				return Status;
			}

			public void setStatus(String status) {
				Status = status;
			}

			@Override
			public String toString() {
				return "Connector [Id=" + Id + ", Name=" + Name + ", Type="
						+ Type + ", Status=" + Status + "]";
			}

		}
	}
}
