/**
 * 
 */
package com.delta.smsandroidproject.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Wenqi.Wang
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindViewTool {
	public int id();// view 的 id

	public boolean clickable() default false;// 是否可以点击，默认不可点击

	public boolean canTouch() default false;// 是否可以触摸，默认不可

	public boolean changeFont() default false;// 是否改变字体(设置给dialog使用)
}
