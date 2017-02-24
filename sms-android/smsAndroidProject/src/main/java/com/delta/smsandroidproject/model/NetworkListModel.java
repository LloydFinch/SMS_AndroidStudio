package com.delta.smsandroidproject.model;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;


public interface NetworkListModel {
	public void loadData(Listener<String> requst,ErrorListener errorListener,String useId);
}
