package com.delta.smsandroidproject.presenter;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.delta.smsandroidproject.bean.ChargerLocationData;
import com.delta.smsandroidproject.bean.LocResultTotalData;
import com.delta.smsandroidproject.model.LocationListModel;
import com.delta.smsandroidproject.model.imodel.IModel;
import com.delta.smsandroidproject.presenter.ipresenter.IPresenter;
import com.delta.smsandroidproject.request.ParseResponse;
import com.delta.smsandroidproject.request.SessionTimeoutHandler;
import com.delta.smsandroidproject.util.GsonTools;
import com.delta.smsandroidproject.view.iview.ICF_getLocationList;
import com.delta.smsandroidproject.view.iview.ILoadView;

//liujiayu
public class CF_getLocationListPresenter implements IPresenter {
	private ICF_getLocationList iCF_getLocationList;
	private IModel imodel;
	private ILoadView loadView;
	private Context context;

	public CF_getLocationListPresenter(ICF_getLocationList iCF_getLocationList,
			ILoadView loadView, Context context) {
		this.iCF_getLocationList = iCF_getLocationList;
		this.loadView = loadView;
		imodel = new LocationListModel();
		this.context = context;
	}

	@Override
	public void loadData(Map<String, String> map) {
		// TODO 自动生成的方法存根
		loadView.showLoading();
		imodel.loadData(map, requestListener(), errListener());
	}

	public Listener<String> requestListener() {
		return new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				response = ParseResponse.parseResponse(response);
				loadView.dissLoading();
				// GsonTools.changeGsonToBean(response,
				// LocResultTotalData.class).getResults();
				Log.i("map location response", response);
				// ArrayList<ChargerLocationData> datas =
				// GsonTools.changeGsonToList(
				// response, ChargerLocationData.class);
				ArrayList<ChargerLocationData> datas;

				datas = (ArrayList<ChargerLocationData>) (GsonTools
						.changeGsonToBean(response, LocResultTotalData.class)
						.getResults());
				if (datas != null) {
					iCF_getLocationList.getLocationListSuccess(datas);
				}
			}
		};
	}

	public Response.ErrorListener errListener() {
		return new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				loadView.dissLoading();
				SessionTimeoutHandler.handSessionTimeout(context, error);
				iCF_getLocationList.getLocationListFailed();
			}
		};

	}

	@Override
	public void cancelAll() {
		// TODO 自动生成的方法存根
		imodel.cancelAll();
	}
}
