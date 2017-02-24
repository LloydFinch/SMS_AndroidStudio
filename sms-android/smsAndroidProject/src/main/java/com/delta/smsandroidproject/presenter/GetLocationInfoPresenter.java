package com.delta.smsandroidproject.presenter;

import java.util.Map;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.delta.smsandroidproject.bean.LocationInfoData;
import com.delta.smsandroidproject.model.GetLocationInfoModel;
import com.delta.smsandroidproject.model.imodel.IModel;
import com.delta.smsandroidproject.presenter.ipresenter.IPresenter;
import com.delta.smsandroidproject.request.ParseResponse;
import com.delta.smsandroidproject.request.SessionTimeoutHandler;
import com.delta.smsandroidproject.util.GsonTools;

public class GetLocationInfoPresenter implements IPresenter {
	private getLocationInfo info;
	private IModel iModel;
	private Context context;

	public GetLocationInfoPresenter(getLocationInfo info, Context context) {
		this.info = info;
		iModel = new GetLocationInfoModel();
		this.context = context;
	}

	@Override
	public void loadData(Map<String, String> map) {
		// TODO 自动生成的方法存根LocationInfoData
		iModel.loadData(map, requestListener(), errListener());
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
				LocationInfoData datas = GsonTools.changeGsonToBean(response,
						LocationInfoData.class);
				if (datas != null) {
					info.getLocationInfoSuccess(datas);
				}
			}
		};
	}

	public Response.ErrorListener errListener() {
		return new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				SessionTimeoutHandler.handSessionTimeout(context, error);
				info.getLocationInfoFailed();
			}
		};

	}

	public interface getLocationInfo {
		void getLocationInfoSuccess(LocationInfoData datas);

		void getLocationInfoFailed();
	}
}
