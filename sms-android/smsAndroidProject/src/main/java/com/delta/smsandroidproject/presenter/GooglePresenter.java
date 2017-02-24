package com.delta.smsandroidproject.presenter;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.bean.TestResult;
import com.delta.smsandroidproject.request.ParseResponse;
import com.delta.smsandroidproject.util.Comment;
import com.delta.smsandroidproject.util.Logg;
import com.delta.smsandroidproject.util.ToolUtil;
import com.google.gson.Gson;

/**
 * according lat and lon ---->get current address
 * 
 * @author Jianzao.Zhang
 * 
 */
public class GooglePresenter {
	public GooglePresenter() {

	}

	public void getAddress(double lat, double lon) {
		String url = Comment.GOOGLE_URL_STRING + lat + "," + lon
				+ "&sensor=false&language=zh-CN";
		Logg.i("getAddress -url", "" + url);
		StringRequest request = new StringRequest(url, listener(),
				errorListener());
		SMSApplication.getRequestQueue().add(request);
	}

	private Listener<String> listener() {
		return new Listener<String>() {

			@Override
			public void onResponse(String response) {
				response = ParseResponse.parseResponse(response);
				Logg.i("GooglePresenter--response", "" + response);
				Gson gson = new Gson();
				TestResult result = gson.fromJson(response, TestResult.class);
				if (result != null && result.getResults() != null
						&& result.getResults().size() > 0) {
					String address = result.getResults().get(0)
							.getFormatted_address();
					Logg.i("address", "" + address);
					ToolUtil.saveMyLocAddress(address);
				}
			}
		};

	}

	private ErrorListener errorListener() {
		return new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {

			}
		};

	}
}
