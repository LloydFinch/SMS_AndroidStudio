package com.delta.smsandroidproject.model;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.request.StringCookieRequest;

public class ChargingLocModelImp implements ChargingLocModel {

	@Override
	public void loadData(Listener<String> responseListener,
			ErrorListener errorListener, String url) {
		StringCookieRequest request = new StringCookieRequest(Method.GET, url,
				responseListener, errorListener);
		SMSApplication.getRequestQueue().add(request);
	}

}
