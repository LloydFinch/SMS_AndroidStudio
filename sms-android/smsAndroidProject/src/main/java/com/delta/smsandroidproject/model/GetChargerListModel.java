package com.delta.smsandroidproject.model;

import java.util.Map;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.model.imodel.IModel;
import com.delta.smsandroidproject.request.StringCookieRequest;
import com.delta.smsandroidproject.util.Comment;

public class GetChargerListModel implements IModel{

	@Override
	public void loadData(Map<String, String> map,
			Listener<String> responseListener, ErrorListener errorLoginListener) {
		StringCookieRequest request = new StringCookieRequest(Method.GET, String.format(Comment.CHARGER_LIST_URL, map.get("key")), responseListener, errorLoginListener);
		request.setRetryPolicy(new DefaultRetryPolicy(3000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		SMSApplication.getRequestQueue().add(request);
		
	}

	@Override
	public void cancelAll() {
		// TODO 自动生成的方法存根
		
	}

}
