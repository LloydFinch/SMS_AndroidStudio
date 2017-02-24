package com.delta.smsandroidproject.view.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.delta.common.utils.GsonTools;
import com.delta.common.utils.LogUtils;
import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.bean.ChargerLocationData;
import com.delta.smsandroidproject.bean.LocResultTotalData;
import com.delta.smsandroidproject.dialog.MyProgressDialog;
import com.delta.smsandroidproject.dialog.NetAlertDialog;
import com.delta.smsandroidproject.request.ParseResponse;
import com.delta.smsandroidproject.request.SessionTimeoutHandler;
import com.delta.smsandroidproject.request.StringCookieRequest;
import com.delta.smsandroidproject.util.Comment;
import com.delta.smsandroidproject.util.ToolUtil;
import com.delta.smsandroidproject.view.BindViewTool;
import com.delta.smsandroidproject.view.ViewInit;
import com.delta.smsandroidproject.view.ViewInitHelper;
import com.delta.smsandroidproject.view.activity.MainActivity;
import com.delta.smsandroidproject.view.adapter.OriginSpinnerAdapter;
import com.delta.smsandroidproject.view.adapter.ServiceRoutePlanAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

public class ServiceRoutePlannerFragment extends BaseFragment implements
		ViewInit, OnClickListener, LocationListener, ConnectionCallbacks,
		OnConnectionFailedListener {

	public static final String TAG = "ServiceRoutePlannerFragment";
	public static final int REQUEST_CHECK_SETTINGS = 10001;
	public static final int GETDATA_ON_ERROR = 10002;

	private static String CURRENT_NAME = "My current location";
	private static ServiceRoutePlannerFragment instance;

	@BindViewTool(id = R.id.button_plan, clickable = true)
	public static ImageView buttonPlan;

	@BindViewTool(id = R.id.sp_origin)
	private Spinner originSpinner;

	@BindViewTool(id = R.id.sp_des)
	private Spinner desSpinner;

	@BindViewTool(id = R.id.servicerouteplan_list)
	private RecyclerView routePlannerList;

	public static OriginSpinnerAdapter originAdapter;
	public static OriginSpinnerAdapter desAdapter;
	public static ServiceRoutePlanAdapter wayPointsAdapter;

	// 所有地点
	public static ArrayList<ChargerLocationData> locationList;

	// 所有可选起点
	public static ArrayList<ChargerLocationData> originList;
	// 所有可选终点
	public static ArrayList<ChargerLocationData> desList;
	// 所有可选途经点
	public static ArrayList<ChargerLocationData> wayPonintsList;

	// 选择的起点
	private ChargerLocationData origin;
	// 选择的终点
	private ChargerLocationData destination;

	private LocationManager locationManager;
	private MyProgressDialog dialog;

	// 当前地点
	private ChargerLocationData currentLocation;

	// 获取地点所使用的URL
	private String initPath;

	private String locationProvider;
	private String loading;

	public static ServiceRoutePlannerFragment getInstance() {
		if (instance == null) {
			instance = new ServiceRoutePlannerFragment();
		}
		return instance;
	}

	public ServiceRoutePlannerFragment() {
	}

	@Override
	public void onResume() {
		super.onResume();

		((MainActivity) getActivity()).getSupportActionBar().setTitle(
				getString(R.string.title_route_planner_s));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_service_routeplanner,
				container, false);
		loading = getActivity().getResources().getString(R.string.loading);
		initView(view);
		initData();
		initListener();

		return view;
	}

	@Override
	public void initView(View view) {

		ViewInitHelper.initView(this, view);
		routePlannerList.setLayoutDirection(RecyclerView.VERTICAL);
		routePlannerList
				.setLayoutManager(new LinearLayoutManager(getActivity()));
		routePlannerList.setItemAnimator(new DefaultItemAnimator());

		buttonPlan.setVisibility(View.GONE);
	}

	@Override
	public void initData() {

		// 初始化成员
		initFields();

		// 添加地点监听
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, this);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, this);

		// 检查GPS状态
		if (!checkGPSstatus()) {
			GoogleApiClient();
		}

		// 添加当前地点
		addMyLocation();

		// 获取地点列表
		String workId = ToolUtil.getNetworkId();
		if (!TextUtils.isEmpty(workId)) {
			initPath = String.format(Comment.LOCATION_LIST_URL, workId);
			Log.i("choice", "path:" + initPath);
			dialog = new MyProgressDialog(getActivity());
			getLocationList(initPath);
		} else {
			Toast.makeText(
					getContext(),
					getContext().getResources().getString(
							R.string.alert_select_net), Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void initFields() {
		CURRENT_NAME = getContext().getResources().getString(
				R.string.current_location);
		currentLocation = new ChargerLocationData();
		currentLocation.setId("-1");

		locationList = new ArrayList<>();
		originList = new ArrayList<>();
		desList = new ArrayList<>();
		wayPonintsList = new ArrayList<>();

		wayPointsAdapter = new ServiceRoutePlanAdapter(getActivity(),
				wayPonintsList);
		originAdapter = new OriginSpinnerAdapter(getActivity(), originList);
		desAdapter = new OriginSpinnerAdapter(getActivity(), desList);

		locationManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
	}

	// 判断GPS是否可用,可用就尝试获取当前地点
	private boolean checkGPSstatus() {
		if (locationManager
				.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			if (locationManager
					.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
				Criteria c = new Criteria();
				c.setAccuracy(Criteria.ACCURACY_FINE);
				c.setAltitudeRequired(false);
				c.setBearingRequired(false);
				c.setCostAllowed(true);
				c.setPowerRequirement(Criteria.POWER_LOW);
				locationProvider = locationManager.getBestProvider(c, true);
				Location location = locationManager
						.getLastKnownLocation(locationProvider);
				if (location != null) {
					SMSApplication.setMyLocation(location);
				}
				return true;
			}
		}
		return false;
	}

	// 使用谷歌API来打开GPS设置对话框
	public void GoogleApiClient() {
		GoogleApiClient googleApiClient = new GoogleApiClient.Builder(
				getContext()).addApi(LocationServices.API)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).build();
		googleApiClient.connect();

		LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
				.addLocationRequest(new LocationRequest());
		builder.setAlwaysShow(true);
		PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
				.checkLocationSettings(googleApiClient, builder.build());
		result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

			@Override
			public void onResult(LocationSettingsResult result) {
				Status status = result.getStatus();
				switch (status.getStatusCode()) {
				case LocationSettingsStatusCodes.SUCCESS:
					break;
				case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
					try {

						status.startResolutionForResult(getActivity(),
								REQUEST_CHECK_SETTINGS);
					} catch (SendIntentException e) {
						e.printStackTrace();
					}
					break;
				case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

					break;
				default:
					break;
				}
			}
		});
	}

	// 将当前地点添加到地点列表中
	private void addMyLocation() {
		if (SMSApplication.getMyLocation() != null) {
			currentLocation.setLat((float) SMSApplication.getMyLocation()
					.getLatitude());
			currentLocation.setLon((float) SMSApplication.getMyLocation()
					.getLongitude());
		}
		currentLocation.setName(CURRENT_NAME);
		locationList.add(currentLocation);
	}

	// 获取所有服务器提供的地点列表
	private void getLocationList(String path) {
		dialog.show(loading);
		StringCookieRequest request = new StringCookieRequest(path,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						LocResultTotalData data = GsonTools.changeGsonToBean(
								ParseResponse.parseResponse(response),
								LocResultTotalData.class);
						if (data != null) {
							locationList.addAll(data.getResults());
						}
						originList.clear();
						desList.clear();
						wayPonintsList.clear();
						originList.addAll(locationList);
						desList.addAll(locationList);
						wayPonintsList.addAll(locationList);
						wayPonintsList.remove(0);

						wayPointsAdapter.changeData(wayPonintsList);
						originAdapter.changeData(originList);
						desAdapter.changeData(desList);

						routePlannerList.setAdapter(wayPointsAdapter);
						originSpinner.setAdapter(originAdapter);
						desSpinner.setAdapter(desAdapter);

						dialog.dismiss();
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						dialog.dismiss();
						if (SessionTimeoutHandler.handSessionTimeout(
								getContext(), error)) {
							return;
						}
						new NetAlertDialog(getContext(),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Intent intent = new Intent(
												android.provider.Settings.ACTION_SETTINGS);
										startActivityForResult(intent,
												GETDATA_ON_ERROR);
										dialog.dismiss();
									}
								}).show();
					}

				});

		SMSApplication.getRequestQueue().add(request);
	}

	@Override
	public void initListener() {
		buttonPlan.setOnClickListener(this);
		originSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				origin = originList.get(position);
				removeWayPoints();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		desSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				destination = desList.get(position);
				removeWayPoints();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	// 当起点和终点选中后,就移除途经点中的相同的地点
	public void removeWayPoints() {

		// 从途经点中删除相同的点
		wayPointsAdapter.removeWaypoint(origin);
		wayPointsAdapter.removeWaypoint(destination);

		// 刷新途经点列表
		wayPonintsList.clear();
		wayPonintsList.addAll(locationList);
		wayPonintsList.remove(origin);
		wayPonintsList.remove(destination);
		wayPointsAdapter.notifyDataSetChanged();

	}

	@Override
	public boolean onBackPress() {
		return false;
	}

	@Override
	public void onClick(View v) {
		RoutePlannerFragment fragment = RoutePlannerFragment.getInstance();
		Bundle bundle = new Bundle();
		// 选中的途经点
		ArrayList<ChargerLocationData> waypointsLocations = wayPointsAdapter
				.getChoiceLocations();
		if (origin != null && destination != null && waypointsLocations != null) {
			updateCurrentLocation(waypointsLocations);
			bundle.putSerializable("locations", waypointsLocations);
			bundle.putSerializable("origin", origin);
			bundle.putSerializable("destination", destination);
			fragment.setArguments(bundle);
			MainActivity.mFragmentManager
					.beginTransaction()
					.replace(R.id.container, fragment, RoutePlannerFragment.TAG)
					.addToBackStack(RoutePlannerFragment.TAG).commit();
			LogUtils.i("myLocation",
					locationList.get(0).getName() + "lat:"
							+ locationList.get(0).getLat() + "lng:"
							+ locationList.get(0).getLon());
		} else if (locationList == null || locationList.size() <= 1) {
			new NetAlertDialog(getContext(),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(
									android.provider.Settings.ACTION_SETTINGS);
							startActivity(intent);
							dialog.dismiss();
						}
					}).show();
		} else {
			Toast.makeText(
					getContext(),
					getContext().getResources().getString(
							R.string.alert_select_waypoint), Toast.LENGTH_LONG)
					.show();
		}
	}

	// 更新当前地点
	private void updateCurrentLocation(
			List<ChargerLocationData> waypointsLocations) {

		if (SMSApplication.getMyLocation() != null) {
			locationList.remove(currentLocation);
			currentLocation.setLat((float) SMSApplication.getMyLocation()
					.getLatitude());
			currentLocation.setLon((float) SMSApplication.getMyLocation()
					.getLongitude());
			locationList.add(0, currentLocation);

			originList.clear();
			originList.addAll(locationList);
			desList.clear();
			desList.addAll(locationList);

			for (ChargerLocationData data : waypointsLocations) {
				if (data.getName().equals(CURRENT_NAME)) {
					data.setLat(currentLocation.getLat());
					data.setLon(currentLocation.getLon());
				}
			}

			LogUtils.i("updateCurrentLocation",
					"lat:" + currentLocation.getLat() + "lng:"
							+ currentLocation.getLon());
		} else {
			LogUtils.i("updateCurrentLocation-null",
					"lat:" + currentLocation.getLat() + "lng:"
							+ currentLocation.getLon());
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			SMSApplication.setMyLocation(location);
			LogUtils.i("myLocation-changed", "lat:"
					+ SMSApplication.getMyLocation().getLatitude() + "lng:"
					+ SMSApplication.getMyLocation().getLongitude());
		}
	}

	// handle response from the GPSAlertDialog
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case GETDATA_ON_ERROR:
			getLocationList(initPath);
			break;
		case REQUEST_CHECK_SETTINGS:
			LogUtils.i("GoogleApiClient", "REQUEST_CHECK_SETTINGS");
			switch (resultCode) {
			case Activity.RESULT_OK:
				if (checkGPSstatus()) {
					locationManager.requestLocationUpdates(
							LocationManager.GPS_PROVIDER, 100, 0, this);
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER, 0, 0, this);
				} else {
					GoogleApiClient();
				}

				LogUtils.i("GoogleApiClient", "RESULT_OK");
				break;
			case Activity.RESULT_CANCELED:
				// The user was asked to change settings, but chose not to
				LogUtils.i("GoogleApiClient", "RESULT_CANCELED");
				break;
			default:
				LogUtils.i("GoogleApiClient", "NO_OPTIONS");
				break;
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onConnected(Bundle arg0) {

	}

	@Override
	public void onConnectionSuspended(int arg0) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {

	}
}
