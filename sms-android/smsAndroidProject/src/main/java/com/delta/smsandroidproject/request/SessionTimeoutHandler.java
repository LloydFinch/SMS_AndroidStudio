package com.delta.smsandroidproject.request;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.android.volley.VolleyError;
import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.view.activity.LoginActivity;

/**
 * @author Wenqi.Wang
 * 
 */
public class SessionTimeoutHandler {
	public static boolean handSessionTimeout(final Context mContext,
			VolleyError error) {
		if (error != null && error.networkResponse != null
				&& error.networkResponse.statusCode == 444) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setCancelable(false);
			builder.setMessage(mContext.getString(R.string.login_again));
			AlertDialog dialog = builder.create();
			dialog.setButton(Dialog.BUTTON_POSITIVE, "OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							logout(mContext);
						}
					});
			dialog.show();
			return true;
		}
		return false;
	}

	private static void logout(Context context) {
		SMSApplication.getInstance().clearAllActivity();
		context.startActivity(new Intent(context, LoginActivity.class));
	}
}
