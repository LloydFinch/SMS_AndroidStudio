package com.delta.smsandroidproject.presenter;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.bean.LogResultData;
import com.delta.smsandroidproject.model.LoginModel;
import com.delta.smsandroidproject.model.LoginModelImp;
import com.delta.smsandroidproject.request.ParseResponse;
import com.delta.smsandroidproject.util.GsonTools;
import com.delta.smsandroidproject.util.NetConnectTools;
import com.delta.smsandroidproject.view.LoginView;

public class LoginPresenter {

	protected static final String TAG = "LoginPresenter";
	private LoginModel loginModel;
	private LoginView loginView;
	private Context context;
	public LoginPresenter(LoginView loginView,Context context) {
		this.loginView = loginView;
		loginModel = new LoginModelImp();
		this.context = context;
	}

	public void login(String uid, String pw) {
		if (NetConnectTools.isNetworkAvailable(SMSApplication.getInstance()
				.getApplicationContext())) {
			loginView.dismiss();
			loginView.showDialog();
			loginModel.loadData(requestListener(), errorListener(), uid, pw);
		} else {
			loginView.showNetAlertDialog();
		}
	}

	public Listener<String> requestListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				response = ParseResponse.parseResponse(response);
				Log.i(TAG, "json-" + response);
				loginView.dismiss();
				LogResultData logResultData = GsonTools.changeGsonToBean(
						response, LogResultData.class);
				if (logResultData != null) {
					Log.i("logResultData", "" + logResultData);
					if (logResultData.getResult() != null
							&& logResultData.getResult().equals("Failure")) {
						loginView.failed(logResultData.getMessage());
					} else if (logResultData.getResult() != null
							&& logResultData.getResult().equals("Successful")) {
						loginView.successed(logResultData.getMessage());
						loginView.intentToMainActivity();
					}
				}
			}
		};
	}

	public Response.ErrorListener errorListener() {
		return new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, "login failed-" + error.getMessage());
				loginView.dismiss();
				loginView.failed("network is not connected");
			}
		};

	}

}
