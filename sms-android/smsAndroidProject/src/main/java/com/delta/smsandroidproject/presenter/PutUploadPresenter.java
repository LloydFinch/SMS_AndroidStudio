package com.delta.smsandroidproject.presenter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.delta.smsandroidproject.model.UpLoadImgModel;
import com.delta.smsandroidproject.presenter.ipresenter.IPresenter;
import com.delta.smsandroidproject.request.ParseResponse;
import com.delta.smsandroidproject.request.SessionTimeoutHandler;
import com.delta.smsandroidproject.view.iview.ILoadView;
import com.delta.smsandroidproject.webrequest.RequestItem;
import com.delta.smsandroidproject.webrequest.ResponseListener;

public class PutUploadPresenter implements IPresenter {
	private UpLoadImg upLoadImg;
	private ILoadView iLoadView;
	private UpLoadImgModel upLoadImgModel;
	private Context context;

	public PutUploadPresenter(UpLoadImg upLoadImg, Context context) {
		this.upLoadImg = upLoadImg;
		upLoadImgModel = new UpLoadImgModel();
		this.context = context;
	}

	public PutUploadPresenter(UpLoadImg upLoadImg, ILoadView iLoadView,
			Context context) {
		this.iLoadView = iLoadView;
		this.upLoadImg = upLoadImg;
		upLoadImgModel = new UpLoadImgModel();
		this.context = context;
	}

	@Override
	public void loadData(Map<String, String> map) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void cancelAll() {
		// TODO 自动生成的方法存根

	}

	public void UpLoadImg(Bitmap bitmap, List<RequestItem> requestList,
			String url) {
		iLoadView.showLoading();
		upLoadImgModel.UpLoadImg(bitmap, Listener, requestList, url);
	}

	public interface UpLoadImg {
		void UpLoadSuccess();

		void UpLoadFailed();
	}

	private ResponseListener<String> Listener = new ResponseListener<String>() {
		public void onErrorResponse(com.android.volley.VolleyError error) {
			Log.e("失败", error.toString());
			iLoadView.dissLoading();
			SessionTimeoutHandler.handSessionTimeout(context, error);
			upLoadImg.UpLoadFailed();
		};

		public void onResponse(String response) {
			response = ParseResponse.parseResponse(response);
			Log.e("成功", response);
			upLoadImg.UpLoadSuccess();
			// iLoadView.dissLoading();
		};
	};
}
