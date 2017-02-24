package com.delta.smsandroidproject.model;

import android.util.Log;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.request.StringCookieRequest;
import com.delta.smsandroidproject.util.Comment;

public class EventModelImp implements EventModel {

	@Override
	public void loadData(Listener<String> listener,
			ErrorListener errorListener, String url) {
		StringCookieRequest request = new StringCookieRequest(Comment.EVENT_URL
				+ url, listener, errorListener);
		Log.e("请求URL", Comment.EVENT_URL
				+ url);
		SMSApplication.getRequestQueue().add(request);
	}

}
