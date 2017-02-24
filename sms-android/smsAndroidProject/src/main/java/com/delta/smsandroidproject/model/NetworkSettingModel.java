package com.delta.smsandroidproject.model;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public interface NetworkSettingModel {
	public void loadData(Listener<String> listener,ErrorListener errorListener,String url);
}
