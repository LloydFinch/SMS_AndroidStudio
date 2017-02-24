package com.delta.smsandroidproject.util;

import android.util.Log;

public class Logg {
	static final boolean showDebug = true;
	static final boolean showInfo = true;
	static final boolean showWarn = true;
	static final boolean showErroe = true;
	
	public static void d(String tag, String msg) {
		if(showDebug){
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, int msg) {
		if(showInfo){
			Log.i(tag, msg + "");
		}
	}
	
	public static void i(String tag, String msg) {
		if(showInfo){
			Log.i(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if(showWarn){
			Log.w(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if(showErroe){
			Log.e(tag, msg);
		}
	}
}
