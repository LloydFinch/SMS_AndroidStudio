package com.delta.smsandroidproject.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.bean.BestRouteModel;
import com.delta.smsandroidproject.util.SetTextUtils;
import com.delta.smsandroidproject.view.ViewInit;
import com.delta.smsandroidproject.view.activity.MainActivity;
import com.delta.smsandroidproject.view.adapter.RoutePlanAdapter;
import com.delta.smsandroidproject.view.adapter.RoutedailAdapter;

public class RouteDetailsFragment extends BaseFragment implements ViewInit {

	public static final String TAG = "RouteDetailFragment";

	private static RouteDetailsFragment instance;

	private ListView listViewSteps;
	private SMSApplication myApplication;
	public static RouteDetailsFragment getInstance() {
		if (instance == null) {
			instance = new RouteDetailsFragment();
		}
		return instance;
	}

	public RouteDetailsFragment() {
	}

	@Override
	public void onResume() {
		super.onResume();
		((MainActivity) getActivity()).getSupportActionBar().setTitle(
				getString(R.string.title_route_detail));
		listViewSteps.setSelection(0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myApplication = SMSApplication.getInstance();
		View view = inflater.inflate(R.layout.fragment_routedetail, container,
				false);

		initView(view);
		initData();
		initListener();

		return view;
	}

	@Override
	public void initView(View view) {
		listViewSteps = (ListView) view.findViewById(R.id.route_detail_list);
	}

	@SuppressLint("InflateParams")
	@Override
	public void initData() {
		Bundle bundle = instance.getArguments();
		if (bundle != null) {

			// 设置list的头部布局
			LinearLayout header = (LinearLayout) LayoutInflater.from(
					getContext()).inflate(R.layout.item_routedetail_header,
					null);
			TextView tvVisit = (TextView) header
					.findViewById(R.id.routedetail_tv_visit);
			TextView tvDistance = (TextView) header
					.findViewById(R.id.routedetail_tv_distance);
			TextView tvDutration = (TextView) header
					.findViewById(R.id.routedetail_tv_duration);
			listViewSteps.addHeaderView(header);

			// 添加数据
			BestRouteModel routeModel = (BestRouteModel) bundle
					.getSerializable("route");
			if (routeModel != null) {

				// 给头部添加数据
				SetTextUtils.setText(tvVisit, routeModel.getVisit());
				SetTextUtils.setText(
						tvDistance,
						String.valueOf(RoutePlanAdapter.parseNum(routeModel
								.getDistance() / 1000.0f) + myApplication.getResources().getString(R.string.km)));
				SetTextUtils.setText(tvDutration,
						String.valueOf(routeModel.getDuration() / 60) + myApplication.getResources().getString(R.string.min));

				// 给list添加的数据
				RoutedailAdapter adapter = new RoutedailAdapter(getContext(),
						routeModel);
				listViewSteps.setAdapter(adapter);
			}
		}
	}

	@Override
	public void initListener() {
	}

	@Override
	public boolean onBackPress() {
		return false;
	}
}
