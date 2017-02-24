package com.delta.smsandroidproject.model;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public interface LoginModel {
	public void cancelAll();
	void loadData(Listener<String> responseListener,
			ErrorListener errorLoginListener, String uid,String pw);
}
