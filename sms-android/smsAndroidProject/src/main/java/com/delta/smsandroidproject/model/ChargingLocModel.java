package com.delta.smsandroidproject.model;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public interface ChargingLocModel {
	public void loadData(Listener<String> responseListener,
			ErrorListener errorLoginListener,String url);
}
