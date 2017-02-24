package com.delta.smsandroidproject.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;

import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.dialog.ActionDialog;
import com.delta.smsandroidproject.util.WebsocketClient;
import com.delta.smsandroidproject.view.activity.MainActivity;
import com.delta.smsandroidproject.view.fragment.LocationFragment;

public class ActionReceiver extends BroadcastReceiver {

	public static final String ACTION = "com.sms.action.websocket";
	public static final String ERROR = "com.sms.action.error";

	public ActionReceiver() {
	}

	@SuppressLint("InflateParams")
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		String message = intent
				.getStringExtra(WebsocketClient.WEBSOCKET_MESSAGE);
		if (action.equals(ACTION)) {
			changeStatus(message);
		} else if (action.equals(ERROR)) {
			// showSnackBar(message);
		}
	}

	private void changeStatus(String info) {
		Log.i("changeStatus", "receive:" + info);
		if (!TextUtils.isEmpty(info) && info.contains("timeout")) {
			SMSApplication.getInstance().exit();
			return;
		}
		if (ActionDialog.instance != null && !TextUtils.isEmpty(info)) {
			ActionDialog.instance.setGrayByPushInfo(info);
		}

		if (LocationFragment.instance != null) {
			LocationFragment.instance.SocketUpdata(info);
		}
	}

	private void showSnackBar(String message) {

		if (message != null) {
			Snackbar.make(MainActivity.SnackBarContainer, message,
					Snackbar.LENGTH_SHORT).show();
		}
	}
}
