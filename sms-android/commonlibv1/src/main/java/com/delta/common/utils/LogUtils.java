package com.delta.common.utils;

import android.util.Log;

import com.delta.common.BuildConfig;

public class LogUtils {
	
	public static void i(String tag, String msg)
	{
		if (BuildConfig.DEBUG)
			Log.i(tag, msg);
	}

	public static void d(String tag, String msg)
	{
		if (BuildConfig.DEBUG)
			Log.d(tag, msg);
	}

	public static void e(String tag, String msg)
	{
		if (BuildConfig.DEBUG)
			Log.e(tag, msg);
	}

	public static void v(String tag, String msg)
	{
		if (BuildConfig.DEBUG)
			Log.v(tag, msg);
	}

	
}
