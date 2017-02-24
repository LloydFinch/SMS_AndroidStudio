package com.delta.smsandroidproject.bean;

import java.io.Serializable;


public class EventListData implements Serializable{
	/**
	 * oderby
	 */
	public static final String LEVEL = "level";
	public static final String OCCURRENCE = "occurrence";
	public static final String CLEARANCE = "clearance";
	public static final String TITLE = "title";
	public static final String EVENT_ID = "EVENT_ID";
	/**
	 * level相关说明
	 * /*
	 */
	public static final String CHARGE = "CHARGE";
	public static final String CONFIGURATION = "CONFIGURATION";
	public static final String INFORMATION = "INFORMATION";
	public static final String WARNING = "WARNING";
	public static final String FAULT = "FAULT";
	public static final String EMERGENCY = "EMERGENCY";
	
	/**
	 * LevelId
	 * LOCK(-1),
	 * 	CHARGE(1),
	    CONFIGURATION(2),
	    INFORMATION(3),
	    WARNING(4),
	    FAULT(5),
	    EMERGENCY(6);
	 */
	public static final String CHARGE_LEVELID = "1";
	public static final String CONFIGURATION_LEVELID = "2";
	public static final String INFORMATION_LEVELID = "3";
	public static final String WARNING_LEVELID = "4";
	public static final String FAULT_LEVELID = "5";
	public static final String EMERGENCY_LEVELID = "6";
	
	private String Level;
	private String Occurrence;
	private String Clearance;
	private String Title;
	private String Status;
	private String ChargerId;
	private String EvseId;
	private String EventId;
	private String TypeId;
	public String getLevel() {
		return Level;
	}
	public void setLevel(String level) {
		Level = level;
	}
	public String getOccurrence() {
		return Occurrence;
	}
	public void setOccurrence(String occurrence) {
		Occurrence = occurrence;
	}
	public String getClearance() {
		return Clearance;
	}
	public void setClearance(String clearance) {
		Clearance = clearance;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getChargerId() {
		return ChargerId;
	}
	public void setChargerId(String chargerId) {
		ChargerId = chargerId;
	}
	public String getEvseId() {
		return EvseId;
	}
	public void setEvseId(String evseId) {
		EvseId = evseId;
	}
	
	public String getEventId() {
		return EventId;
	}
	public void setEventId(String eventId) {
		EventId = eventId;
	}
	public String getTypeId() {
		return TypeId;
	}
	public void setTypeId(String typeId) {
		TypeId = typeId;
	}
	@Override
	public String toString() {
		return "EventListData [Level=" + Level + ", Occurrence=" + Occurrence
				+ ", Clearance=" + Clearance + ", Title=" + Title + ", Status="
				+ Status + ", ChargerId=" + ChargerId + ", EvseId=" + EvseId
				+ ", EventId=" + EventId + ", TypeId=" + TypeId + "]";
	}
	
	public class EventLogCount{
		private String Count;

		public String getCount() {
			return Count;
		}

		public void setCount(String count) {
			Count = count;
		}

		@Override
		public String toString() {
			return "EventLogCount [Count=" + Count + "]";
		}
	}
	
}
