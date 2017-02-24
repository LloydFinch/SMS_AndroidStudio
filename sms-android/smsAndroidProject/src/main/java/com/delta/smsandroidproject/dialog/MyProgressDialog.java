package com.delta.smsandroidproject.dialog;

import android.app.ProgressDialog;
import android.content.Context;

public class MyProgressDialog {

	private ProgressDialog dialog;

	public MyProgressDialog(Context context) {
		dialog = new ProgressDialog(context);
		dialog.setCancelable(false);
	}

	public void show(String message) {
		dialog.setMessage(message);
		dialog.show();
	}

	public void dismiss() {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}
}
