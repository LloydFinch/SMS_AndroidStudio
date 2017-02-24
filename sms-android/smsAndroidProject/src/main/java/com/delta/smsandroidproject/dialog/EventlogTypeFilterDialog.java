package com.delta.smsandroidproject.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.bean.EventListData;
import com.delta.smsandroidproject.util.Logg;
import com.delta.smsandroidproject.util.ToolUtil;

public class EventlogTypeFilterDialog extends Dialog implements android.view.View.OnClickListener, OnCheckedChangeListener{

	private static final String TAG = "EventlogTypeFilterDialog";
	private Context mContext;
	private CheckBox mCheck0;
	private CheckBox mCheck1;
	private CheckBox mCheck2;
	private CheckBox mCheck3;
	private CheckBox mCheck4;
	private RadioButtonCheckListener listener;
	private static EventlogTypeFilterDialog dialog;
	private String Type = EventListData.EMERGENCY;
	private List<String> typeList = new ArrayList<>();
	private Map<String, Boolean> typeAllMap = new HashMap<>();//保存每个位置的状态
	private CheckBox mCheck5;
	private TextView mTxOk;
	
	public EventlogTypeFilterDialog(Context context, List<String> typeList) {
		super(context);
		this.mContext = context;
		List<Boolean> eventFilter = ToolUtil.getEventFilter();
		if (eventFilter!=null && eventFilter.size()>0) {
			initTypeList(eventFilter.get(0), EventListData.EMERGENCY_LEVELID);
			initTypeList(eventFilter.get(1), EventListData.FAULT_LEVELID);
			initTypeList(eventFilter.get(2), EventListData.WARNING_LEVELID);
			initTypeList(eventFilter.get(3), EventListData.INFORMATION_LEVELID);
			initTypeList(eventFilter.get(4), EventListData.CONFIGURATION_LEVELID);
			initTypeList(eventFilter.get(5), EventListData.CHARGE_LEVELID);
		}else {
			initTypeList(true, EventListData.EMERGENCY_LEVELID);
			initTypeList(true, EventListData.FAULT_LEVELID);
			initTypeList(true, EventListData.WARNING_LEVELID);
			initTypeList(true, EventListData.INFORMATION_LEVELID);
			initTypeList(true, EventListData.CONFIGURATION_LEVELID);
			initTypeList(true, EventListData.CHARGE_LEVELID);
		}
//		this.typeList =typeList;
	}
	
	public interface RadioButtonCheckListener{
		public void onCheckListener(List<String> oderby);
	}
	
	public void registerOncheckListener(RadioButtonCheckListener listener){
		this.listener = listener;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initData();
		initView();
		initListener();
	}
	
	private void initListener() {
		mCheck0.setOnCheckedChangeListener(this);
		mCheck1.setOnCheckedChangeListener(this);
		mCheck2.setOnCheckedChangeListener(this);
		mCheck3.setOnCheckedChangeListener(this);
		mCheck4.setOnCheckedChangeListener(this);
		mCheck5.setOnCheckedChangeListener(this);
		mTxOk.setOnClickListener(this);
	}

	private void initView() {
		mCheck0 = (CheckBox) findViewById(R.id.radio0);
		mCheck1 = (CheckBox) findViewById(R.id.radio1);
		mCheck2 = (CheckBox) findViewById(R.id.radio2);
		mCheck3 = (CheckBox) findViewById(R.id.radio3);
		mCheck4 = (CheckBox) findViewById(R.id.radio4);
		mCheck5 = (CheckBox) findViewById(R.id.radio5);
		
		mTxOk = (TextView) findViewById(R.id.txOk);
		
		List<Boolean> eventFilter = ToolUtil.getEventFilter();
		if (eventFilter!=null && eventFilter.size()>0) {
			mCheck0.setChecked(eventFilter.get(0));
			mCheck1.setChecked(eventFilter.get(1));
			mCheck2.setChecked(eventFilter.get(2));
			mCheck3.setChecked(eventFilter.get(3));
			mCheck4.setChecked(eventFilter.get(4));
			mCheck5.setChecked(eventFilter.get(5));
		}else {
			mCheck0.setChecked(true);
			mCheck1.setChecked(true);
			mCheck2.setChecked(true);
			mCheck3.setChecked(true);
			mCheck4.setChecked(true);
			mCheck5.setChecked(true);
		}
		
	}

	private void initData() {
		
	}

	/**
	 * 初始化设置
	 */
	private void init() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题栏
		setContentView(R.layout.dialog_event_log_type_filter);
		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		// 设置dialog大小
		WindowManager windowManager = ((Activity) mContext).getWindowManager();    
        Display display = windowManager.getDefaultDisplay();
        int height = 450;
		lp.height = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, height, mContext.getResources()
						.getDisplayMetrics());
		lp.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				300, mContext.getResources().getDisplayMetrics());
		lp.gravity = Gravity.CENTER;
		dialogWindow.setAttributes(lp);
	}
	private void switchClick(int v,boolean isChecked) {
		switch (v) {
		case R.id.radio0:
			Logg.i("radio0", "radio0");
			
			Type = EventListData.EMERGENCY_LEVELID;
			Logg.i("EvenlogOderbyDialog-Type", ""+Type);
			getTypeList(isChecked, Type);
			break;
		case R.id.radio1:
			Logg.i("radio1", "radio1");
			Logg.i("listener", ""+listener);
			
			Type = EventListData.FAULT_LEVELID;
			Logg.i("EvenlogOderbyDialog-oderby1", ""+Type);
			getTypeList(isChecked,Type);
			break;
		case R.id.radio2:
			
			Type = EventListData.WARNING_LEVELID;
			Logg.i("EvenlogOderbyDialog-Type", ""+Type);
			getTypeList(isChecked, Type);
			break;
		case R.id.radio3:
			Type = EventListData.INFORMATION_LEVELID;
			Logg.i("EvenlogOderbyDialog-oderby3", ""+Type);
			getTypeList(isChecked, Type);
			break;
		case R.id.radio4:
			
			Type = EventListData.CONFIGURATION_LEVELID;
			Logg.i("EvenlogOderbyDialog-oderby4", ""+Type);
			getTypeList(isChecked, Type);
			break;
		case R.id.radio5:
			
			Type = EventListData.CHARGE_LEVELID;
			Logg.i("EvenlogOderbyDialog-oderby4", ""+Type);
			getTypeList(isChecked, Type);
			break;
		default:
			break;
		}
	}

	private void getTypeList(boolean isChecked, String type) {
		typeAllMap.put(type, isChecked);
		if (isChecked) {
			typeList.add(type);
		}else {
			if (typeList.size()>0) {
				typeList.remove(type);
			}
		}
		Logg.i(TAG+"typeList", ""+typeList);
	}
	private void initTypeList(boolean isChecked, String type) {
		typeAllMap.put(type, isChecked);
		if (isChecked) {
			typeList.add(type);
		}else {
			if (typeList.size()>0) {
				typeList.remove(type);
			}
		}
		Logg.i(TAG+"typeList", ""+typeList);
	}
	public List<String> getEvetType(){
		return typeList;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txOk:
			if (typeAllMap.size()>0) {
				ToolUtil.saveEventFilter(typeAllMap);
			}
			listener.onCheckListener(typeList);
			dismiss();
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switchClick(buttonView.getId(),isChecked);
	}
}
