package com.delta.smsandroidproject.view;

import com.delta.smsandroidproject.bean.PublicKeyData;

public interface PublicKeyView {
	public void setPublicKey(PublicKeyData keyData);	//公钥
	public void showDialog();	//显示对话框
	public void dismiss();		//隐藏对话框
	public void showNetAlertDialog();
}
