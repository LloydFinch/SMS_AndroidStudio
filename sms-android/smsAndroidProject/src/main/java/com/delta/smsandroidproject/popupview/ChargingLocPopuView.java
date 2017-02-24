package com.delta.smsandroidproject.popupview;

import java.util.HashMap;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.bean.ChargerLocationData;
import com.delta.smsandroidproject.bean.LocationInfoData;
import com.delta.smsandroidproject.presenter.GetLocationInfoPresenter;
import com.delta.smsandroidproject.presenter.GetLocationInfoPresenter.getLocationInfo;
import com.delta.smsandroidproject.util.GoogleUtil;
import com.delta.smsandroidproject.util.Logg;
import com.delta.smsandroidproject.util.ToastCustom;
import com.delta.smsandroidproject.util.ToolUtil;
import com.google.android.gms.maps.model.LatLng;

/**
 * 在charging location map界面下点击location出现的布局
 * 
 * @author Jianzao.Zhang
 * 
 */
public class ChargingLocPopuView implements OnClickListener,getLocationInfo {

	private Context mContext;
	private PopupWindow mPopupWindow;
	private LinearLayout layout;
	private ChargerLocationData data;
	private LatLng myLatLng;
	private ClickListener listener;
	private LocationInfoData locInfoData;//为了获取location的address地址
	private GetLocationInfoPresenter presenter;
	public interface ClickListener {
		public void onClick(ChargerLocationData data);
	}

	public ChargingLocPopuView(Context context, LinearLayout layout,
			ChargerLocationData data, LatLng myLatLng, ClickListener listener) {
		this.mContext = context;
		this.layout = layout;
		this.myLatLng = myLatLng;
		this.listener = listener;
		this.data = data;
		init();
	}

	private void init() {
		presenter = new GetLocationInfoPresenter(this,mContext);
		loadLocData(data.getId());
		WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
//		int height = wm.getDefaultDisplay().getHeight();
		int height = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 130, mContext.getResources()
						.getDisplayMetrics());

		View inflate = LayoutInflater.from(mContext).inflate(
				R.layout.item_map_popview, null);
		mPopupWindow = new PopupWindow(inflate, width, height);
		// 解决点击外部，popupview消失
		mPopupWindow.setOutsideTouchable(true);//
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

		// 查找控件
		LinearLayout mLocLayout = (LinearLayout) inflate
				.findViewById(R.id.locLayout);
		LinearLayout mCLocLayout = (LinearLayout) inflate
				.findViewById(R.id.copyLocInfo);
		TextView mLoc = (TextView) inflate.findViewById(R.id.loc);

		TextView mDistance = (TextView) inflate.findViewById(R.id.distance);
		ImageView mImgIntentToLocInfo = (ImageView) inflate
				.findViewById(R.id.imgIntentToLocInfo);

		if (data != null) {
			mLoc.setText(data.getName());
			Logg.i("ChargingLocPopuView-myLatLng", ""+myLatLng);
			Double distanceString = GoogleUtil.getDistanceString(myLatLng, new LatLng(
					data.getLat(), data.getLon()));
			if (distanceString!=null) {
				mDistance
				.setText(distanceString
						+ mContext.getResources().getString(
								R.string.loc_away_from));
				Logg.i("distanceString", ""+distanceString);
				if (distanceString <= 0.0d) {
					mLocLayout.setVisibility(View.GONE);
					mPopupWindow.setHeight(height-50);
				}
			}
		}

		mLocLayout.setOnClickListener(this);
		mCLocLayout.setOnClickListener(this);
		mImgIntentToLocInfo.setOnClickListener(this);
	}

	private void loadLocData(String locId) {
		HashMap<String, String> locInfMap = new HashMap<String, String>();
		locInfMap.put("key", locId);
		presenter.loadData(locInfMap);
	}

	public void showPopViewWindow() {
		if (mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		} else {
			int[] loc = new int[2];
			layout.getLocationOnScreen(loc);
			// 第一个参数指定PopupWindow的锚点view，即依附在哪个view上。
			// 第二个参数指定起始点为parent的右下角，第三个参数设置以parent的右下角为原点，向左、上各偏移10像素。
			mPopupWindow.showAtLocation(layout, Gravity.BOTTOM, 100, 100);

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.locLayout:
			if (data != null) {
				Logg.i("address", ""+GoogleUtil.getAddress(locInfoData));
//				GoogleUtil.navigationAddress(mContext, ToolUtil.getCurLocAddress(),GoogleUtil.getAddress(locInfoData));
				Logg.i("lat", ""+ToolUtil.getCurLocLat());
				Logg.i("lon", ""+ToolUtil.getCurLocLon());
				Logg.i("endlat", ""+data.getLat());
				Logg.i("endlon", ""+data.getLon());
				GoogleUtil.navigationAddress(mContext, ToolUtil.getCurLocLat()+"",ToolUtil.getCurLocLon()+"",data.getLat()+"",data.getLon()+"");
			}
			break;
		case R.id.copyLocInfo:
			if (data != null && locInfoData!=null) {
				ToolUtil.setText(GoogleUtil.getGoogleMapUrl(data,locInfoData));
				Log.i("toolUtil.getText()", "" + ToolUtil.getText());
				ToastCustom.showToast(mContext, "Copy to clipboard",
						ToastCustom.LENGTH_SHORT);
			}
			break;
		case R.id.imgIntentToLocInfo:
			showPopViewWindow();
			if (data != null) {
				// intent to location info activity,and put data to location
				// info fragment
				listener.onClick(data);
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public void getLocationInfoSuccess(LocationInfoData datas) {
		this.locInfoData = datas;
	}

	@Override
	public void getLocationInfoFailed() {
		
	}
	

}
