package com.delta.smsandroidproject.util;

import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class SetTextUtils {
	public static void setText(TextView textView, CharSequence content) {
		if (textView != null && content != null) {
			textView.setText(content);
		}
	}

	public static void setText(TextView textView, CharSequence content,
			LinearLayout container) {
		if (textView != null && !TextUtils.isEmpty(content)
				&& container != null) {
			container.setVisibility(View.VISIBLE);
			textView.setText(content);
		} else {
			container.setVisibility(View.GONE);
		}
	}

	public static void setText(TextView textView, int content) {
		if (textView != null) {
			textView.setText(String.valueOf(content));
		}
	}

	public static void setText(TextView textView, Spanned content) {
		if (textView != null && content != null) {
			textView.setText(content);
		}
	}

}
