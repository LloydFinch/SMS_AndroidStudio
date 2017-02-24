package com.delta.smsandroidproject.model;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.request.StringCookieRequest;

/**
 * 用户角色
 * 
 * @author Jianzao.Zhang
 * 
 */
public class UseModelImp implements UseModel {

	@Override
	public void loadData(Listener<String> listener,
			ErrorListener errorListener, String url) {
		StringCookieRequest request = new StringCookieRequest(url, listener,
				errorListener);
		SMSApplication.getRequestQueue().add(request);
	}

}
