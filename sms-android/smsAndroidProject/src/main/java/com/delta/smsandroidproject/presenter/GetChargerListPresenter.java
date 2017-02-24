package com.delta.smsandroidproject.presenter;

import java.util.Map;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.delta.smsandroidproject.bean.ChargerListData;
import com.delta.smsandroidproject.model.GetChargerListModel;
import com.delta.smsandroidproject.model.imodel.IModel;
import com.delta.smsandroidproject.presenter.ipresenter.IPresenter;
import com.delta.smsandroidproject.request.ParseResponse;
import com.delta.smsandroidproject.request.SessionTimeoutHandler;
import com.delta.smsandroidproject.util.GsonTools;
import com.delta.smsandroidproject.view.iview.ILoadView;

public class GetChargerListPresenter implements IPresenter {
	private IModel iModelList;
	private IGetChargerList iChargerlist;
	private ILoadView loadView;
	private Context context;

	public GetChargerListPresenter(IGetChargerList iChargerlist,
			ILoadView loadView, Context context) {
		this.iChargerlist = iChargerlist;
		this.loadView = loadView;
		iModelList = new GetChargerListModel();
		this.context = context;
	}

	@Override
	public void loadData(Map<String, String> map) {
		// TODO 自动生成的方法存根
		loadView.showLoading();
		iModelList.loadData(map, requestListener(), errListener());
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
				ChargerListData datas = GsonTools.changeGsonToBean(response,
						ChargerListData.class);
				if (datas != null) {
					iChargerlist.getChargerListSuccess(datas);
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
				iChargerlist.getChargerListFailed();
			}
		};

	}

	public interface IGetChargerList {
		void getChargerListSuccess(ChargerListData datas);

		void getChargerListFailed();
	}

}
