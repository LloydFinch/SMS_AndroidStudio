package com.delta.smsandroidproject.view;

import com.delta.smsandroidproject.bean.NetwrkInfoData;

public interface NetworkSettingView {
	public void showDialog();
	public void dimiss();
	public void setNetwrkInfoData(NetwrkInfoData data);
	public NetwrkInfoData getNetwrkInfoData();
}
