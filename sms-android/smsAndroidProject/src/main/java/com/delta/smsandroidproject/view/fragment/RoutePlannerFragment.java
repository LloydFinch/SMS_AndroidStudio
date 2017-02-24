package com.delta.smsandroidproject.view.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.delta.common.utils.GsonTools;
import com.delta.common.utils.LogUtils;
import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.bean.BestRouteModel;
import com.delta.smsandroidproject.bean.ChargerLocationData;
import com.delta.smsandroidproject.bean.GoogleDirectionModel;
import com.delta.smsandroidproject.bean.GoogleDirectionModel.Routes;
import com.delta.smsandroidproject.bean.GoogleDirectionModel.Routes.Fare;
import com.delta.smsandroidproject.bean.GoogleDirectionModel.Routes.Legs;
import com.delta.smsandroidproject.bean.GoogleDirectionModel.Routes.Legs.Distance;
import com.delta.smsandroidproject.bean.GoogleDirectionModel.Routes.Legs.Duration;
import com.delta.smsandroidproject.dialog.MyProgressDialog;
import com.delta.smsandroidproject.util.Comment;
import com.delta.smsandroidproject.util.RoutePlanerComparator;
import com.delta.smsandroidproject.util.ToolUtil;
import com.delta.smsandroidproject.view.ViewInit;
import com.delta.smsandroidproject.view.activity.MainActivity;
import com.delta.smsandroidproject.view.adapter.BasicViewHolder.onRecyclerViewItemClickListener;
import com.delta.smsandroidproject.view.adapter.RoutePlanAdapter;

public class RoutePlannerFragment extends BaseFragment implements ViewInit,
		OnClickListener, onRecyclerViewItemClickListener {

	public static final String TAG = "RoutePlannerFragment";
	private static RoutePlannerFragment instance;

	private RecyclerView routePlannerList;
	private RoutePlanAdapter adapter;

	// 所有路径
	private List<BestRouteModel> useRouteModels = new ArrayList<>();

	// 起点
	private ChargerLocationData origin;
	// 终点
	private ChargerLocationData destination;
	// 途经点
	private ArrayList<ChargerLocationData> visitLocations;

	private MyProgressDialog diaLog;

	public static RoutePlannerFragment getInstance() {
		if (instance == null) {
			instance = new RoutePlannerFragment();
		}
		return instance;
	}

	public RoutePlannerFragment() {
	}

	@Override
	public void onResume() {
		super.onResume();
		((MainActivity) getActivity()).getSupportActionBar().setTitle(
				getString(R.string.title_route_planner));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_routeplanner, container,
				false);

		initView(view);
		initData();
		initListener();

		return view;
	}

	@Override
	public void initView(View view) {
		routePlannerList = (RecyclerView) view
				.findViewById(R.id.routeplan_list);
		routePlannerList.setLayoutDirection(RecyclerView.VERTICAL);
		routePlannerList
				.setLayoutManager(new LinearLayoutManager(getActivity()));
		routePlannerList.setItemAnimator(new DefaultItemAnimator());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initData() {
		Bundle bundle = instance.getArguments();
		if (bundle != null) {

			useRouteModels.clear();
			adapter = new RoutePlanAdapter(useRouteModels);
			adapter.setOnItemClickListener(this);
			routePlannerList.setAdapter(adapter);

			// 获取所有用户选中的途经点
			visitLocations = (ArrayList<ChargerLocationData>) bundle
					.getSerializable("locations");

			origin = (ChargerLocationData) bundle.get("origin");
			destination = (ChargerLocationData) bundle.get("destination");

			if (origin != null && destination != null) {
				String originPoint = origin.getLat() + "," + origin.getLon();
				String desPoint = destination.getLat() + ","
						+ destination.getLon();

				// 构造途经点参数
				StringBuilder wayPoints = new StringBuilder();
				if (visitLocations != null && visitLocations.size() > 0) {
					for (ChargerLocationData data : visitLocations) {
						String d = data.getLat() + "," + data.getLon();
						if (!d.equals(originPoint) && !d.equals(desPoint)) {
							wayPoints.append(d).append("|");
						}
					}
				}

				// 拼接URL并访问谷歌获取路径
				if (wayPoints.length() > 0) {
					String path = Comment.GOOGLE_DIRECTION_URL
							+ "origin="
							+ originPoint
							+ "&destination="
							+ desPoint
							+ "&waypoints=optimize:true|"
							+ wayPoints.substring(0, wayPoints.length() - 1)
									.toString() + "&alternatives=true"
							+ "&language=" + ToolUtil.getSysLanguage()
							+ "&key=AIzaSyBwuY55ylEFUtvtX3AhCsg48XqAFeds7tA";
					getRoute(path);
					LogUtils.d("route-url", "waypoints:" + path);
				} else {
					String path = Comment.GOOGLE_DIRECTION_URL + "origin="
							+ originPoint + "&destination=" + desPoint
							+ "&alternatives=true" + "&language="
							+ ToolUtil.getSysLanguage()
							+ "&key=AIzaSyBwuY55ylEFUtvtX3AhCsg48XqAFeds7tA";
					LogUtils.d("route-url", "waypoints:" + path);
					getRoute(path);
				}
			}
		}
	}

	// 从谷歌获取所有可用的路径
	private void getRoute(String path) {
		diaLog = new MyProgressDialog(getActivity());
		diaLog.show(getContext().getResources().getString(R.string.loading));
		StringRequest request = new StringRequest(Method.GET, path,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						if (!TextUtils.isEmpty(response)) {
							GoogleDirectionModel route = GsonTools
									.changeGsonToBean(response,
											GoogleDirectionModel.class);
							LogUtils.i("route-response", response);
							if (route != null) {
								useRouteModels.clear();
								useRouteModels.addAll(getBestRoute(route));
								Collections.sort(useRouteModels,
										new RoutePlanerComparator());
								adapter.setRoutes(useRouteModels);
								adapter.notifyDataSetChanged();
							}
						}
						diaLog.dismiss();
						if (useRouteModels.size() == 0) {
							Toast.makeText(
									getContext(),
									getContext().getResources().getString(
											R.string.alert_no_any_route),
									Toast.LENGTH_SHORT).show();
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						LogUtils.d("error", "Route-error" + error);
						diaLog.dismiss();
						Toast.makeText(
								getContext(),
								getContext().getResources().getString(
										R.string.dialog_net_alert),
								Toast.LENGTH_SHORT).show();
					}
				});
		request.setRetryPolicy(new DefaultRetryPolicy(1000 * 5, 1, 1.0f));
		SMSApplication.getRequestQueue().add(request);
	}

	// 将谷歌返回的路径转换为我们需要的路径,并按照路径长短和费用高低排序
	private List<BestRouteModel> getBestRoute(GoogleDirectionModel route) {
		List<BestRouteModel> routePlanList = new ArrayList<>();
		if (route != null) {
			// 获取所有路径
			List<Routes> routesList = route.getRoutes();
			if (routesList != null && routesList.size() > 0) {
				LogUtils.i("routesList:", "size:" + routesList.size());
				for (Routes routes : routesList) {
					BestRouteModel r = new BestRouteModel();
					if (routes != null) {

						// 获取费用
						Fare fare = routes.getFare();
						if (fare != null) {
							r.setFare(fare.getValue());
							r.setFareValue(fare.getText());
						}

						// 获取 copyrights
						String copyrights = routes.getCopyrights();
						if (!TextUtils.isEmpty(copyrights)) {
							r.setCopyrights(copyrights);
						}
						// 获取提示信息
						String[] warnings = routes.getWarnings();
						if (warnings != null) {
							r.setWarnings(warnings);
						}

						// get all legs
						List<Legs> legsList = routes.getLegs();
						if (legsList != null && legsList.size() > 0) {

							// 获取距离和花费时间
							long distance = 0;
							long duration = 0;
							for (Legs leg : legsList) {
								Distance d = leg.getDistance();
								if (d != null) {
									distance += d.getValue();
								}
								Duration dt = leg.getDuration();
								if (dt != null) {
									duration += dt.getValue();
								}
							}

							// 根据起点,途经点,终点来构造路径
							List<Integer> orders = routes.getWaypoint_order();
							StringBuilder sb = new StringBuilder();
							// 获取起点
							if (origin != null) {
								sb.append(origin.getName());
							}
							// 获取途经点
							for (Integer index : orders) {
								sb.append("->").append(
										visitLocations.get(index).getName());
							}
							// 获取终点
							sb.append("->").append(destination.getName());

							r.setDistance(distance);
							r.setDuration(duration);
							r.setVisit(sb.toString());
							r.setLegs(legsList);
							routePlanList.add(r);
						}
					}
				}

				// 排序(距离第一,费用第二)
				Collections.sort(routePlanList, new RoutePlanerComparator());
			}
		}
		return routePlanList;
	}

	@Override
	public void initListener() {
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public boolean onBackPress() {
		return false;
	}

	// 点击任何一条路径信息就跳转到路径的详情页面
	@Override
	public void onItemClick(View itemView, int position) {
		BestRouteModel route = useRouteModels.get(position);
		if (route != null) {
			Bundle bundle = new Bundle();
			bundle.putSerializable("route", route);
			RouteDetailsFragment fragment = RouteDetailsFragment.getInstance();
			fragment.setArguments(bundle);
			MainActivity.mFragmentManager
					.beginTransaction()
					.replace(R.id.container, fragment, RouteDetailsFragment.TAG)
					.addToBackStack(RouteDetailsFragment.TAG).commit();
		}
	}
}
