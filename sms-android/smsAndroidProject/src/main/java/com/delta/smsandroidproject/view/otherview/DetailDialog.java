package com.delta.smsandroidproject.view.otherview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.bean.EvseDetailData;

public class DetailDialog extends Dialog {
	private TextView description1, user, solution, description2, value, key,
			oldvalue1, oldvalue2, status;

	public DetailDialog(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_detail);
		//initView();
	}

	private void initView() {
		description1 = (TextView) findViewById(R.id.description1);
		user = (TextView) findViewById(R.id.user);
		solution = (TextView) findViewById(R.id.solution);
		description2 = (TextView) findViewById(R.id.description2);
		value = (TextView) findViewById(R.id.value);
		key = (TextView) findViewById(R.id.key);
		oldvalue1 = (TextView) findViewById(R.id.oldvalue1);
		oldvalue2 = (TextView) findViewById(R.id.oldvalue2);
		status = (TextView) findViewById(R.id.status);
	}

	public void setData(EvseDetailData datas) {
		initView();
		Log.e("显示返回的数据", datas.toString());
		if (!TextUtils.isEmpty(datas.getDescription())) {
			description1.setText(datas.getDescription());
		}
		if (!TextUtils.isEmpty(datas.getUser())) {
			user.setText(datas.getUser());
		}
		if (!TextUtils.isEmpty(datas.getSolution())) {
			solution.setText(datas.getSolution());
		}
		if (!TextUtils.isEmpty(datas.getError())) {

			description2.setText(datas.getError());
		}
		if (!TextUtils.isEmpty(datas.getValue())) {
			value.setText(datas.getValue());
		}
		if (!TextUtils.isEmpty(datas.getKey())) {

			key.setText(datas.getKey());
		}
		if (!TextUtils.isEmpty(datas.getOldValue())) {
			oldvalue1.setText(datas.getOldValue());
		}
		if (!TextUtils.isEmpty(datas.getNewValue())) {

			oldvalue2.setText(datas.getNewValue());
		}
		if (!TextUtils.isEmpty(datas.getStatus())) {

			status.setText(datas.getStatus());
		}
	}
}
