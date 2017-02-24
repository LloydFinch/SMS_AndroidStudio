package com.delta.smsandroidproject.dialog;

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
import android.widget.RadioButton;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.bean.EventListData;
import com.delta.smsandroidproject.util.Logg;
import com.delta.smsandroidproject.util.ToolUtil;

public class EvenlogOderbyDialog extends Dialog implements android.view.View.OnClickListener{

	private Context mContext;
	private RadioButton mRadio0;
	private RadioButton mRadio1;
	private RadioButton mRadio2;
	private RadioButton mRadio3;
	private RadioButton mRadio4;
	private RadioButtonCheckListener listener;
	private static EvenlogOderbyDialog dialog;
	private String oderby = EventListData.LEVEL;
	
	public EvenlogOderbyDialog(Context context) {
		super(context);
		this.mContext = context;
	}
	
//	public static EvenlogOderbyDialog getInstance(Context context){
//		if (dialog == null) {
//			dialog = new EvenlogOderbyDialog(context);
//		}
//		return dialog;
//		
//	}
	
	public interface RadioButtonCheckListener{
		public void onCheckListener(String oderby);
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
		setChecked();
		initListener();
	}
	private void initListener() {
		mRadio0.setOnClickListener(this);
		mRadio1.setOnClickListener(this);
		mRadio2.setOnClickListener(this);
		mRadio3.setOnClickListener(this);
		mRadio4.setOnClickListener(this);
	}

	private void initView() {
		mRadio0 = (RadioButton) findViewById(R.id.radio0);
		mRadio1 = (RadioButton) findViewById(R.id.radio1);
		mRadio2 = (RadioButton) findViewById(R.id.radio2);
		mRadio3 = (RadioButton) findViewById(R.id.radio3);
		mRadio4 = (RadioButton) findViewById(R.id.radio4);
		mRadio4.setVisibility(View.GONE);
		
	}

	private void setChecked(){
		String eventOderby = ToolUtil.getEventOderby();
		if (eventOderby.equals(EventListData.LEVEL)) {
			setLevelChecked();
		}
		if (eventOderby.equals(EventListData.OCCURRENCE)) {
			setOccurChecked();
		}
		if (eventOderby.equals(EventListData.CLEARANCE)) {
			setClearChecked();		
		}
		if (eventOderby.equals(EventListData.TITLE)) {
			setTitleChecked();
		}
	}
	
	private void setLevelChecked(){
		mRadio0.setChecked(true);
		mRadio1.setChecked(false);
		mRadio2.setChecked(false);
		mRadio3.setChecked(false);
		mRadio4.setChecked(false);
	}
	
	private void setOccurChecked(){
		mRadio0.setChecked(false);
		mRadio1.setChecked(true);
		mRadio2.setChecked(false);
		mRadio3.setChecked(false);
		mRadio4.setChecked(false);
	}
	
	private void setClearChecked(){
		mRadio0.setChecked(false);
		mRadio1.setChecked(false);
		mRadio2.setChecked(true);
		mRadio3.setChecked(false);
		mRadio4.setChecked(false);
	}
	
	private void setTitleChecked(){
		mRadio0.setChecked(false);
		mRadio1.setChecked(false);
		mRadio2.setChecked(false);
		mRadio3.setChecked(true);
		mRadio4.setChecked(false);
	}
	
	private void initData() {
		
	}

	/**
	 * 初始化设置
	 */
	private void init() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题栏
		setContentView(R.layout.dialog_event_log_oderby);
		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		// 设置dialog大小
		WindowManager windowManager = ((Activity) mContext).getWindowManager();    
        Display display = windowManager.getDefaultDisplay();
//		int h = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (display.getHeight() * 0.165), mContext.getResources().getDisplayMetrics());
//		int d = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (display.getWidth() * 0.3), mContext.getResources().getDisplayMetrics());
//		lp.height = h;
//		lp.width = d;
//		dialogWindow.setAttributes(lp);
        int height = 250;
		lp.height = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, height, mContext.getResources()
						.getDisplayMetrics());
		lp.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				300, mContext.getResources().getDisplayMetrics());
		lp.gravity = Gravity.CENTER;
		dialogWindow.setAttributes(lp);
	}
	private String switchClick(int v) {
		switch (v) {
		case R.id.radio0:
			Logg.i("radio0", "radio0");
			oderby = EventListData.LEVEL;
			ToolUtil.saveEventOderby(oderby);
			Logg.i("EvenlogOderbyDialog-oderby0", ""+oderby);
			listener.onCheckListener(oderby);
			dismiss();
			break;
		case R.id.radio1:
			Logg.i("radio1", "radio1");
			Logg.i("listener", ""+listener);
			
			oderby = EventListData.OCCURRENCE;
			ToolUtil.saveEventOderby(oderby);
			Logg.i("EvenlogOderbyDialog-oderby1", ""+oderby);
			listener.onCheckListener(oderby);
			dismiss();
			break;
		case R.id.radio2:
			
			oderby = EventListData.CLEARANCE;
			ToolUtil.saveEventOderby(oderby);
			Logg.i("EvenlogOderbyDialog-oderby2", ""+oderby);
			listener.onCheckListener(oderby);
			dismiss();
			break;
		case R.id.radio3:
			oderby = EventListData.TITLE;
			ToolUtil.saveEventOderby(oderby);
			Logg.i("EvenlogOderbyDialog-oderby3", ""+oderby);
			listener.onCheckListener(oderby);
			dismiss();
			break;
		case R.id.radio4:
			
			oderby = EventListData.EVENT_ID;
			ToolUtil.saveEventOderby(oderby);
			Logg.i("EvenlogOderbyDialog-oderby4", ""+oderby);
			listener.onCheckListener(oderby);
			dismiss();
			break;
		}
		return oderby;
	}

	public String getOderby(){
		oderby = ToolUtil.getEventOderby();
		return oderby;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switchClick(v.getId());
		
	}
}
