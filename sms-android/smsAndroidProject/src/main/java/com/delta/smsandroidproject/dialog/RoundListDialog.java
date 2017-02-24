package com.delta.smsandroidproject.dialog;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.bean.ChargerLocationData;
import com.delta.smsandroidproject.view.adapter.ListDialogAdapter;
import com.delta.smsandroidproject.widget.DividerItemDecoration;

public class RoundListDialog extends Dialog {

	private Context mContext;
	private List<ChargerLocationData> datas;

	public RoundListDialog(Context context, View layout, int style,
			List<ChargerLocationData> datas) {
		this(context, style, datas);
	}

	public RoundListDialog(Context context, int style,
			List<ChargerLocationData> datas) {
		super(context, style);
		this.datas = datas;
		this.mContext = context;
		init();
	}

	@SuppressWarnings("deprecation")
	private void init() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_choose_locationo);
		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		WindowManager windowManager = ((Activity) mContext).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		lp.height = (int) (display.getHeight() * 0.7);
		lp.width = (int) (display.getWidth() * 0.8);
		lp.gravity = Gravity.CENTER;
		dialogWindow.setAttributes(lp);

		RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.listView);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,
				DividerItemDecoration.VERTICAL_LIST));
		StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
				1, LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(layoutManager);

		ListDialogAdapter adapter = new ListDialogAdapter(mContext, datas, this);
		mRecyclerView.setAdapter(adapter);
	}
}
