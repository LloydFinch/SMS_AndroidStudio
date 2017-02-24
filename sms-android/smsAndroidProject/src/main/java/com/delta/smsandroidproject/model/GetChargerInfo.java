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

public class GetChargerInfo implements IModel{
private String Id = "2";
	@Override
	public void loadData(Map<String, String> map,
			Listener<String> responseListener, ErrorListener errorLoginListener) {
		if (map==null) {
			 Id = "2";
		}else{
			Id = map.get("key");
		}
		StringCookieRequest request = new StringCookieRequest(Method.GET, String.format(Comment.CHARGER_INFO_URL, Id), responseListener, errorLoginListener);
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
