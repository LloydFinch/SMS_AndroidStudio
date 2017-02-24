package com.delta.smsandroidproject.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.view.activity.MainActivity;

/**
 * 按事件发生时间或事件严重程度排序
 * @author Jianzao.Zhang
 *
 */
public class SortTimeOrEventStatusDialog extends Dialog{

	private Context mContext;
	public SortTimeOrEventStatusDialog(Context context) {
		super(context);
		this.mContext = context;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initView();
	}
	private void initView() {
		
	}
	private void init() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_sort_time_or_event_status);
		Window window = this.getWindow();
		LayoutParams lp = window.getAttributes();
		WindowManager wm= ((MainActivity) mContext).getWindowManager();
		Display display = wm.getDefaultDisplay();
		lp.height = (int) (display.getHeight()*0.7);
		lp.width = (int) (display.getWidth()*0.8);
		window.setAttributes(lp);
	}
	
	

}
