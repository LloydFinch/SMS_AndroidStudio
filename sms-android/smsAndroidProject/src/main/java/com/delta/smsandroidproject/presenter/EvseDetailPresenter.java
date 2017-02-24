package com.delta.smsandroidproject.presenter;

import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.delta.smsandroidproject.bean.EvseDetailData;
import com.delta.smsandroidproject.model.EvseDetailModel;
import com.delta.smsandroidproject.model.imodel.IModel;
import com.delta.smsandroidproject.presenter.ipresenter.IPresenter;
import com.delta.smsandroidproject.util.GsonTools;
import com.delta.smsandroidproject.view.iview.ILoadView;

public class EvseDetailPresenter implements IPresenter {
	private Object iInterface;
	private Context context;
	private ILoadView iLoadView;
	private IModel iModel;
	private EvseDetail mCallBack;

	public EvseDetailPresenter() {

	}

	public EvseDetailPresenter(Context context, Object iInterface) {
		this.context = context;
		this.iInterface = iInterface;
		iModel = new EvseDetailModel();
		if (this.iInterface instanceof ILoadView) {
			iLoadView = (ILoadView) iInterface;
		}
		if (this.iInterface instanceof EvseDetail) {
			mCallBack = (EvseDetail) iInterface;
		}
	}

	@Override
	public void loadData(Map<String, String> map) {
		if (iLoadView != null) {
			iLoadView.showLoading();
		}
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
				Log.e("显示返回数据", response);
				if (!TextUtils.isEmpty(response)) {
					EvseDetailData datas = GsonTools.changeGsonToBean(response,
							EvseDetailData.class);
					mCallBack.getEvseDetailSuc(datas);
				} else {
					mCallBack.getEvseDetailFai(null);
				}
				if (iLoadView != null) {
					iLoadView.dissLoading();
				}

			}
		};
	}

	public Response.ErrorListener errListener() {
		return new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				mCallBack.getEvseDetailFai(error);
				if (iLoadView != null) {
					iLoadView.dissLoading();
				}
			}
		};

	}

	public interface EvseDetail {
		void getEvseDetailSuc(EvseDetailData datas);

		void getEvseDetailFai(VolleyError error);
	}
}
