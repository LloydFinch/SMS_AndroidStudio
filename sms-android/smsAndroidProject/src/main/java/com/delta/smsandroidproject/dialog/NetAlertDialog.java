package com.delta.smsandroidproject.dialog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.delta.smsandroidproject.R;

public class NetAlertDialog {

	private AlertDialog mDialog;

	public NetAlertDialog(Context context,
			DialogInterface.OnClickListener onButtonRetryClickListener) {
		Builder builder = new Builder(context);
		builder.setMessage(context.getResources().getString(
				R.string.dialog_net_alert));
		builder.setCancelable(true);
		mDialog = builder.create();
		mDialog.setButton(Dialog.BUTTON_POSITIVE, context.getResources()
				.getString(R.string.dialog_retry), onButtonRetryClickListener);
	}

	public void show() {
		if (mDialog != null && !mDialog.isShowing()) {
			mDialog.show();
		}
	}

	public void dismiss() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}
}
