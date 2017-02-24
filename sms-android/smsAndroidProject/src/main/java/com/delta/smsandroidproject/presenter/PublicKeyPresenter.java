package com.delta.smsandroidproject.presenter;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.bean.PublicKeyData;
import com.delta.smsandroidproject.model.PublicKeyModel;
import com.delta.smsandroidproject.model.PublicKeyModelImp;
import com.delta.smsandroidproject.request.ParseResponse;
import com.delta.smsandroidproject.util.Logg;
import com.delta.smsandroidproject.util.NetConnectTools;
import com.delta.smsandroidproject.view.PublicKeyView;
import com.google.gson.Gson;

/**
 * 获取公钥接口
 * 
 * @author Jianzao.Zhang
 * 
 */
public class PublicKeyPresenter {
	private static final String TAG = "PublicKeyPresenter";
	private PublicKeyModel model;
	private PublicKeyView view;
	private Context context;

	public PublicKeyPresenter(PublicKeyView view, Context context) {
		this.view = view;
		model = new PublicKeyModelImp();
		this.context = context;
	}

	public void loadData(String url) {
		if (NetConnectTools.isNetworkAvailable(SMSApplication.getInstance()
				.getApplicationContext())) {
			Logg.i(TAG, "url-" + url);
			view.dismiss();
			model.loadData(listener(), errorListener(), url);
			view.showDialog();
		} else {
			view.showNetAlertDialog();
		}

	}

	private ErrorListener errorListener() {
		return new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Logg.i(TAG, "error-" + error.getMessage());
				view.dismiss();
				Toast.makeText(
						context,
						context.getResources().getString(
								R.string.dialog_net_alert), Toast.LENGTH_SHORT)
						.show();
			}
		};
	}

	private Listener<String> listener() {
		return new Listener<String>() {

			@Override
			public void onResponse(String response) {
				response = ParseResponse.parseResponse(response);
				Logg.i(TAG, "response-" + response);
				view.dismiss();
				if (response != null && !response.isEmpty()) {
					Gson gson = new Gson();
					PublicKeyData keyData = gson.fromJson(response,
							PublicKeyData.class);
					if (keyData != null) {
						Logg.i(TAG, "keyData-" + keyData);
						view.setPublicKey(keyData);
					}
				}
			}
		};
	}
}
