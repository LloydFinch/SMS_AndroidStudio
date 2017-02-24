package com.delta.smsandroidproject.model;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.request.StringCookieRequest;

public class PublicKeyModelImp implements PublicKeyModel{

	@Override
	public void loadData(Listener<String> listener,
			ErrorListener errorListener, String url) {
//		StringRequest request = new StringRequest(url, listener, errorListener);
//		SMSApplication.getRequestQueue().add(request);
		StringCookieRequest request = new StringCookieRequest(url, listener, errorListener);
		SMSApplication.getRequestQueue().add(request);
	}

}
