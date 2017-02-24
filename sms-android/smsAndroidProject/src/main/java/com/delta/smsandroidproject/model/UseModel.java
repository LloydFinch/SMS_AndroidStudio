package com.delta.smsandroidproject.model;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
/**
 * 用户角色
 * @author Jianzao.Zhang
 *
 */
public interface UseModel {
	public void loadData(Listener<String> listener,ErrorListener errorListener,String url);
}
