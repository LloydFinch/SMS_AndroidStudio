package com.delta.smsandroidproject.popupview;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.presenter.GetLocationInfoPresenter;
import com.delta.smsandroidproject.util.Logg;
import com.delta.smsandroidproject.util.TimeUtil;
import com.delta.smsandroidproject.util.ToastCustom;
import com.delta.smsandroidproject.util.ToolUtil;

/**
 * 在charging location map界面下点击location出现的布局
 * 
 * @author Jianzao.Zhang
 * 
 */
public class EventLogTimePopuView implements OnClickListener {

	public static final String ALL = "ALL";
	private static final String TEXT_NULL = "------------------";
	private static final String TAG = "EventLogTimePopuView";
	private Context mContext;
	private PopupWindow mPopupWindow;
	private FrameLayout layout;
	private ClickListener listener;
	private GetLocationInfoPresenter presenter;
	private TextView mTxToday;
	private TextView mTx7days;
	private TextView mTx30days;
	private TextView mTxAllTime;
	private TextView mTxCustomRange;
	private TextView mTxFrom;
	private TextView mTxTo;
	private DatePickerDialog datePickerDialog;
	private TextView mTxOk;
	private ImageView mImgToday;
	private ImageView mImg7days;
	private ImageView mImg30days;
	private ImageView mImgAlltime;
	private ImageView mImgCustom;
	private ImageView mImgFrom;
	private ImageView mImgTo;
	private LinearLayout mLayoutToday;
	private LinearLayout mLayout7days;
	private LinearLayout mLayout30days;
	private LinearLayout mLayoutAlltime;
	private LinearLayout mLayoutCustom;
	private LinearLayout mLayoutFrom;
	private LinearLayout mLayoutTo;
	private int pos = 0;//位置
	private LinearLayout mPopLayout;
	public interface ClickListener {
		public void onClick(String startTime,String endTime);
	}

	public EventLogTimePopuView(Context context, FrameLayout layout,ClickListener listener) {
		this.mContext = context;
		this.layout = layout;
		this.listener = listener;
		init();
	}

	private void init() {
		WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
//		int height = wm.getDefaultDisplay().getHeight();
		int height = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 270, mContext.getResources()
						.getDisplayMetrics());

		View inflate = LayoutInflater.from(mContext).inflate(
				R.layout.item_event_time_popview, null);
		mPopupWindow = new PopupWindow(inflate, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		// 解决点击外部，popupview消失
		mPopupWindow.setOutsideTouchable(true);//
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

		mPopLayout = (LinearLayout) inflate.findViewById(R.id.popLayout);
		mLayoutToday = (LinearLayout) inflate.findViewById(R.id.layoutToday);
		mLayout7days = (LinearLayout) inflate.findViewById(R.id.layout7days);
		mLayout30days = (LinearLayout) inflate.findViewById(R.id.layout30days);
		mLayoutAlltime = (LinearLayout) inflate.findViewById(R.id.layoutAlltime);
		mLayoutCustom = (LinearLayout) inflate.findViewById(R.id.layoutCustom);
		mLayoutFrom = (LinearLayout) inflate.findViewById(R.id.layoutFrom);
		mLayoutTo = (LinearLayout) inflate.findViewById(R.id.layoutTo);
		
		mTxOk = (TextView) inflate.findViewById(R.id.txOk);
		mTxToday = (TextView) inflate.findViewById(R.id.txToday);
		mTx7days = (TextView) inflate.findViewById(R.id.tx7days);
		mTx30days = (TextView) inflate.findViewById(R.id.tx30days);
		mTxAllTime = (TextView) inflate.findViewById(R.id.txAllTime);
		mTxCustomRange = (TextView) inflate.findViewById(R.id.txCustomRange);
		mTxFrom = (TextView) inflate.findViewById(R.id.txFrom);
		mTxTo = (TextView) inflate.findViewById(R.id.txTo);
		mImgToday = (ImageView) inflate.findViewById(R.id.imgToday);
		mImg7days = (ImageView) inflate.findViewById(R.id.img7days);
		mImg30days = (ImageView) inflate.findViewById(R.id.img30days);
		mImgAlltime = (ImageView) inflate.findViewById(R.id.imgAlltime);
		mImgCustom = (ImageView) inflate.findViewById(R.id.imgCustom);
		mImgFrom = (ImageView) inflate.findViewById(R.id.imgFrom);
		mImgTo = (ImageView) inflate.findViewById(R.id.imgTo);
		
		rememberFilterPos();
		initListener();
	}

	/**
	 * 记住上次的要进行筛选时间的选项
	 */
	private void rememberFilterPos() {
		int filterPos = ToolUtil.getFilterPos();
		if (filterPos == 0) {
			setTodayVisible();
		}
		if (filterPos == 1) {
			set7daysVisible();
		}
		if (filterPos == 2) {
			set30daysVisible();
		}
		if (filterPos == 3) {
			setAlltimeVisible();
		}
		if (filterPos == 4) {
			setAlltimeVisible();
		}
		if (filterPos == 5) {
			if (!ToolUtil.getFilterStartTime().isEmpty()&&!ToolUtil.getFilterEndTime().isEmpty()) {
				mTxFrom.setText(ToolUtil.getFilterStartTime());
				mTxTo.setText(ToolUtil.getFilterEndTime());
				setFromToEnabled(true);
				mImgToday.setVisibility(View.INVISIBLE);
				mImg7days.setVisibility(View.INVISIBLE);
				mImg30days.setVisibility(View.INVISIBLE);
				mImgAlltime.setVisibility(View.INVISIBLE);
				mImgCustom.setVisibility(View.VISIBLE);
			}else {
				setCustomVisible();
			}
		}
	}

	private void initListener() {
		
		mLayoutToday.setOnClickListener(this);
		mLayout7days.setOnClickListener(this);
		mLayout30days.setOnClickListener(this);
		mLayoutAlltime.setOnClickListener(this);
		mLayoutCustom.setOnClickListener(this);
		mLayoutFrom.setOnClickListener(this);
		mLayoutTo.setOnClickListener(this);
		mTxOk.setOnClickListener(this);
		mPopLayout.setOnClickListener(this);
	}

	public void showPopViewWindow() {
		if (mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		} else {
			int[] loc = new int[2];
			layout.getLocationOnScreen(loc);
			// 第一个参数指定PopupWindow的锚点view，即依附在哪个view上。
			// 第二个参数指定起始点为parent的右下角，第三个参数设置以parent的右下角为原点，向左、上各偏移10像素。
			mPopupWindow.showAtLocation(layout, Gravity.BOTTOM, 0, 0);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.popLayout:
			showPopViewWindow();
			break;
		case R.id.layoutToday:
			pos = 0;
			setTodayVisible();
			break;
		case R.id.layout7days:
			pos = 1;
			set7daysVisible();
			break;
		case R.id.layout30days:
			pos = 2;
			set30daysVisible();
			break;
		case R.id.layoutAlltime:
			pos = 3;
			setAlltimeVisible();
			break;
		case R.id.layoutCustom:
			pos = 4;
			setCustomVisible();
			break;
		case R.id.layoutFrom:
			pos = 5;
			setTime(mTxFrom);
			break;
		case R.id.layoutTo:
			pos = 6;
			setTime(mTxTo);
			break;
		case R.id.txOk:
			if (pos == 3) {
				ToolUtil.saveFilterTime(ALL,ALL,pos);
				listener.onClick(ALL,ALL);
				return;
			}
			String startTime = mTxFrom.getText().toString().trim();
			String endTime = mTxTo.getText().toString().trim();
			if (startTime.compareTo(endTime)<=0&&startTime.compareTo(setTextToday(null))<=0&&endTime.compareTo(setTextToday(null))<=0) {
				ToolUtil.saveFilterTime(startTime,endTime,pos);
				listener.onClick(startTime,endTime);
			}else {
				ToastCustom.showToast(mContext, mContext.getResources().getString(R.string.time_error), ToastCustom.LENGTH_SHORT);
			}
			break;
		default:
			break;
		}
	}
	
	private void compare(){
		
	}
	
	private void setFromToEnabled(boolean isVisible){
		mLayoutFrom.setEnabled(isVisible);
		mLayoutTo.setEnabled(isVisible);
		mImgFrom.setVisibility(View.VISIBLE);
		mImgTo.setVisibility(View.VISIBLE);
	}

	private void setTodayVisible() {
		setTextToday(mTxFrom);
		setTextToday(mTxTo);
		setFromToEnabled(false);
		mImgFrom.setVisibility(View.INVISIBLE);
		mImgTo.setVisibility(View.INVISIBLE);
		mImgToday.setVisibility(View.VISIBLE);
		mImg7days.setVisibility(View.INVISIBLE);
		mImg30days.setVisibility(View.INVISIBLE);
		mImgAlltime.setVisibility(View.INVISIBLE);
		mImgCustom.setVisibility(View.INVISIBLE);
	}
	private void set7daysVisible() {
		setTextdaysBeforeFromTo(mTxFrom,7);
		setTextToday(mTxTo);
		setFromToEnabled(false);
		mImgFrom.setVisibility(View.INVISIBLE);
		mImgTo.setVisibility(View.INVISIBLE);
		mImgToday.setVisibility(View.INVISIBLE);
		mImg7days.setVisibility(View.VISIBLE);
		mImg30days.setVisibility(View.INVISIBLE);
		mImgAlltime.setVisibility(View.INVISIBLE);
		mImgCustom.setVisibility(View.INVISIBLE);
	}
	private void set30daysVisible() {
		setTextdaysBeforeFromTo(mTxFrom,30);
		setTextToday(mTxTo);
		setFromToEnabled(false);
		mImgFrom.setVisibility(View.INVISIBLE);
		mImgTo.setVisibility(View.INVISIBLE);
		mImgToday.setVisibility(View.INVISIBLE);
		mImg7days.setVisibility(View.INVISIBLE);
		mImg30days.setVisibility(View.VISIBLE);
		mImgAlltime.setVisibility(View.INVISIBLE);
		mImgCustom.setVisibility(View.INVISIBLE);
	}
	private void setAlltimeVisible() {
//		setTextToday(mTxFrom);
//		setTextToday(mTxTo);
		mTxFrom.setText(TEXT_NULL);
		mTxTo.setText(TEXT_NULL);
		setFromToEnabled(false);
		mImgFrom.setVisibility(View.INVISIBLE);
		mImgTo.setVisibility(View.INVISIBLE);
		mImgToday.setVisibility(View.INVISIBLE);
		mImg7days.setVisibility(View.INVISIBLE);
		mImg30days.setVisibility(View.INVISIBLE);
		mImgAlltime.setVisibility(View.VISIBLE);
		mImgCustom.setVisibility(View.INVISIBLE);
	}
	private void setCustomVisible() {
		setTextToday(mTxFrom);
		setTextToday(mTxTo);
		setFromToEnabled(true);
		mImgToday.setVisibility(View.INVISIBLE);
		mImg7days.setVisibility(View.INVISIBLE);
		mImg30days.setVisibility(View.INVISIBLE);
		mImgAlltime.setVisibility(View.INVISIBLE);
		mImgCustom.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 设置时间
	 * @param tx
	 */
	private void setTime(final TextView tx) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int monthOfYear = calendar.get(Calendar.MONTH);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);	
		new DatePickerDialog(mContext, new OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				int month = monthOfYear+1;
				String monthString = ""+month;
				if (monthOfYear<10) {
					monthString = "0"+month;
				}
				String day = dayOfMonth+"";
				if (dayOfMonth<10) {
					day = "0"+day;
				}
				StringBuffer sBuffer = new StringBuffer();
				String string = sBuffer.append(year)
				.append("-")
				.append(monthString)
				.append("-")
				.append(day).toString();
				tx.setText(string);
			}
		}, year, monthOfYear, dayOfMonth).show();
	}
	
	/**
	 * today
	 * @param tx
	 */
	private String setTextToday(TextView tx){
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int monthOfYear = calendar.get(Calendar.MONTH);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		int month = monthOfYear+1;
		String monthString = ""+month;
		if (monthOfYear<10) {
			monthString = "0"+month;
		}
		String day = dayOfMonth+"";
		if (dayOfMonth<10) {
			day = "0"+day;
		}
		StringBuffer sBuffer = new StringBuffer();
		String string = sBuffer.append(year)
		.append("-")
		.append(monthString)
		.append("-")
		.append(day).toString();
		if (tx!=null) {
			tx.setText(string);
			
		}
		return string;
	}
	
	/**
	 * last dayBefore days
	 * @param tx
	 */
	private void setTextdaysBeforeFromTo(TextView tx,int dayBefore){
		Date date = TimeUtil.getDateBefore(new Date(), dayBefore);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String time = format.format(date);
		Logg.i(TAG, "time-"+time);
		tx.setText(time);
	}
	
}
