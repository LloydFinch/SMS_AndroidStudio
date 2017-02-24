package com.delta.smsandroidproject.request;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.util.Logg;

/**
 * @author Wenqi.Wang
 * 
 */
public class StringCookieRequest extends StringRequest {

	private static final String TAG = "StringCookieRequest";

	public StringCookieRequest(String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(url, listener, errorListener);
	}

	public StringCookieRequest(int method, String url,
			Listener<String> listener, ErrorListener errorListener) {
		super(method, url, listener, errorListener);
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		Logg.i(TAG, "response.headers-" + response.headers);
		Logg.i(TAG, "response.statusCode-" + response.statusCode);
		SMSApplication.getInstance().saveSession(response.headers);
		return super.parseNetworkResponse(response);
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = super.getHeaders();
		if (headers == null || headers.equals(Collections.emptyMap())) {
			headers = new HashMap<String, String>();
		}
		SMSApplication.getInstance().addSession(headers);
		Logg.i("SESSION", "getHeaders:" + headers.toString());
		return headers;
	}
}
