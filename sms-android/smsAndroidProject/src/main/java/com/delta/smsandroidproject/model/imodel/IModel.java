package com.delta.smsandroidproject.model.imodel;

import java.util.Map;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public interface IModel {
void loadData(Map<String, String> map,Listener<String> responseListener,
		ErrorListener errorLoginListener);
public void cancelAll();
}
