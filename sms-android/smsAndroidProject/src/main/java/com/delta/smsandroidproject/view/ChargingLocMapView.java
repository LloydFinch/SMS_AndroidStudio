package com.delta.smsandroidproject.view;

import java.util.List;

import com.delta.smsandroidproject.bean.ChargerLocationData;

public interface ChargingLocMapView {
	public void setCurrentLocation(ChargerLocationData data);
	public void dismissChooseLocationDialog();
	public void dismiss();		// dismiss progress dialog
	public void showDialog();	//show progress dialog
	public void setLocMarkToMap(List<ChargerLocationData> datas);
	public List<ChargerLocationData> getLocationDatas();
	public void showNetAlertDialog();
}
