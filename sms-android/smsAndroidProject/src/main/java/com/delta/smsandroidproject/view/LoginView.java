package com.delta.smsandroidproject.view;

public interface LoginView {
	public void showDialog();
	public void dismiss();
	public void failed(String s);
	public void successed(String s);
	public void intentToMainActivity();
	public void showNetAlertDialog();
}
