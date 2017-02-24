/**
 * 
 */
package com.delta.smsandroidproject.bean;

import java.util.List;

/**
 * @author Wenqi.Wang
 * 
 */
public class InfoByPush {

	private String type;
	private ChargerPush charger;
	private LocationPush location;
	private List<EvsePush> evse;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ChargerPush getCharger() {
		return charger;
	}

	public void setCharger(ChargerPush charger) {
		this.charger = charger;
	}

	public LocationPush getLocation() {
		return location;
	}

	public void setLocation(LocationPush location) {
		this.location = location;
	}

	public List<EvsePush> getEvse() {
		return evse;
	}

	public void setEvse(List<EvsePush> evse) {
		this.evse = evse;
	}

	public static class LocationPush {
		// locationId":"97","locationLevel":"1"
		private String locationId;
		private String locationLevel;

		// private String chargerStatus;
		public String getLocationId() {
			return locationId;
		}

		public void setLocationId(String locationId) {
			this.locationId = locationId;
		}

		public String getLocationLevel() {
			return locationLevel;
		}

		public void setLocationLevel(String locationLevel) {
			this.locationLevel = locationLevel;
		}

	}

	public static class ChargerPush {

		private String chargerId;
		private String chargerLevel;
		private String chargerStatus;
		private String chargerFirmwareVersion;

		public String getChargerId() {
			return chargerId;
		}

		public void setChargerId(String chargerId) {
			this.chargerId = chargerId;
		}

		public String getChargerLevel() {
			return chargerLevel;
		}

		public void setChargerLevel(String chargerLevel) {
			this.chargerLevel = chargerLevel;
		}

		public String getChargerStatus() {
			return chargerStatus;
		}

		public void setChargerStatus(String chargerStatus) {
			this.chargerStatus = chargerStatus;
		}

		public String getChargerFirmwareVersion() {
			return chargerFirmwareVersion;
		}

		public void setChargerFirmwareVersion(String chargerFirmwareVersion) {
			this.chargerFirmwareVersion = chargerFirmwareVersion;
		}

	}

	// public static class LocationPush {
	// private String locationId;
	// private String locationLevel;
	//
	// public String getLocationId() {
	// return locationId;
	// }
	//
	// public void setLocationId(String locationId) {
	// this.locationId = locationId;
	// }
	//
	// public String getLocationLevel() {
	// return locationLevel;
	// }
	//
	// public void setLocationLevel(String locationLevel) {
	// this.locationLevel = locationLevel;
	// }
	// }

	public static class EvsePush {
		private String evseId;
		private String evseStatus;

		public String getEvseId() {
			return evseId;
		}

		public void setEvseId(String evseId) {
			this.evseId = evseId;
		}

		public String getEvseStatus() {
			return evseStatus;
		}

		public void setEvseStatus(String evseStatus) {
			this.evseStatus = evseStatus;
		}
	}
}
