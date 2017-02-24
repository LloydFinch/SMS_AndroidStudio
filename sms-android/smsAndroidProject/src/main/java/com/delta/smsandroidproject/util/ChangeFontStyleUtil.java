/**
 * 
 */
package com.delta.smsandroidproject.util;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;

import com.delta.smsandroidproject.app.SMSApplication;

/**
 * @author Wenqi.Wang
 * 
 */
public class ChangeFontStyleUtil {

	private static AssetManager assetManager;
	private static Typeface typeface;

	private static void init() {
		if (assetManager == null) {
			assetManager = SMSApplication.getInstance().getApplicationContext()
					.getAssets();
		}
		if (typeface == null) {
			typeface = Typeface.createFromAsset(assetManager,
					"fonts/Roboto-Regular.ttf");
		}
	}

	public static void setFontStyle(TextView tv) {
		init();
		tv.setTypeface(typeface);
	}
}
