package com.delta.smsandroidproject.presenter;

import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.delta.smsandroidproject.bean.EvseDetailData;
import com.delta.smsandroidproject.model.ChargerDetailModel;
import com.delta.smsandroidproject.model.imodel.IModel;
import com.delta.smsandroidproject.presenter.ipresenter.IPresenter;
import com.delta.smsandroidproject.util.GsonTools;
import com.delta.smsandroidproject.view.iview.ILoadView;

public class ChargerDetailPresenter implements IPresenter {
	private IModel iModel;
	private ChargerDetail mInterface;
	private ILoadView iLoadView;
	private Context context;

	public ChargerDetailPresenter() {
		iModel = new ChargerDetailModel();
	}

	public ChargerDetailPresenter(Context context,Object mObject) {
		if (mObject instanceof ILoadView) {
			this.iLoadView = (ILoadView) mObject;
		}
		if (mObject instanceof ChargerDetail) {
			this.mInterface = (ChargerDetail) mObject;
		}
		this.context = context;
		iModel = new ChargerDetailModel();
	}

	@Override
	public void loadData(Map<String, String> map) {
		// TODO 自动生成的方法存根
		iModel.loadData(map, requestListener(), errListener());
		if (iLoadView!=null) {
			iLoadView.showLoading();
		}
	}

	@Override
	public void cancelAll() {
		// TODO 自动生成的方法存根

	}

	public Listener<String> requestListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("ChargerDetail 详细信息", response);
				if (TextUtils.isEmpty(response)) {
					mInterface.getChDetailFia(null);
				}else{
					EvseDetailData datas = GsonTools.changeGsonToBean(response,
							EvseDetailData.class);
					mInterface.getChDetailSuc(datas);
				}
				if (iLoadView!=null) {
					iLoadView.dissLoading();
				}
			}
		};
	}

	public Response.ErrorListener errListener() {
		return new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				mInterface.getChDetailFia(error);
				if (iLoadView!=null) {
					iLoadView.dissLoading();
				}
			}
		};

	}

	public interface ChargerDetail {
		void getChDetailSuc(EvseDetailData datas);

		void getChDetailFia(VolleyError error);
	}
}
