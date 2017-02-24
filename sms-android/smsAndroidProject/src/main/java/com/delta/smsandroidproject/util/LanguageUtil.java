package com.delta.smsandroidproject.util;

import java.util.Locale;

import android.content.Context;
import android.content.res.Configuration;

public class LanguageUtil {
	private static void initLanguage(Context context,String languageToLoad) {
		Locale locale = new Locale(languageToLoad);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
	}
	public static void setLanguage(Context context,int languageId){
		String language = "en";
		switch (languageId) {
		case 0:
			language = "en";//English
			break;
		case 1:
			language = "zh";//中文
			break;
		case 2:
			language = "fr";//french
			break;
		case 3:
			language = "de";//German
			break;
		case 4:
			language = "sv";//Swedish
			break;
		case 5:
			language = "fi";//Finnish
			break;
		case 6:
			language = "ru";//Russian
			break;
		case 7:
			language = "ja";//Russian
			break;
		default:
			break;
		}
		initLanguage(context, language);
		ToolUtil.saveSysLanguage(language);
	}
}
