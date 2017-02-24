package com.delta.smsandroidproject.presenter;

import android.content.Context;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.delta.smsandroidproject.EventView;
import com.delta.smsandroidproject.bean.ResultChargersData;
import com.delta.smsandroidproject.model.EventModelImp;
import com.delta.smsandroidproject.request.ParseResponse;
import com.delta.smsandroidproject.request.SessionTimeoutHandler;
import com.delta.smsandroidproject.util.Comment;
import com.delta.smsandroidproject.util.GsonTools;
import com.delta.smsandroidproject.util.Logg;

public class EventPresenter {

	private static final String TAG = "EventPresenter";
	private EventView view;
	private EventModelImp model;
	private Context context;

	public EventPresenter(EventView view, Context context) {
		this.view = view;
		this.model = new EventModelImp();
		this.context = context;
	}

	public void loadData(String url) {
		view.showDialog();
		model.loadData(listener, errorListener, url);
		Logg.i(TAG, "" + Comment.EVENT_URL + url);
	}

	private Listener<String> listener = new Listener<String>() {

		@Override
		public void onResponse(String response) {
			response = ParseResponse.parseResponse(response);
			Logg.i("EventPresenter--response", "" + response);
			view.dismiss();
			ResultChargersData list = GsonTools.changeGsonToBean(response,
					ResultChargersData.class);
			view.isLoadDataFinish(false);
			if (list != null) {
				view.setEventDatas(list.getChargers());
			} else {
				view.isLoadDataFinish(true);
			}
		}
	};

	private ErrorListener errorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			Logg.i("EventPresenter--error", "" + error.getMessage());
			view.dismiss();
			SessionTimeoutHandler.handSessionTimeout(context, error);
		}
	};
}
