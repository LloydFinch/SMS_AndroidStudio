package com.delta.smsandroidproject.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public final class SetImageUtils {
	public static void setImage(ImageView imageView, Bitmap bitmap) {
		if (imageView != null && bitmap != null) {
			imageView.setImageBitmap(bitmap);
		}
	}

	public static void setImage(ImageView imageView, Drawable drawable) {
		if (imageView != null && drawable != null) {
			imageView.setImageDrawable(drawable);
		}
	}
}
