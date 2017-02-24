package com.delta.smsandroidproject.view.iview;

import java.util.ArrayList;

import com.delta.smsandroidproject.bean.ChargerLocationData;

public interface ICF_getLocationList {
		void getLocationListSuccess(ArrayList<ChargerLocationData> datas);
		void getLocationListFailed();
}
