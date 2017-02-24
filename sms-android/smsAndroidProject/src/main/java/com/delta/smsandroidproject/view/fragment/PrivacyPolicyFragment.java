package com.delta.smsandroidproject.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.view.activity.MainActivity;

public class PrivacyPolicyFragment extends BaseFragment {
	private WebView webView;
	public static final String TAG = "PrivacyPolicyFragment";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_privaty_policy,
				container, false);
		initView(view);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		((MainActivity) getActivity()).getSupportActionBar().setTitle(
				getString(R.string.privacy_policy));
	}

	private void initView(View view) {
		webView = (WebView) view.findViewById(R.id.web_view);
		webView.loadUrl("file:///android_asset/html/privacy.html");
	}

	@Override
	public boolean onBackPress() {
		return false;
	}

}
