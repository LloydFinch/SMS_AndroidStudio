package com.delta.smsandroidproject;

import java.util.List;

import com.delta.smsandroidproject.bean.EventListData;

public interface EventView {
	public void showDialog();
	public void dismiss();
	public void setEventDatas(List<EventListData> datas);
	public List<EventListData> getEventDatas();
	public void isLoadDataFinish(boolean b);
}
