/**
 * 
 */
package com.delta.smsandroidproject.view;

import java.lang.reflect.Field;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.delta.smsandroidproject.util.ChangeFontStyleUtil;

/**
 * @author Wenqi.Wang 利用注解加反射初始化view
 */
public final class ViewInitHelper {
	public static void initView(Object viewClass, android.view.View view) {
		Field[] fields = viewClass.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				BindViewTool bindView = field.getAnnotation(BindViewTool.class);
				if (bindView != null) {
					int id = bindView.id();
					boolean clickable = bindView.clickable();
					try {
						field.setAccessible(true);
						View itemView = view.findViewById(id);
						if (clickable) {
							itemView.setOnClickListener((OnClickListener) viewClass);
						}
						boolean changeFont = bindView.changeFont();
						if (changeFont && itemView instanceof TextView) {
							ChangeFontStyleUtil
									.setFontStyle((TextView) itemView);
						}
						field.set(viewClass, itemView);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
