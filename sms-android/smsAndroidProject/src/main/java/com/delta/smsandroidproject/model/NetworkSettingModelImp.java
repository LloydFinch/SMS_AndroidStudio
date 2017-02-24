package com.delta.smsandroidproject.model;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.request.StringCookieRequest;
import com.delta.smsandroidproject.util.Comment;

public class NetworkSettingModelImp implements NetworkSettingModel {

	@Override
	public void loadData(Listener<String> listener,
			ErrorListener errorListener, String url) {
		StringCookieRequest request = new StringCookieRequest(
				Comment.NET_WORK_URL + url, listener, errorListener);
		SMSApplication.getRequestQueue().add(request);
	}

}
