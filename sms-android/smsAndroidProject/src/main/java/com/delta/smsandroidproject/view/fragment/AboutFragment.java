package com.delta.smsandroidproject.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.view.activity.MainActivity;

public class AboutFragment extends BaseFragment implements OnClickListener {
	private View view;
	private Context mContext;
	public static final String TAG = "AboutFragment";
	private RelativeLayout about_version, terms_of_service, privacy_policy,
			open_source_license;
	private OpenSourceFragment osFragment;
	private MainActivity activity;

	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mContext = getActivity();
		// LanguageUtil.setLanguage(mContext, ToolUtil.getLanguageId());
		SMSApplication.setLanguage();
		view = inflater.inflate(R.layout.about_layout, container, false);
		initView(view);
		activity = (MainActivity) getActivity();
		activity.setIsFrist(true);
		return view;
	}

	private void initView(View view) {
		about_version = (RelativeLayout) view.findViewById(R.id.about_version);
		terms_of_service = (RelativeLayout) view
				.findViewById(R.id.terms_of_service);
		privacy_policy = (RelativeLayout) view
				.findViewById(R.id.privacy_policy);
		open_source_license = (RelativeLayout) view
				.findViewById(R.id.open_source_license);
		about_version.setOnClickListener(this);
		terms_of_service.setOnClickListener(this);
		privacy_policy.setOnClickListener(this);
		open_source_license.setOnClickListener(this);
	}

	@Override
	public boolean onBackPress() {
		// TODO 自动生成的方法存根
		return true;
	}

	@Override
	public void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
//		Toolbar mToolbar = (Toolbar) ((MainActivity) getActivity())
//				.findViewById(R.id.toolbar);
//		mToolbar.setNavigationIcon(R.drawable.dash);
		((MainActivity) getActivity()).getSupportActionBar().setTitle(
				getString(R.string.title_about));
		ActionBar mActionBar = ((MainActivity)getActivity()).getSupportActionBar();
	    mActionBar.setHomeAsUpIndicator(R.drawable.dash);
	    mActionBar.setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.about_version:

			break;
		case R.id.terms_of_service:
			toTermsService();
			break;
		case R.id.privacy_policy:
			toPrivacyPolicy();
			break;
		case R.id.open_source_license:
			toOpenSource();
			break;
		default:
			break;
		}
	}

	private void toPrivacyPolicy() {
		MainActivity.mFragmentManager.beginTransaction()
				.replace(R.id.container, new PrivacyPolicyFragment())
				.addToBackStack(PrivacyPolicyFragment.TAG).commit();
	}

	private void toTermsService() {
		MainActivity.mFragmentManager.beginTransaction()
				.replace(R.id.container, new TermsServiceFragment())
				.addToBackStack(TermsServiceFragment.TAG).commit();
	}

	private void toOpenSource() {
		if (osFragment == null) {
			osFragment = new OpenSourceFragment();
		}
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.container, osFragment)
				.addToBackStack(AboutFragment.TAG).commit();
	}

	@Override
	public void onStop() {
		// TODO 自动生成的方法存根
		super.onStop();
		activity.setIsFrist(false);
		Toolbar mToolbar = (Toolbar) ((MainActivity) getActivity())
				.findViewById(R.id.toolbar);
		mToolbar.setNavigationIcon(R.drawable.ic_back);
//		ActionBar mActionBar = ((MainActivity)getActivity()).getSupportActionBar();
//	    mActionBar.setHomeAsUpIndicator(R.drawable.ic_back);
	   // mActionBar.setDisplayHomeAsUpEnabled(false);
		
	}
}
