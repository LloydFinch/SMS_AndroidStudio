package com.delta.smsandroidproject.dialog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.delta.smsandroidproject.R;

public class GPSAlertDialog {

	private AlertDialog mDialog;

	public GPSAlertDialog(Context context,
			DialogInterface.OnClickListener onButtonSettingClickListener) {
		Builder builder = new Builder(context);
		builder.setMessage(context.getResources().getString(
				R.string.dialog_gps_alert));
		builder.setCancelable(true);
		mDialog = builder.create();
		mDialog.setButton(Dialog.BUTTON_POSITIVE, context.getResources()
				.getString(R.string.dialog_setting),
				onButtonSettingClickListener);
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
