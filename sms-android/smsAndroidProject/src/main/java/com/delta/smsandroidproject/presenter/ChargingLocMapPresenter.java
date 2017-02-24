package com.delta.smsandroidproject.presenter;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.bean.LocResultTotalData;
import com.delta.smsandroidproject.model.ChargingLocModelImp;
import com.delta.smsandroidproject.request.ParseResponse;
import com.delta.smsandroidproject.request.SessionTimeoutHandler;
import com.delta.smsandroidproject.util.Comment;
import com.delta.smsandroidproject.util.GsonTools;
import com.delta.smsandroidproject.util.NetConnectTools;
import com.delta.smsandroidproject.view.ChargingLocMapView;

public class ChargingLocMapPresenter {

	private ChargingLocMapView chargingLocMapView;
	private ChargingLocModelImp ChargingLocModel;
	private Context context;

	public ChargingLocMapPresenter(ChargingLocMapView chargingLocMapView,
			Context context) {
		this.context = context;
		this.chargingLocMapView = chargingLocMapView;
		this.ChargingLocModel = new ChargingLocModelImp();
	}

	public void getLocations(String networkId) {
		if (NetConnectTools.isNetworkAvailable(SMSApplication.getInstance()
				.getApplicationContext())) {
			chargingLocMapView.dismiss();
			chargingLocMapView.showDialog();
			ChargingLocModel.loadData(requestListener(), errListener(),
					Comment.MAP_LCOATION_URL + networkId);
		} else {
			chargingLocMapView.showNetAlertDialog();
		}
	}

	public Listener<String> requestListener() {
		return new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				response = ParseResponse.parseResponse(response);
				Log.i("map location response", response);
				chargingLocMapView.dismiss();
				LocResultTotalData datas = GsonTools.changeGsonToBean(response,
						LocResultTotalData.class);
				if (datas != null) {
					Log.i("ChargingLocMapPresenter datas", "" + datas);
					chargingLocMapView.setLocMarkToMap(datas.getResults());
				}
			}
		};
	}

	public Response.ErrorListener errListener() {
		return new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("map location request failed", "" + error.getMessage());
				chargingLocMapView.dismiss();
				SessionTimeoutHandler.handSessionTimeout(context, error);
			}
		};

	}
}
