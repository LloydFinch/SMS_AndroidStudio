package com.delta.smsandroidproject.model;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.request.StringCookieRequest;
import com.delta.smsandroidproject.util.Comment;

public class NetworkListModelImp implements NetworkListModel {

	@Override
	public void loadData(Listener<String> requst, ErrorListener errorListener,
			String useId) {
		StringCookieRequest request = new StringCookieRequest(
				Comment.NET_WORK_LIST_URL + useId, requst, errorListener);
		SMSApplication.getRequestQueue().add(request);
	}
}
