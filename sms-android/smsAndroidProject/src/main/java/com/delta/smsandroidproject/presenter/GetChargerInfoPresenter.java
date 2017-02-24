package com.delta.smsandroidproject.presenter;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.delta.smsandroidproject.bean.ChLisResultData;
import com.delta.smsandroidproject.bean.ChargerInfoData;
import com.delta.smsandroidproject.model.GetChargerInfo;
import com.delta.smsandroidproject.model.imodel.IModel;
import com.delta.smsandroidproject.presenter.ipresenter.IPresenter;
import com.delta.smsandroidproject.request.ParseResponse;
import com.delta.smsandroidproject.request.SessionTimeoutHandler;
import com.delta.smsandroidproject.util.GsonTools;
import com.delta.smsandroidproject.view.iview.ILoadView;

public class GetChargerInfoPresenter implements IPresenter {
	private IGetChargerInfo iGetChargerInfo;
	private IModel imodel;
	private ILoadView loadView;
	private Context context;

	public GetChargerInfoPresenter(IGetChargerInfo iGetChargerInfo,
			ILoadView loadView, Context context) {
		this.iGetChargerInfo = iGetChargerInfo;
		imodel = new GetChargerInfo();
		this.loadView = loadView;
		this.context = context;
	}

	@Override
	public void loadData(Map<String, String> map) {
		// TODO 自动生成的方法存根
		loadView.showLoading();
		imodel.loadData(map, requestListener(), errListener());
	}

	@Override
	public void cancelAll() {
		// TODO 自动生成的方法存根

	}

	public Listener<String> requestListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				response = ParseResponse.parseResponse(response);
				ArrayList<ChargerInfoData> datas;
				ChargerInfoData d;
				// ChLisResultData<ChargerInfoData> ds;
				// ArrayList<ChargerInfoData> datas =
				// GsonTools.changeGsonToList(
				// response, ChargerInfoData.class);
				datas = GsonTools.changeGsonToBean(response,
						ChLisResultData.class).getResult();
				if (datas != null) {
					iGetChargerInfo.getChargerInfoSuccess(datas);
				}
				loadView.dissLoading();
			}
		};
	}

	public Response.ErrorListener errListener() {
		return new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				loadView.dissLoading();
				SessionTimeoutHandler.handSessionTimeout(context, error);
				iGetChargerInfo.getChargerInfoFailed();

			}
		};

	}

	public interface IGetChargerInfo {
		void getChargerInfoSuccess(ArrayList<ChargerInfoData> datas);

		void getChargerInfoFailed();
	}
}
