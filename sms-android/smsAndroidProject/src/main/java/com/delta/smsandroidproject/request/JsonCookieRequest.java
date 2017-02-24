/**
 * 
 */
package com.delta.smsandroidproject.request;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.util.Logg;

/**
 * @author Wenqi.Wang
 * 
 */
public class JsonCookieRequest extends JsonObjectRequest {

	public JsonCookieRequest(String url, JSONObject jsonRequest,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		super(url, jsonRequest, listener, errorListener);
	}

	public JsonCookieRequest(int method, String url, JSONObject jsonRequest,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		super(method, url, jsonRequest, listener, errorListener);
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		Logg.i("Header-parse",
				"response.statusCode-" + response.headers.toString());
		Logg.i("Header-parse", "response.statusCode-" + response.statusCode);
		SMSApplication.getInstance().saveSession(response.headers);
		return super.parseNetworkResponse(response);
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = super.getHeaders();
		if (headers == null || headers.equals(Collections.emptyMap())) {
			headers = new HashMap<String, String>();
		}
		headers.put("Accept", "application/json");
		headers.put("Content-Type", "application/json; charset=UTF-8");
		SMSApplication.getInstance().addSession(headers);
		Logg.i("Header-getHeaders", headers.toString());
		return headers;
	}
}
