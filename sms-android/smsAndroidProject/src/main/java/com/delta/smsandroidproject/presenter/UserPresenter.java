package com.delta.smsandroidproject.presenter;

import java.util.List;

import android.content.Context;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.delta.smsandroidproject.bean.UseData;
import com.delta.smsandroidproject.bean.UseData.Function;
import com.delta.smsandroidproject.model.UseModelImp;
import com.delta.smsandroidproject.request.ParseResponse;
import com.delta.smsandroidproject.request.SessionTimeoutHandler;
import com.delta.smsandroidproject.util.Logg;
import com.delta.smsandroidproject.view.UseView;
import com.google.gson.Gson;

/**
 * 用户角色
 * 
 * @author Jianzao.Zhang
 * 
 */
public class UserPresenter {
	private static final String TAG = "UserPresenter";
	private UseView view;
	private UseModelImp UseModel;
	private Context context;

	public UserPresenter(UseView view, Context context) {
		this.view = view;
		UseModel = new UseModelImp();
		this.context = context;
	}

	public void loadData(String url) {
		Logg.i(TAG, "url-" + url);
		UseModel.loadData(listener(), errorListener(), url);
		view.showDialog();
	}

	private ErrorListener errorListener() {
		return new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Logg.i(TAG, "error" + error.getMessage());
				view.dimiss();
				SessionTimeoutHandler.handSessionTimeout(context, error);
			}
		};
	}

	private Listener<String> listener() {
		return new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Logg.i(TAG, "response1-" + response);
				view.dimiss();
				if (response != null) {
					response = ParseResponse.parseResponse(response);
					Logg.i(TAG, "response2-" + response);
					Gson gson = new Gson();
					UseData data = gson.fromJson(response, UseData.class);
					Logg.i(TAG, "UseData-" + data);
					if (data != null) {
						List<Function> functions = data.getFunction();
						Logg.i(TAG, "functions-" + functions);
						if (functions != null && functions.size() > 0) {
							view.setUseRole(functions.get(0));
						}
					}
				}
			}
		};
	}
}
