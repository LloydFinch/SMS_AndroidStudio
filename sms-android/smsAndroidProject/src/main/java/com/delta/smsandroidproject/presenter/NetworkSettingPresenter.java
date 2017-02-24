package com.delta.smsandroidproject.presenter;

import android.content.Context;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.delta.common.utils.GsonTools;
import com.delta.smsandroidproject.bean.NetwrkInfoData;
import com.delta.smsandroidproject.model.NetworkSettingModel;
import com.delta.smsandroidproject.model.NetworkSettingModelImp;
import com.delta.smsandroidproject.request.ParseResponse;
import com.delta.smsandroidproject.request.SessionTimeoutHandler;
import com.delta.smsandroidproject.util.Logg;
import com.delta.smsandroidproject.view.NetworkSettingView;

public class NetworkSettingPresenter {
	private NetworkSettingView view;
	private NetworkSettingModel model;
	private Context context;

	public NetworkSettingPresenter(NetworkSettingView view, Context context) {
		this.view = view;
		this.model = new NetworkSettingModelImp();
		this.context = context;
	}

	public void loadData(String url) {
		view.showDialog();
		model.loadData(listener(), errorListener(), url);

	}

	private Listener<String> listener() {
		return new Listener<String>() {

			@Override
			public void onResponse(String response) {
				response = ParseResponse.parseResponse(response);
				view.dimiss();
				response = ParseResponse.parseResponse(response);
				Logg.i("NetworkSettingPresenter-response", "" + response);
				NetwrkInfoData data = GsonTools.changeGsonToBean(response,
						NetwrkInfoData.class);
				if (data != null) {
					view.setNetwrkInfoData(data);
				}
			}
		};

	}

	private ErrorListener errorListener() {
		return new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				view.dimiss();
				Logg.e("error", "" + error.getMessage());
				SessionTimeoutHandler.handSessionTimeout(context, error);
			}
		};
	}
}
