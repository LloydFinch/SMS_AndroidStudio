package com.delta.smsandroidproject.util;

public class Comment {
	// public static String BASE_URL = "http://172.22.35.131:7001/EMEA/";//
	// basic
	public static String BASE_URL = "";
	// url
	public static String LOGIN_URL = BASE_URL + "user_login";// log
	// url
	public static String MAP_LCOATION_URL = BASE_URL + "location/list?Id=";// map
																			// location
																			// url
	public static String GOOGLE_DIRECTION_URL = "https://maps.googleapis.com/maps/api/directions/json?";
	public static String GOOGLE_PLACE_URL = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";
	public static String LOCATION_LIST_URL = BASE_URL + "location/list?Id=%s";
	public static String CHARGER_INFO_URL = BASE_URL + "charger/list?Id=%s";
	public static String CHARGER_LIST_URL = BASE_URL + "charger/info?Id=%s";
	// location info
	public static String LOCATION_INFO_URL = BASE_URL + "location/info?Id=%s";

	public static String NET_WORK_LIST_URL = BASE_URL + "network/list?UserId=";
	public static String NET_WORK_URL = BASE_URL + "network/info?Id=";
	// http://172.22.35.131:8001/apidoc/
	public static String UPDATA_LOCATION_INFO = BASE_URL
			+ "location/info/update";// location 上传图片
	public static String UPDATA_CHARGER_INFO = BASE_URL + "charger/info/update";// charger
																				// 上传图片

	public static String EVENT_URL = BASE_URL + "event/log_list?";
	public static String LOCATION_INFO_URL_STRING = BASE_URL + "location/info";

	public static String GOOGLE_URL_STRING = "http://maps.googleapis.com/maps/api/geocode/json?latlng=";// google
																										// according
																										// to
																										// lat
																										// and
																										// lon--->get
																										// address

	// get all WhiteList(use in action dialog)
	public static String WHITELIST_ALL_URL = BASE_URL
			+ "white_list_channel/list?NetworkId=";

	// download whiteList form server
	public static String DOWNLOAD_WHILELIST_URL = BASE_URL
			+ "white_list_version/download?ChannelId=%s&VersionId=%s";

	// change or deactivate the white list version on the given channel
	public static String CHANGE_WHILELIST_URL = BASE_URL
			+ "white_list_version/change";

	public static String ACTION_URL = BASE_URL + "ocpp/cs2cp/";// basic
	// post body
	// {"ChargerId":"A1-11","EvseId":0,"UserId":"admin","Type":"Hard"}
	// EvseId必為0, 軟重置Type請填Soft, 應重置Type請填Hard.
	// 回傳格式如下.
	// {"result":"accepted"}
	public static String RESET_AND_REBOOT = ACTION_URL + "reset";

	// post body
	// {"ChargerId":"A1-11","EvseId":1,"UserId":"admin"}
	// EvseId有1,2,3,4可以使用. UserId就是登入的UserId了
	// 回傳格式如下. recordId是要做為remote_stop時使用的.
	// {"recordId":211,"result":"accepted"}
	public static String START_REMOTE = ACTION_URL + "remote_start";

	// post body
	// {"ChargerId":"A1-11","EvseId":1,"UserId":"admin","RecordId":211}
	// 回傳格式如下.
	// {"result":"accepted"}
	public static String STOP_REMOTE = ACTION_URL + "remote_stop";

	// post body
	// {"ChargerId":"A1-11","EvseId":1,"UserId":"admin","Type":"Inoperative"}
	// EvseId有0,1,2,3,4可以使用.對整台charger設定請用0.
	// Type有Inoperative, Operative兩種參數.
	// 回傳格式如下.
	// {"result":"accepted"}
	public static String ENABLE_AND_DISENABLE = ACTION_URL
			+ "change_availability";

	// post body
	// {"ChargerId":"A1-11","EvseId":1,"UserId":"admin"}
	// EvseId有1,2,3,4可以使用. UserId就是登入的UserId了
	// 回傳格式如下.
	// {"result":"accepted"}
	public static String UNLOCK = ACTION_URL + "unlock_connector";

	// post body
	// {"ChargerId":"A1-11","EvseId":0,"UserId":"admin","UpdateType":"differential","ListVersion":0}
	// EvseId必為0, UpdateType請都暫時填differential, ListVersion填0
	// 目前因為模擬器不支持, 所以回傳格式
	// {"result":"notSupported"}
	public static String UNDATE_LOCAL_WHITELIST = ACTION_URL
			+ "send_white_list";

	public static String GET_FIRMWARE_LIST = BASE_URL
			+ "firmware/get_list?ChargerType=%s";
	// Event log 详细信息
	// charger 详细信息 http://localhost:7001/EMEA/event/discription?EventId=2355796
	public static String DETAIL_EVSE_URL = BASE_URL
			+ "event/discription?EventId=%s";
	// evse 详细信息 http://localhost:7001/EMEA/event/log_list?ChargerId=8NvOxMd0c3w 
	public static String DETAIL_CHARGER_URL = BASE_URL
			+ "event/log_list?ChargerId=%s";
	// http://localhost:7001/EMEA/ocpp/cs2cp/update_firmware
	// post { "ChargerIds":"A1-10,A1-11", "FirmwareId":"5"}
	// PS. 這個API目前在131暫時不能使用
	public static String UPDATE_FIRMWARE = ACTION_URL + "update_firmware";
	public static String USER_ROLE;// 用户角色
	public static String PUBLIC_KEY = "";

	public static void setBASE_URL(String base_url) {
		BASE_URL = base_url;
		Logg.i("base_url", "" + base_url);

		// LOGIN_URL = BASE_URL + "user_login";// log
		LOGIN_URL = BASE_URL + "user/info";
		MAP_LCOATION_URL = BASE_URL + "location/list?Id=";// map
															// location
															// url
		LOCATION_LIST_URL = BASE_URL + "location/list?Id=%s";
		CHARGER_INFO_URL = BASE_URL + "charger/list?Id=%s";
		CHARGER_LIST_URL = BASE_URL + "charger/info?Id=%s";
		LOCATION_INFO_URL = BASE_URL + "location/info?Id=%s";
		NET_WORK_LIST_URL = BASE_URL + "network/list?UserId=";
		NET_WORK_URL = BASE_URL + "network/info?Id=";
		UPDATA_LOCATION_INFO = BASE_URL + "location/info/update";// location
																	// 上传图片
		UPDATA_CHARGER_INFO = BASE_URL + "charger/info/update";// charger 上传图片
		EVENT_URL = BASE_URL + "event/log_list?";
		LOCATION_INFO_URL_STRING = BASE_URL + "location/info";
		WHITELIST_ALL_URL = BASE_URL + "white_list_channel/list?NetworkId=";
		DOWNLOAD_WHILELIST_URL = BASE_URL
				+ "white_list_version/download?ChannelId=%s&VersionId=%s";
		CHANGE_WHILELIST_URL = BASE_URL + "white_list_version/change";
		ACTION_URL = BASE_URL + "ocpp/cs2cp/";// basic
		RESET_AND_REBOOT = ACTION_URL + "reset";
		START_REMOTE = ACTION_URL + "remote_start";
		STOP_REMOTE = ACTION_URL + "remote_stop";
		ENABLE_AND_DISENABLE = ACTION_URL + "change_availability";
		UNLOCK = ACTION_URL + "unlock_connector";
		UNDATE_LOCAL_WHITELIST = ACTION_URL + "send_white_list";
		GET_FIRMWARE_LIST = BASE_URL + "firmware/get_list?ChargerType=%s";
		UPDATE_FIRMWARE = ACTION_URL + "update_firmware";

		USER_ROLE = BASE_URL + "user/function?";

		PUBLIC_KEY = BASE_URL + "user/info/publickey";// 获取RSA公钥
		DETAIL_EVSE_URL = BASE_URL+"event/discription?EventId=%s";
		DETAIL_CHARGER_URL = BASE_URL + "event/log_list?ChargerId=%s";
	}
}
