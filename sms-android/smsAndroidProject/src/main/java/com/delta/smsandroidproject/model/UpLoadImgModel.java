package com.delta.smsandroidproject.model;

import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.delta.smsandroidproject.model.imodel.IModel;
import com.delta.smsandroidproject.webrequest.PutUploadRequest;
import com.delta.smsandroidproject.webrequest.RequestItem;
import com.delta.smsandroidproject.webrequest.ResponseListener;

public class UpLoadImgModel implements IModel{

	@Override
	public void loadData(Map<String, String> map,
			Listener<String> responseListener, ErrorListener errorLoginListener) {
		// TODO 自动生成的方法存根
		
	}
	public void UpLoadImg(Bitmap bitmap,ResponseListener listener,List<RequestItem> requestList,String url){
		PutUploadRequest.uploadImg(bitmap, listener, requestList,url);
	}
	@Override
	public void cancelAll() {
		// TODO 自动生成的方法存根
		
	}

}
