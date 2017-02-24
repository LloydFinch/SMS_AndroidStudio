package com.delta.smsandroidproject.view.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.bean.ChargerLocationData;
import com.delta.smsandroidproject.bean.LocationInfoData;
import com.delta.smsandroidproject.dialog.ChooseLocationDialog;
import com.delta.smsandroidproject.dialog.NetAlertDialog;
import com.delta.smsandroidproject.popupview.ChargingLocPopuView;
import com.delta.smsandroidproject.presenter.ChargingLocMapPresenter;
import com.delta.smsandroidproject.presenter.GetLocationInfoPresenter;
import com.delta.smsandroidproject.presenter.GetLocationInfoPresenter.getLocationInfo;
import com.delta.smsandroidproject.presenter.GooglePresenter;
import com.delta.smsandroidproject.util.GoogleUtil;
import com.delta.smsandroidproject.util.Logg;
import com.delta.smsandroidproject.util.ToastCustom;
import com.delta.smsandroidproject.util.ToolUtil;
import com.delta.smsandroidproject.view.ChargingLocMapView;
import com.delta.smsandroidproject.view.activity.MainActivity;
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
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class ChargerMapFragment extends BaseFragment implements
		OnClickListener, ChargingLocMapView, ConnectionCallbacks,
		OnConnectionFailedListener, getLocationInfo {
	public static final String TAG = "ChargerMapFragment";
	private static final int REQUEST_CHECK_SETTINGS = 0;
	private MapView mapView;

	private GoogleMap googleMap;
	private String provider;
	private NetAlertDialog netAlertDialog;
	private GoogleApiClient googleApiClient;
	private Toolbar mToolbar;
	private View mNearByLocIcon;
	private TextView mCurLocation;
	private ChargingLocMapPresenter presenter;
	private ChooseLocationDialog dialog;
	private ProgressDialog mDialog;
	private List<ChargerLocationData> datas = new ArrayList<ChargerLocationData>();// 刚接收到后台的数据
	private List<ChargerLocationData> datasToList = new ArrayList<ChargerLocationData>();// 刚接收到后台的数据
	private List<ChargerLocationData> locDatas = new ArrayList<ChargerLocationData>();
	private List<ChargerLocationData> nBDatas = new ArrayList<ChargerLocationData>();// 按距离排好序list
	private LinearLayout mLocPopviewLayout;
	private LatLng lat = null;
	private LatLng myLatLng;
	private Map<String, ChargerLocationData> locMap = new HashMap<String, ChargerLocationData>();// 供点击mark，传参给locationinfo
																									// fragment使用

	private ChargerLocationData data;

	private FragmentActivity mContext;
	private GetLocationInfoPresenter locInfoPresenter;
	private Map<String, Marker> markMap = new HashMap<String, Marker>();

	public ChargerMapFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflate = inflater.inflate(R.layout.fragment_charger_map,
				container, false);
		Log.i(TAG, "onCreateView");
		mContext = getActivity();
		initView(inflate, savedInstanceState);
		loadLocationData();
		// showMenuImg();
		GoogleApiClient();
		listener();
		getMyLocation();
		return inflate;
	}

	@Override
	public void onResume() {
		mapView.onResume();
		showMenuImg();
		closeToLoc();
		Log.i(TAG, "onResume");
		super.onResume();
	}

	@Override
	public void onDestroy() {
		mapView.onDestroy();
		super.onDestroy();
	}

	private void loadLocationData() {
		Bundle bundle = getArguments();
		if (bundle != null) {
			data = (ChargerLocationData) bundle
					.getSerializable("location");
		}
		presenter = new ChargingLocMapPresenter(this,getActivity());
		Logg.i(".getNetworkId()", "" + ToolUtil.getNetworkId());
		presenter.getLocations(ToolUtil.getNetworkId());
		locInfoPresenter = new GetLocationInfoPresenter(this,getActivity());
	}

	private void initView(View inflate, Bundle savedInstanceState) {
		mLocPopviewLayout = (LinearLayout) inflate
				.findViewById(R.id.locPopupview);
		mCurLocation = (TextView) inflate.findViewById(R.id.currentLocation);
		mapView = (MapView) inflate.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		mapView.onResume();// needed to get the map to display immediately
		googleMap = mapView.getMap();
		
		MapsInitializer.initialize(mContext);// 解决MapsInitializer.initialize(getApplicationContext())
		Logg.i(TAG, "googleMap-"+googleMap);
		if (googleMap == null) {
			ToastCustom.showToast(mContext, mContext.getResources().getString(R.string.goolge_map_no_support), ToastCustom.LENGTH_SHORT);
		}else {
			if (data != null) {
				mCurLocation.setText(data.getName());
				Double latitue = Double.valueOf(data.getLat());
				Double longitude = Double.valueOf(data.getLon());
				animateToCloseLocation(latitue,longitude);
				ToolUtil.saveCurrentLoc(data.getName(),latitue,longitude);
			} else {
				mCurLocation.setText(ToolUtil.getCurrentLoc());
				closeToLoc();
			}
			
		}
	}
	private void closeToLoc(){
		if(!ToolUtil.getCurrentLoc().equals(mContext.getResources().getString(R.string.maps_words))){
			animateToCloseLocation(ToolUtil.getCurLocLat(),ToolUtil.getCurLocLon());
		}else {
			getMyLocation();
		}
	}
	/**
	 * 
	 * @param data
	 */
	private void animateToCloseLocation(Double lat,Double lon) {
		LatLng latLng = new LatLng(
				lat, lon);
		if (googleMap!=null) {
			googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
			googleMap.moveCamera(CameraUpdateFactory
					.newLatLngZoom(latLng, 10));
		}
	}

	private void getMyLocation() {
		if (googleMap != null) {
			googleMap.setMyLocationEnabled(true);
			googleMap
					.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {

						@Override
						public void onMyLocationChange(Location mLocation) {
							if (mLocation != null) {
								double lati = mLocation.getLatitude();
								double lon = mLocation.getLongitude();
								Log.i("getMyLocation", lati
										+ "," + lon);
								if (lat == null) {
									myLatLng = new LatLng(lati,
											lon);
									ToolUtil.saveMyLatingLoc(lati, lon);
									if (mCurLocation.getText().toString().equals(mContext.getResources().getString(R.string.maps_words))) {
										ToolUtil.saveCurrentLoc(mContext.getResources().getString(R.string.maps_words),lati,lon);
										googleMap.moveCamera(CameraUpdateFactory
												.newLatLngZoom(myLatLng, 10));
									}
									new GooglePresenter().getAddress(lati,lon);
									Log.i("myLatLng", "" + myLatLng);
									if(datas.size()>0){
										datasToList.clear();
										datasToList.addAll(datas);
										datasToList.remove(0);
									}
									ChargerLocationData closeLocData = GoogleUtil
											.getTheClosestPoint(datasToList, myLatLng);
									Logg.i("closeLocData", ""+closeLocData);
									if (closeLocData != null) {
										Double latitue = Double
												.valueOf(closeLocData.getLat());
										Double longitude = Double
												.valueOf(closeLocData.getLon());
										lat = new LatLng(latitue, longitude);
										googleMap
												.animateCamera(CameraUpdateFactory
														.newLatLng(lat));
										showInfoWindow(closeLocData.getName());
									}
									calculatDistance(datas);
								}
							}

						}
					});
		}else {
			
		}

	}


	/**
	 * show icon
	 */
	private void showMenuImg() {
		((MainActivity) mContext).setActionBarTitle(getResources().getString(
				R.string.chargig_location_map));
		mToolbar = (Toolbar) ((MainActivity) mContext)
				.findViewById(R.id.toolbar);
		Logg.i("mToolbar", "" + mToolbar);
		mToolbar.inflateMenu(R.menu.menu_nearby_location);
		mNearByLocIcon = mToolbar.findViewById(R.id.nearByLocationIcon);
		Logg.i("mNearByLocIcon", "" + mNearByLocIcon);
		mToolbar.setNavigationIcon(R.drawable.ic_back);// left icon
		mNearByLocIcon.setOnClickListener(this);

	}

	private void listener() {
		mCurLocation.setOnClickListener(this);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Logg.i(TAG + "onDestroyView-mToolbar", "" + mToolbar);
		if (mToolbar != null) {
			mToolbar.getMenu().removeItem(R.id.nearByLocationIcon);
			mToolbar = null;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		Logg.i(TAG + "onPause", "onPause");
		mapView.onPause();
		lat = null;
		Logg.i(TAG + "onPause-mToolbar", "" + mToolbar);
		if (mToolbar != null) {
			mToolbar.getMenu().removeItem(R.id.nearByLocationIcon);
			mToolbar = null;
		}
		Logg.i(TAG + "onPause2-mToolbar", "" + mToolbar);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}

	// ///////////////////////////////
	// code for route planner
	// ///////////////////////////////

	private void draw(PolylineOptions lineOptions) {
		if (lineOptions != null) {
			googleMap.addPolyline(lineOptions);
		}
		googleMap.addPolyline(lineOptions);
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(lineOptions
				.getPoints().get(0)));
		// LatLng local = new LatLng(googleMap.getMyLocation().getLatitude(),
		// googleMap.getMyLocation().getLongitude());
		// googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(local, 10));
	}

	@Override
	public boolean onBackPress() {

		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.currentLocation:
			dialog = new ChooseLocationDialog(mContext, this);
			dialog.show();
			break;
		case R.id.nearByLocationIcon:// intent to NearByLocationsFragment
			intentToNearByLocationFragment();
			break;
		default:
			break;
		}
	}

	private void intentToNearByLocationFragment() {
		if (mToolbar != null) {
			mToolbar.getMenu().removeItem(R.id.nearByLocationIcon);
			mToolbar = null;
		}
		NearByLocationsFragment fragment = new NearByLocationsFragment();
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Bundle bundle = new Bundle();
		Log.i("nBDatas.size()", "" + nBDatas.size());
		bundle.putSerializable("ChargerLocationData", (Serializable) nBDatas);
		fragment.setArguments(bundle);
		ft.replace(R.id.container, fragment, NearByLocationsFragment.TAG)
				.addToBackStack(null).commit();
	}

	@Override
	public void setCurrentLocation(ChargerLocationData data) {
		if (data!=null) {
			mCurLocation.setText(data.getName());
			calculatDistance(datas);
			if (data.getName().equals(
					mContext.getResources().getString(R.string.maps_words))) {
				Logg.i("setCurrentLocation--myLoc", ""+data.getName());
				ToolUtil.saveCurLocAddress(ToolUtil.getMyLocAddress());
//				loadLocData(data.getId());
				lat = null;
			} else {
				showInfoWindow(data.getName());
				Double latitue = Double.valueOf(data.getLat());
				Double longitude = Double.valueOf(data.getLon());
				animateToCloseLocation(latitue,longitude);
				Logg.i("setCurrentLocation", ""+data.getName());
//				ToolUtil.saveCurLocAddress(data.getName());
				loadLocData(data.getId());
			}
			Logg.i("setCurrentLocation-ChargerLocationData", ""+data);
			ToolUtil.saveCurrentLoc(data.getName(),data.getLat(),data.getLon());
		}
		
	}

	/**
	 * 显示出当前location infowindow
	 * @param name
	 */
	private void showInfoWindow(String name) {
		if (markMap.size()>0) {//
			Marker marker = markMap.get(name);
			if (marker!=null) {
				if (!marker.isInfoWindowShown()) {
					marker.showInfoWindow();
				}
			}
		}
	}
	private void loadLocData(String locId) {
		HashMap<String, String> locInfMap = new HashMap<String, String>();
		locInfMap.put("key", locId);
		locInfoPresenter.loadData(locInfMap);
	}
	@Override
	public void dismissChooseLocationDialog() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	@Override
	public void dismiss() {
		if (mDialog != null) {
			mDialog.dismiss();
		}
		if (netAlertDialog != null) {
			netAlertDialog.dismiss();
		}
	}

	@Override
	public void showDialog() {
		if (mDialog == null) {
			mDialog = new ProgressDialog(mContext);
			mDialog.setMessage(mContext.getResources().getString(
					R.string.loading));
		}
		mDialog.show();
	}

	@Override
	public void setLocMarkToMap(final List<ChargerLocationData> datas) {
		// set mark on the map
		if (datas != null && datas.size() > 0) {
			this.datas.clear();
			if (this.datas.size() == 0) {
				ChargerLocationData locationData = new ChargerLocationData();
				locationData.setName(mContext.getResources().getString(
						R.string.maps_words));
				locationData.setLat(ToolUtil.getMyLat());
				locationData.setLon(ToolUtil.getMyLon());
				Logg.i("setLocMarkToMap-locationData", ""+locationData);
				this.datas.add(locationData);
				this.datas.addAll(datas);
			}
			this.locDatas = datas;
//			getMyLocation();
			closeToLoc();
			calculatDistance(datas);
			for (int i = 0; i < datas.size(); i++) {
				ChargerLocationData chargerLocationData = datas.get(i);
				if (chargerLocationData != null) {
					locMap.put(chargerLocationData.getName(),
							chargerLocationData);
				}
			}
			if (googleMap != null) {
				for (int i = 0; i < datas.size(); i++) {
					ChargerLocationData data = datas.get(i);
					if (data!=null) {
						markMap.put(data.getName(),addMark(data));
						markListener();
					}
				}
			}
			showInfoWindow(ToolUtil.getCurrentLoc());
		} else {
			Log.e(TAG + "--setLocMarkToMap()",
					"List<ChargerLocationData> is null ");
		}
	}

	/**
	 * 
	 * @param data
	 */
	private Marker addMark(ChargerLocationData data) {
		if (data != null) {
			MarkerOptions markerOptions = new MarkerOptions()
			.position(
					new LatLng(data.getLat(), data.getLon()))
			.title(data.getName());
			if (data.getStatus() == ChargerLocationData.ERROR) {
				markerOptions.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
			}else if (data.getStatus()  == ChargerLocationData.OK) {
				markerOptions.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
			} else if (data.getStatus() == ChargerLocationData.WARNING) {
				markerOptions.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
			}else if (data.getStatus() == ChargerLocationData.FAULT) {
				markerOptions.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED));
			}else if (data.getStatus() == ChargerLocationData.EMERGENCY) {
				markerOptions.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
			}
			Marker marker = googleMap.addMarker(markerOptions);
			return marker;
		}
		return null;
	}

	private void markListener() {
		if (googleMap!=null) {
			googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
				
				@Override
				public void onInfoWindowClick(Marker arg0) {
					if (myLatLng!=null) {
						ChargingLocPopuView locPopuView = new ChargingLocPopuView(
								mContext, mLocPopviewLayout,
								locMap.get(arg0.getTitle()), myLatLng,
								new PopViewClickListener());
						Logg.i("myLatLng.latitude", ""+myLatLng.latitude);
						Logg.i("myLatLng.longitude", ""+myLatLng.longitude);
						locPopuView.showPopViewWindow();
					}
				}
			});
		}
	}

	private class PopViewClickListener implements
			ChargingLocPopuView.ClickListener {

		@Override
		public void onClick(ChargerLocationData data) {
			intentToLocationFragment(data);
		}
	}

	@Override
	public List<ChargerLocationData> getLocationDatas() {
		return datas;
	}

	/**
	 * 计算点击的location与current location间距离
	 * @param locDatas
	 */
	private void calculatDistance(List<ChargerLocationData> locDatas) {
		if (locDatas != null) {
			ArrayList<ChargerLocationData> locList = new ArrayList<ChargerLocationData>();
			locList.addAll(locDatas);
			Log.i("calculatDistance locDatas.size()", "" + locDatas.size());
			List<ChargerLocationData> list = new ArrayList<ChargerLocationData>();
			myLatLng = findMyLatLng(locList, mCurLocation.getText().toString()
					.trim());
			for (int i = 0; i < locList.size(); i++) {
				ChargerLocationData locationData = locList.get(i);
				if (myLatLng != null) {
					Double distance = GoogleUtil.getDistanceString(
							myLatLng,
							new LatLng(locationData.getLat(), locationData
									.getLon()));
					locationData.setDistance(distance);
					if (!locationData.getName().equals(mContext.getResources().getString(R.string.maps_words))) {
						list.add(locationData);
					}
				}
			}
			if (nBDatas != null && nBDatas.size() > 0) {
				nBDatas.clear();
			}
			nBDatas = GoogleUtil.compare(list);
		}
	}

	/**
	 * 查找current location的lating
	 * @param locList
	 * @param curLocation
	 * @return
	 */
	private LatLng findMyLatLng(ArrayList<ChargerLocationData> locList,
			String curLocation) {
		if (locList != null) {
			for (int i = 0; i < locList.size(); i++) {
				ChargerLocationData locationData = locList.get(i);
				if (locationData.getName().equals(curLocation)) {
					Logg.i("curLocation", ""+curLocation);
					locList.remove(locationData);
					LatLng myLatLng = new LatLng(locationData.getLat(),
							locationData.getLon());
					Logg.i("locationData.getLat()", ""+locationData.getLat());
					Logg.i("locationData.getLon()", ""+locationData.getLon());
					return myLatLng;
				}
			}
		}
		return myLatLng;
	}

	private void intentToLocationFragment(ChargerLocationData data) {
		if (data != null) {
			if (mToolbar != null) {
				mToolbar.getMenu().removeItem(R.id.nearByLocationIcon);
				mToolbar = null;
			}
			Bundle bundle = new Bundle();
			bundle.putSerializable("ChargerLocationData", data);
			LocationFragment fragment = new LocationFragment();
			fragment.setArguments(bundle);
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.container, fragment, LocationFragment.TAG)
					.addToBackStack(null).commit();
		}
	}

	/**
	 * 检测gps
	 */
	public void GoogleApiClient() {
		googleApiClient = new GoogleApiClient.Builder(mContext)
				.addApi(LocationServices.API).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).build();
		googleApiClient.connect();
		LocationRequest locationRequest = new LocationRequest();
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//高精度，开gps
		LocationSettingsRequest.Builder build = new LocationSettingsRequest.Builder()
				.addLocationRequest(locationRequest);
		build.setAlwaysShow(true);//点击了never，还是会显示dialog
		PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
				.checkLocationSettings(googleApiClient, build.build());
		result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

			@Override
			public void onResult(LocationSettingsResult arg0) {
				Status status = arg0.getStatus();
				LocationSettingsStates locationSettingsStates = arg0
						.getLocationSettingsStates();
				switch (status.getStatusCode()) {
				case LocationSettingsStatusCodes.SUCCESS:

					break;
				case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
					try {
						status.startResolutionForResult(mContext,
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
		Logg.i("onActivityResult", "onActivityResult");
		switch (requestCode) {
		case REQUEST_CHECK_SETTINGS:
			switch (resultCode) {
			case Activity.RESULT_OK:
				Logg.i("RESULT_OK", "onActivityResult");
				break;

			default:
				break;
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void showNetAlertDialog() {
		if (netAlertDialog == null) {
			netAlertDialog = new NetAlertDialog(mContext,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (presenter != null) {
								presenter.getLocations(ToolUtil.getNetworkId());
							}
						}
					});
		}
		netAlertDialog.show();
	}

	@Override
	public void onConnected(Bundle arg0) {
		Logg.i("onConnected", "onConnected");
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		Logg.i("onConnectionSuspended", "onConnectionSuspended");
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Logg.i("onConnectionFailed", "onConnectionFailed");
	}

	@Override
	public void getLocationInfoSuccess(LocationInfoData data) {
		if(data!=null){
			ToolUtil.saveCurLocAddress(GoogleUtil.getAddress(data));//可能要修改下GooglePresenter中-onResponse-ToolUtil.saveCurLocAddress(result.getResults().get(0).getFormatted_address());
		}
		
	}

	@Override
	public void getLocationInfoFailed() {
		// TODO Auto-generated method stub
		
	}

}
