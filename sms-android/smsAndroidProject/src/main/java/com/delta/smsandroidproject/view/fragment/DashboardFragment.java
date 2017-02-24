package com.delta.smsandroidproject.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.dialog.NetAlertDialog;
import com.delta.smsandroidproject.util.GoogleUtil;
import com.delta.smsandroidproject.util.ToastCustom;
import com.delta.smsandroidproject.util.ToolUtil;
import com.delta.smsandroidproject.view.BindViewTool;
import com.delta.smsandroidproject.view.ViewInit;
import com.delta.smsandroidproject.view.ViewInitHelper;
import com.delta.smsandroidproject.view.activity.LoginActivity;
import com.delta.smsandroidproject.view.activity.MainActivity;

public class DashboardFragment extends BaseFragment implements ViewInit,
		OnClickListener {

	public static final String TAG = "DashBoardFragment";

	@BindViewTool(id = R.id.tv_dashboard_charge_function, clickable = true)
	private TextView tvChargeFunc;

	@BindViewTool(id = R.id.tv_dashboard_map, clickable = true)
	private TextView tvMap;

	@BindViewTool(id = R.id.tv_dashboard_route_planner)
	private TextView tvRoutePlanner;

	@BindViewTool(id = R.id.tv_dashboard_network_setting, clickable = true)
	private TextView tvNetWorkSetting;

	@BindViewTool(id = R.id.tv_dashboard_logout, clickable = true)
	private TextView tvLogout;

	@BindViewTool(id = R.id.tv_dashboard_settings, clickable = true)
	private TextView tvSettings;

	private Context mContext;

	public static DashboardFragment getInstance() {
		return new DashboardFragment();
	}

	public DashboardFragment() {
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mContext = context;
	}

	@Override
	public void onResume() {
		super.onResume();
		((MainActivity) getActivity()).getSupportActionBar().setTitle(
				getString(R.string.title_dashborad));
		Toolbar mToolbar = (Toolbar) ((MainActivity) getActivity())
				.findViewById(R.id.toolbar);
		mToolbar.setNavigationIcon(R.drawable.dash);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// 默认显示一般用户界面
		View view = inflater.inflate(R.layout.fragment_dashboard, container,
				false);

		// 是超级用户就显示超级用户界面
		boolean isSuper = !ToolUtil.notService();
		if (isSuper) {
			view = inflater.inflate(R.layout.fragment_dashboard_super,
					container, false);
		}

		// 初始化布局,数据,以及事件监听
		initView(view);
		initData();
		initListener();

		return view;
	}

	@Override
	public void onStop() {
		Toolbar mToolbar = (Toolbar) ((MainActivity) getActivity())
				.findViewById(R.id.toolbar);
		mToolbar.setNavigationIcon(R.drawable.ic_back);
		super.onStop();
	}

	@Override
	public void initView(View view) {
		ViewInitHelper.initView(this, view);
	}

	@Override
	public void initData() {
	}

	@Override
	public void initListener() {
		if (tvRoutePlanner.getVisibility() == View.VISIBLE) {
			tvRoutePlanner.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tv_dashboard_charge_function:
			jumpToServicesPage();
			break;
		case R.id.tv_dashboard_map:
			jumpToMapPage();
			break;
		case R.id.tv_dashboard_route_planner:
			jumpToRoutePlannerPage();
			break;
		case R.id.tv_dashboard_network_setting:
			jumpToSwitchNetworkPage();
			break;
		case R.id.tv_dashboard_settings:
			jumpToSettingsPage();
			break;
		case R.id.tv_dashboard_logout:
			logout();
			break;
		default:
			break;
		}
	}

	private void jumpToServicesPage() {
		Fragment currentFragment = new ChargingFunctionFragment();
		String tag = ChargingFunctionFragment.TAG;
		jumpToNextPage(currentFragment, tag);
	}

	private void jumpToMapPage() {
		if (GoogleUtil.isSupportGoogMap()) {
			Fragment currentFragment = new ChargerMapFragment();
			String tag = ChargerMapFragment.TAG;
			jumpToNextPage(currentFragment, tag);
		} else {
			ToastCustom.showToast(
					mContext,
					mContext.getResources().getString(
							R.string.goolge_map_no_support),
					ToastCustom.LENGTH_SHORT);
		}
	}

	private void jumpToRoutePlannerPage() {
		Fragment currentFragment = ServiceRoutePlannerFragment.getInstance();
		String tag = ServiceRoutePlannerFragment.TAG;
		jumpToNextPage(currentFragment, tag);
	}

	private void jumpToSwitchNetworkPage() {
		Fragment currentFragment = new SelectNetWorkFragment();
		String tag = SelectNetWorkFragment.TAG;
		jumpToNextPage(currentFragment, tag);
	}

	private void jumpToSettingsPage() {
		Fragment currentFragment = new SettingFragment();
		String tag = SettingFragment.TAG;
		jumpToNextPage(currentFragment, tag);
	}

	private void logout() {
		SMSApplication.getInstance().clearAllActivity();
		startActivity(new Intent(getContext(), LoginActivity.class));
	}

	private void jumpToNextPage(Fragment page, String tag) {
		// 如果网络可用,就跳转到下一个页面
		if (isNetworkAvailable(getContext())) {
			if (page != null) {
				MainActivity.mFragmentManager.beginTransaction()
						.replace(R.id.container, page, tag)
						.addToBackStack(null).commit();
			}
		} else {
			// 如果网络未连接,就提示连接网络
			promptConnectToNet();
		}
	}

	// 判断网络是否连接
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo[] info = cm.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// 提示连接网络
	private void promptConnectToNet() {
		new NetAlertDialog(getContext(), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				Intent intent = new Intent(
						android.provider.Settings.ACTION_SETTINGS);
				startActivity(intent);
				dialog.dismiss();
			}
		}).show();
	}

	@Override
	public boolean onBackPress() {
		return true;
	}
}
