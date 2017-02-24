package com.delta.smsandroidproject.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.delta.smsandroidproject.R;

public class ChargingFunctionDialog extends Dialog{

	public ChargingFunctionDialog(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
	}
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO 自动生成的方法存根
	super.onCreate(savedInstanceState);
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.chargering_function_dialog);
}
}
