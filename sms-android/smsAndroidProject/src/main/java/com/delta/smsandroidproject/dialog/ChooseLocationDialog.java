package com.delta.smsandroidproject.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.bean.ChargerLocationData;
import com.delta.smsandroidproject.view.ChargingLocMapView;
import com.delta.smsandroidproject.view.adapter.ChooseLocationAdapter;
import com.delta.smsandroidproject.view.adapter.ChooseLocationAdapter.RecycleviewOnItemClick;
import com.delta.smsandroidproject.widget.DividerItemDecoration;
/**
 * choose location dialog
 * @author Jianzao.Zhang
 *
 */
public class ChooseLocationDialog extends Dialog{

	private Context mContext;
	private List<ChargerLocationData> datas;
	private ChargingLocMapView chargingLocMapView;

	public ChooseLocationDialog(Context context, ChargingLocMapView chargingLocMapView) {
		super(context);
		this.mContext = context;
		this.chargingLocMapView = chargingLocMapView;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initData();
		initView();
		
	}
	
	/**
	 * 初始化设置
	 */
	private void init() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题栏
		setContentView(R.layout.dialog_choose_locationo);
		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		// 设置dialog大小
		WindowManager windowManager = ((Activity) mContext).getWindowManager();    
        Display display = windowManager.getDefaultDisplay();
//		lp.height = (int) (display.getHeight() * 0.7);
//		lp.width = (int) (display.getWidth() * 0.8);
//        int h = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (display.getHeight() * 0.12), mContext.getResources().getDisplayMetrics());
//		int d = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (display.getWidth() * 0.3), mContext.getResources().getDisplayMetrics());
//		lp.height = h;
//		lp.width = d;
//		dialogWindow.setAttributes(lp);
        int height = 300;
		lp.height = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, height, mContext.getResources()
						.getDisplayMetrics());
		lp.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				300, mContext.getResources().getDisplayMetrics());
		lp.gravity = Gravity.CENTER;
		dialogWindow.setAttributes(lp);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		datas = chargingLocMapView.getLocationDatas();
		if (datas == null) {
			datas = new ArrayList<ChargerLocationData>();
		}
		
	}
	
	/**
	 * 初始化布局
	 */
	private void initView() {
		RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.listView);
		mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,LinearLayoutManager.VERTICAL));
		ChooseLocationAdapter mAdapter = new ChooseLocationAdapter(mContext, datas,chargingLocMapView);
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
		mAdapter.setOnItemClick(new RecycleviewOnItemClick() {
			
			@Override
			public void onItemClick(View v, ChargerLocationData data) {
				chargingLocMapView.setCurrentLocation(data);
				dismiss();
			}
		});
	}

}
