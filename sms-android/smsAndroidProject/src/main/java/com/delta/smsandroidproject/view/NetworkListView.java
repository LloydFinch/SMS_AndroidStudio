package com.delta.smsandroidproject.view;

import java.util.List;

import com.delta.smsandroidproject.bean.NetworkData;

public interface NetworkListView {
	public void setNetworkDatas(List<NetworkData> datas);
	public List<NetworkData> getNetworkDatas();
	public void showDialog();
	public void dimiss();
	public void showNetAlertDialog();
	
}
