package com.delta.smsandroidproject.webrequest;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public class ResponseListener<T> implements Response.ErrorListener,Listener<T>{

	@Override
	public void onResponse(T response) {
		// TODO 自动生成的方法存根
		Log.e("成功", response+"");
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO 自动生成的方法存根
		Log.e("成功", error+"");
	}

}
