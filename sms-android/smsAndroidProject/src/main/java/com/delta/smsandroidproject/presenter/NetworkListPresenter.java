package com.delta.smsandroidproject.presenter;

import java.util.List;

import android.content.Context;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.delta.common.utils.GsonTools;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.bean.NetworkData;
import com.delta.smsandroidproject.bean.NetworkListResultTotalData;
import com.delta.smsandroidproject.model.NetworkListModel;
import com.delta.smsandroidproject.model.NetworkListModelImp;
import com.delta.smsandroidproject.request.ParseResponse;
import com.delta.smsandroidproject.request.SessionTimeoutHandler;
import com.delta.smsandroidproject.util.Logg;
import com.delta.smsandroidproject.util.NetConnectTools;
import com.delta.smsandroidproject.view.NetworkListView;

public class NetworkListPresenter {
	private NetworkListModel model;
	private NetworkListView view;
	private Context context;

	public NetworkListPresenter(NetworkListView view, Context context) {
		this.view = view;
		this.model = new NetworkListModelImp();
		this.context = context;
	}

	public void loadData(String useId, int PageNo, int PerPage) {
		if (NetConnectTools.isNetworkAvailable(SMSApplication.getInstance()
				.getApplicationContext())) {
			view.dimiss();
			view.showDialog();
			model.loadData(request(), errorListener(), useId);
		} else {
			view.showNetAlertDialog();
		}
	}

	private Listener<String> request() {
		return new Listener<String>() {

			@Override
			public void onResponse(String response) {
				response = ParseResponse.parseResponse(response);
				view.dimiss();
				Logg.i("NetworkListPresenter-response", "" + response);
				if (!response.equals("")) {
					response = ParseResponse.parseResponse(response);
					Logg.i("NetworkListPresenter-response2", "response:"
							+ response);
					NetworkListResultTotalData data = GsonTools
							.changeGsonToBean(response,
									NetworkListResultTotalData.class);
					if (data != null) {
						Logg.i("datas", "" + data);
						List<NetworkData> results = data.getResults();
						view.setNetworkDatas(results);
					}
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
