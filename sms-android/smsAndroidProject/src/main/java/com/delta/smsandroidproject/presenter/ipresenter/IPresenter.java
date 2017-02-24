package com.delta.smsandroidproject.presenter.ipresenter;

import java.util.Map;

public interface IPresenter {
	void loadData(Map<String, String> map);
	void cancelAll();
}
