package com.delta.smsandroidproject.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.view.activity.MainActivity;

public class OpenSourceFragment extends BaseFragment {
	private View view;
	private Context mContext;
	private WebView webView;
	public static final String TAG = "OpenSourceFragment";

	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mContext = getActivity();
		// LanguageUtil.setLanguage(mContext, ToolUtil.getLanguageId());
		SMSApplication.setLanguage();
		view = inflater.inflate(R.layout.open_source_layout, container, false);
		initView(view);
		return view;
	}
@Override
public void onResume() {
	// TODO 自动生成的方法存根
	super.onResume();
	((MainActivity) getActivity()).getSupportActionBar().setTitle(
			getActivity().getResources().getString(
					R.string.open_source_license));
}
	private void initView(View view) {
		webView = (WebView) view.findViewById(R.id.web_view);
		webView.loadUrl("file:///android_asset/licenses.html");
	}

	@Override
	public boolean onBackPress() {
		// TODO 自动生成的方法存根
		return false;
	}

}
