package com.delta.smsandroidproject.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.util.Log;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.bean.EventListData;

public class ToolUtil {
	private static final String TAG = "ToolUtil";
	private static final String KEY = "delta";// aes的密钥
	/**
	 * 过滤的状态
	 */
	private static final String CHARGER = "charger";
	private static final String CONFIGRATION = "configration";
	private static final String INFOMATION = "infomation";
	private static final String WARNING = "warning";
	private static final String FAULT = "fault";
	private static final String EMEGENTCY = "emegentcy";

	// private static final String SUPERADMIN = "superadmin";
	public static final String IS_SERVICE = "Service";
	public static final String IS_SUPPORT = "Support";
	private static final String POS = "filterPos";
	private static final String END_TIME = "endTime";
	private static final String START_TIME = "startTime";
	private static final String REMEMBER_ME = "rememberMe";
	private static Context context;
	private static ClipboardManager cmb = null;
	private static SharedPreferences share;

	static {
		context = SMSApplication.getInstance().getApplicationContext();
		init();
	}

	public ToolUtil() {
	}

	private static void init() {
		if (share == null) {
			share = context
					.getSharedPreferences("folder", Context.MODE_PRIVATE);
		}
		if (cmb == null) {
			cmb = (ClipboardManager) context
					.getSystemService(context.CLIPBOARD_SERVICE);
		}
	}

	public static void setText(String s) {
		cmb.setText(s);
	}

	public static String getText() {
		return cmb.getText().toString().trim();
	}

	public static void saveLogin(String uid, String pw) {
		AESCipher aesUtils = new AESCipher();
		try {
			String encrypt = aesUtils.encrypt(KEY, pw);// aes加密
			Logg.i(TAG, "encrypt-" + encrypt);
			Editor editor = share.edit();
			editor.putString("uid", uid);
			editor.putString("pw", encrypt);
			editor.commit();
		} catch (Exception e) {
			ToastCustom
					.showToast(
							context,
							context.getResources().getString(
									R.string.encryption_error),
							ToastCustom.LENGTH_SHORT);
			e.printStackTrace();
		}// aes加密
	}

	public static void clearLogin() {
		Editor editor = share.edit();
		editor.remove("uid");
		editor.remove("pw");
		editor.commit();
	}

	public static void saveIsSevice(String isService) {
		Editor editor = share.edit();
		editor.putString("isService", isService);
		editor.commit();
	}

	public static Boolean notService() {
		String service = share.getString("isService", "");
		if (service != null && (service.equals(IS_SERVICE))) {
			return false;
		}
		return true;
	}

	public static String getUid() {
		return share.getString("uid", "");
	}

	public static String getPw() {
		AESCipher aesUtils = new AESCipher();
		try {
			String decrypt = aesUtils.decrypt(KEY, share.getString("pw", ""));// aes解密
			return decrypt;
		} catch (Exception e) {
			Log.i("getPw","error:"+e.toString());
			e.printStackTrace();
		}// aes解密
		return "";
	}

	public static void saveNetworkId(String networkId, String networkName) {
		Editor editor = share.edit();
		editor.putString("networkId", networkId);
		editor.putString("networkName", networkName);
		Logg.i("setIsSelectNetwork-networkId", "" + networkId);
		editor.commit();
	}

	public static void setIsSelectNetwork(String networkId, String networkName) {
		Editor editor = share.edit();
		editor.putString("networkId", networkId);
		editor.putString("networkName", networkName);
		Logg.i("setIsSelectNetwork-networkId", "" + networkId);
		if (!networkName.equals(getNetworkName())) {
			saveCurrentLoc(context.getResources()
					.getString(R.string.maps_words), null, null);
		}
		editor.commit();
	}

	public static void saveDefaultNetwork(String networkId, String networkName) {
		Editor editor = share.edit();
		editor.putString("defaultNetworkId", networkId);
		editor.putString("defaultNetworkName", networkName);
		editor.commit();
	}

	/**
	 * get networkId
	 * 
	 * @return
	 */
	public static String getNetworkId() {
		String string = share.getString("networkId", "");
		Logg.i("networkId", "" + string);
		return string;
	}

	public static String getNetworkName() {
		String string = share.getString("networkName", "");
		Logg.i("getNetworkName", "" + string);
		return string;
	}

	public static String getDefaultNetworkName() {
		String string = share.getString("defaultNetworkName", "");
		Logg.i("defaultNetworkName", "" + string);
		return string;
	}

	public static String getDefaultNetworkId() {
		String string = share.getString("defaultNetworkId", "");
		Logg.i("defaultNetworkId", "" + string);
		return string;
	}

	/**
	 * 保存定位当前位置（mylocation）的经纬度
	 * 
	 * @param lati
	 * @param lon
	 */
	public static void saveMyLatingLoc(double lati, double lon) {
		Editor editor = share.edit();
		Float latFloat = Float.valueOf(lati + "");
		Logg.i("latFloat", "" + latFloat);
		editor.putFloat("myLat", latFloat);
		Float lonFloat = Float.valueOf(lon + "");
		Logg.i("lonFloat", "" + lonFloat);
		editor.putFloat("myLon", lonFloat);
		editor.commit();
	}

	public static float getMyLat() {
		return share.getFloat("myLat", 0.0f);
	}

	public static float getMyLon() {
		return share.getFloat("myLon", 0.0f);
	}

	/**
	 * 保存当前location（不是mylocation）的经纬度
	 * 
	 * @param loc
	 * @param lat
	 * @param lon
	 */
	public static void saveCurrentLoc(String loc, Double lat, Double lon) {
		Editor editor = share.edit();
		editor.putString("currentLoc", loc);
		editor.putString("currentLocLat", String.valueOf(lat));
		editor.putString("currentLocLon", String.valueOf(lon));
		editor.commit();
	}

	public static void saveMyLocAddress(String address) {
		Editor editor = share.edit();
		editor.putString("myLocAddress", address);
		editor.commit();
	}

	public static String getMyLocAddress() {
		return share.getString("myLocAddress", "");

	}

	public static void saveCurLocAddress(String address) {
		Editor editor = share.edit();
		editor.putString("currentLocAddress", address);
		editor.commit();
	}

	public static String getCurLocAddress() {
		return share.getString("currentLocAddress", "");

	}

	public static String getCurrentLoc() {
		return share.getString("currentLoc",
				context.getResources().getString(R.string.maps_words));
	}

	public static Double getCurLocLat() {
		String string = share.getString("currentLocLat", "null");
		if (string.equals("null")) {
			return null;
		} else {
			return Double.valueOf(string);
		}
	}

	public static Double getCurLocLon() {
		String string = share.getString("currentLocLon", "null");
		if (string.equals("null")) {
			return null;
		} else {
			return Double.valueOf(string);
		}
	}

	public static int findNetworkNameIndex(List<String> strings) {
		if (strings != null) {
			for (int i = 0; i < strings.size(); i++) {
				String string = strings.get(i);
				if (string.equals(getNetworkName())) {
					return i;
				}
			}
		}
		return 0;
	}

	public static int findDefaultNetworkNameIndex(List<String> strings) {
		if (strings != null) {
			for (int i = 0; i < strings.size(); i++) {
				String string = strings.get(i);
				if (string.equals(getDefaultNetworkName())) {
					return i;
				}
			}
		}
		return 0;
	}

	public static void saveLanguage(String s, int id) {
		Editor editor = share.edit();
		editor.putString("languageName", s);
		editor.putInt("languageId", id);
		editor.commit();
	}

	public static String getLanguageName() {
		String string = share.getString("languageName", context.getResources()
				.getString(R.string.settings_language_english));
		Logg.i("language", "" + string);
		return string;
	}

	public static void saveSysLanguage(String language) {
		Editor editor = share.edit();
		editor.putString("sysLanguage", language);
		editor.commit();
	}

	public static String getSysLanguage() {
		return share.getString("sysLanguage", "en");
	}

	public static int findLanguageIndex(List<String> strings) {
		if (strings != null) {
			for (int i = 0; i < strings.size(); i++) {
				String string = strings.get(i);
				if (string.equals(getLanguageName())) {
					return i;
				}
			}
		}
		return 0;

	}

	public static int getLanguageId() {
		return share.getInt("languageId", 0);

	}

	/**
	 * 保存用戶选择的排序
	 * 
	 * @param oderby
	 */
	public static void saveEventOderby(String oderby) {
		Editor editor = share.edit();
		editor.putString("oderby", oderby);
		editor.putString("user", getUid());
		editor.commit();
	}

	/**
	 * 获取用户选择某种事件排序
	 * 
	 * @return
	 */
	public static String getEventOderby() {
		String user = share.getString("user", "");
		String oderby = EventListData.LEVEL;
		if (getUid().equals(user)) {
			oderby = share.getString("oderby", EventListData.LEVEL);
		}
		return oderby;
	}

	/**
	 * 保存用户要过滤的事件
	 * 
	 * @param filter
	 */
	public static void saveEventFilter(Map<String, Boolean> filter) {
		Editor editor = share.edit();
		editor.putString("user", getUid());
		editor.putBoolean(EMEGENTCY,
				filter.get(EventListData.EMERGENCY_LEVELID));
		editor.putBoolean(FAULT, filter.get(EventListData.FAULT_LEVELID));
		editor.putBoolean(WARNING, filter.get(EventListData.WARNING_LEVELID));
		editor.putBoolean(INFOMATION,
				filter.get(EventListData.INFORMATION_LEVELID));
		editor.putBoolean(CONFIGRATION,
				filter.get(EventListData.CONFIGURATION_LEVELID));
		editor.putBoolean(CHARGER, filter.get(EventListData.CHARGE_LEVELID));
		editor.commit();
	}

	/**
	 * 获取用户过滤的事件
	 * 
	 * @return
	 */
	public static List<Boolean> getEventFilter() {
		List<Boolean> filteList = null;
		String user = share.getString("user", "");
		if (getUid().equals(user)) {
			filteList = new ArrayList<>();
			filteList.add(share.getBoolean(EMEGENTCY, true));
			filteList.add(share.getBoolean(FAULT, true));
			filteList.add(share.getBoolean(WARNING, true));
			filteList.add(share.getBoolean(INFOMATION, true));
			filteList.add(share.getBoolean(CONFIGRATION, true));
			filteList.add(share.getBoolean(CHARGER, true));
		}

		return filteList;
	}

	/**
	 * 保存按时间筛选eventlog
	 * 
	 * @param startTime
	 * @param endTime
	 * @param pos
	 */
	public static void saveFilterTime(String startTime, String endTime, int pos) {
		Editor editor = share.edit();
		editor.putString(START_TIME, startTime);
		editor.putString(END_TIME, endTime);
		editor.putInt(POS, pos);
		editor.commit();
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public static String getFilterStartTime() {
		return share.getString(START_TIME, "");
	}

	public static String getFilterEndTime() {
		return share.getString(END_TIME, "");
	}

	public static int getFilterPos() {
		return share.getInt(POS, 0);
	}

	public static boolean simIsExist() {
		// TODO Auto-generated method stub
		TelephonyManager mTelephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		int simState = mTelephonyManager.getSimState();
		switch (simState) {
		case TelephonyManager.SIM_STATE_UNKNOWN:
			return false;
		case TelephonyManager.SIM_STATE_ABSENT:
			return false;
		case TelephonyManager.SIM_STATE_READY:
			return true;
		default:
			return false;
		}
	}

	public static void setRememberMe(boolean remember) {
		Editor editor = share.edit();
		editor.putBoolean(REMEMBER_ME, remember);
		editor.commit();
	}

	public static boolean getRememberMe() {
		return share.getBoolean(REMEMBER_ME, false);
	}

}
