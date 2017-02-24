package com.delta.smsandroidproject.view.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.adapter.MySpinnerAdpater;
import com.delta.smsandroidproject.bean.ChargerInfoData;
import com.delta.smsandroidproject.bean.ChargerListData;
import com.delta.smsandroidproject.bean.ChargerListData.Evse;
import com.delta.smsandroidproject.bean.ChargerLocationData;
import com.delta.smsandroidproject.dialog.ActionDialog;
import com.delta.smsandroidproject.dialog.ChargingFunctionDialog;
import com.delta.smsandroidproject.dialog.NetAlertDialog;
import com.delta.smsandroidproject.presenter.CF_getLocationListPresenter;
import com.delta.smsandroidproject.presenter.GetChargerInfoPresenter;
import com.delta.smsandroidproject.presenter.GetChargerInfoPresenter.IGetChargerInfo;
import com.delta.smsandroidproject.presenter.GetChargerListPresenter;
import com.delta.smsandroidproject.presenter.GetChargerListPresenter.IGetChargerList;
import com.delta.smsandroidproject.presenter.ipresenter.IPresenter;
import com.delta.smsandroidproject.util.GoogleUtil;
import com.delta.smsandroidproject.util.NetConnectTools;
import com.delta.smsandroidproject.util.ToastCustom;
import com.delta.smsandroidproject.view.activity.MainActivity;
import com.delta.smsandroidproject.view.iview.ICF_getLocationList;
import com.delta.smsandroidproject.view.iview.ILoadView;

public class ChargingFunctionFragment extends BaseFragment implements
		OnClickListener, OnItemSelectedListener, ICF_getLocationList,
		IGetChargerInfo, IGetChargerList, ILoadView,
		android.content.DialogInterface.OnClickListener {

	public static final String TAG = "ChargingFunctionFragment";
	private RadioButton Event, Info, Map, Action;
	private Spinner sp_evse, sp_charger, sp_location;
	private ChargingFunctionDialog dialog;
	private String[] data = new String[] { "", "aa", "bb", "cc" };
	private ArrayAdapter<String> provinceAdapter = null;
	private IPresenter locationPresenter, evsePresenter, chargerInfoPresenter;
	private List<String> locationName, locationId, chargerInfoName,
			chargerInfoID, evseID, evseName;
	private ArrayList<String> toEventEvse;
	private boolean loc_isFirst = true;
	private boolean char_isFirst = true;
	private boolean evse_isFirst = true;
	private ArrayList<ChargerLocationData> locationList;
	private ArrayList<ChargerInfoData> chargerList;
	private int locationPosition, chargerPosition, evsePosition,
			m_chargerPostion = 0, m_evsePosition = 0;
	private LinearLayout cf_linear;

	private java.util.Map<String, String> evseMap, charMap;
	private Bundle outState;
	private MySpinnerAdpater locationAdapter, chargerAdapter, evseAdapter;
	private LinearLayout zw_location, zw_charger, zw_evse;
	private ProgressDialog mDialog;
	private NetAlertDialog netDialog;
	private ChargerListData chargerListData, chargerListData_n;
	private boolean isBack = false;

	public ChargingFunctionFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);

	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		View view = inflater.inflate(R.layout.charging_functions, container,
				false);
		initView(view);

		initPresenter();
		if (outState == null) {
			outState = new Bundle();
			initList();
			sp_charger.setAdapter(chargerAdapter);
			sp_location.setAdapter(locationAdapter);
			sp_evse.setAdapter(evseAdapter);
		} else {
			isBack = true;
			locationPosition = outState.getInt("locationPosition");
			chargerPosition = outState.getInt("chargerPosition");
			evsePosition = outState.getInt("evsePosition");
			loc_isFirst = outState.getBoolean("loc_isFirst");
			char_isFirst = outState.getBoolean("char_isFirst");
			evse_isFirst = outState.getBoolean("evse_isFirst");
			locationName = outState.getStringArrayList("locationName");
			locationId = outState.getStringArrayList("locationId");
			chargerInfoName = outState.getStringArrayList("chargerInfoName");
			chargerInfoID = outState.getStringArrayList("chargerInfoID");
			evseID = outState.getStringArrayList("evseID");
			evseName = outState.getStringArrayList("evseName");
			m_chargerPostion = outState.getInt("chargerPosition");
			m_evsePosition = outState.getInt("evsePosition");
			sp_charger.setAdapter(chargerAdapter);
			sp_location.setAdapter(locationAdapter);
			sp_evse.setAdapter(evseAdapter);
			chargerAdapter.setList(chargerInfoName);
			locationAdapter.setList(locationName);
			evseAdapter.setList(evseName);
			// // sp_charger.setSelection(chargerPosition);
			// // sp_evse.setSelection(evsePosition);
			// sp_location.setSelection(locationPosition);

		}

		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO 自动生成的方法存根

		super.onSaveInstanceState(outState);
	}

	private void initList() {
		locationName = new ArrayList<>();
		locationId = new ArrayList<>();
		chargerInfoName = new ArrayList<>();
		chargerInfoID = new ArrayList<>();
		evseID = new ArrayList<>();
		evseName = new ArrayList<>();
		toEventEvse = new ArrayList<>();
		locationName.add("");
		locationId.add("");
		chargerInfoName.add("");
		chargerInfoID.add("");
		evseID.add("");
		evseName.add("");
	}

	private void initPresenter() {
		locationAdapter = new MySpinnerAdpater(getActivity(), getActivity()
				.getResources().getString(R.string.Select_Location));
		chargerAdapter = new MySpinnerAdpater(getActivity(), getActivity()
				.getResources().getString(R.string.Select_Charger));
		evseAdapter = new MySpinnerAdpater(getActivity(), getActivity()
				.getResources().getString(R.string.Select_EVSE));

		locationPresenter = new CF_getLocationListPresenter(this, this,
				getActivity());
		chargerInfoPresenter = new GetChargerInfoPresenter(this, this,
				getActivity());
		evsePresenter = new GetChargerListPresenter(this, this, getActivity());
		locationPresenter.loadData(null);
	}

	@Override
	public void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
		((MainActivity) getActivity()).getSupportActionBar().setTitle(
				getActivity().getResources().getString(
						R.string.title_charging_fuctions));

	}

	private void initView(View view) {
		netDialog = new NetAlertDialog(getActivity(), this);
		provinceAdapter = new ArrayAdapter<String>(getActivity(),
				R.layout.item_location, R.id.location, data);
		evseMap = new HashMap<String, String>();
		charMap = new HashMap<String, String>();
		cf_linear = (LinearLayout) view.findViewById(R.id.cf_linear);

		dialog = new ChargingFunctionDialog(getActivity());
		dialog.setCanceledOnTouchOutside(true);
		Event = (RadioButton) view.findViewById(R.id.cf_rd_event);

		Info = (RadioButton) view.findViewById(R.id.cf_rd_info);
		Map = (RadioButton) view.findViewById(R.id.cf_rd_map);
		Action = (RadioButton) view.findViewById(R.id.cf_rd_action);
		Action.setVisibility(View.INVISIBLE);
		Event.setVisibility(View.INVISIBLE);
		sp_charger = (Spinner) view.findViewById(R.id.cf_sp_charger);
		sp_evse = (Spinner) view.findViewById(R.id.cf_sp_evse);
		sp_location = (Spinner) view.findViewById(R.id.cf_sp_location);
		zw_charger = (LinearLayout) view.findViewById(R.id.zw_charger);
		zw_evse = (LinearLayout) view.findViewById(R.id.zw_evse);
		zw_location = (LinearLayout) view.findViewById(R.id.zw_location);

		zw_charger.setOnClickListener(this);
		zw_evse.setOnClickListener(this);
		zw_location.setOnClickListener(this);
		sp_charger.setOnItemSelectedListener(this);
		sp_location.setOnItemSelectedListener(this);
		sp_evse.setOnItemSelectedListener(this);
		sp_evse.setAdapter(provinceAdapter);
		Action.setVisibility(View.INVISIBLE);
		Event.setVisibility(View.INVISIBLE);
		cf_linear.setVisibility(View.GONE);
		Action.setOnClickListener(this);
		Event.setOnClickListener(this);

		Info.setOnClickListener(this);
		Map.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		if (!NetConnectTools.isNetworkAvailable(getActivity())) {
			netDialog.show();
			return;
		}
		switch (v.getId()) {

		case R.id.cf_rd_info:
			intentToLocationFragment();

			break;
		case R.id.cf_rd_event:
			intentToEventLogFragment();
			break;
		case R.id.cf_rd_action:
			ActionDialog dialog = new ActionDialog(getContext(),
					R.style.radius_dialog);
			if (evsePosition == 0) {
				Evse e = null;
				if (chargerList!=null) {
					dialog.setData(chargerList.get(chargerPosition - 1),
							chargerListData, e);
				}
				
			} else {
				
				if (chargerList!=null&&chargerListData!=null) {
					dialog.setData(chargerList.get(chargerPosition - 1),
							chargerListData,
							chargerListData.getEvse()[evsePosition - 1]);
				}
				
			}
			dialog.show();
			break;
		case R.id.cf_rd_map:
			intentToChargerMapFragment();
			break;
		case R.id.zw_charger:
			if (NetConnectTools.isNetworkAvailable(getActivity())) {
				Toast.makeText(getActivity(), R.string.no_location,
						Toast.LENGTH_SHORT).show();
			} else {
				netDialog.show();
			}
			break;
		case R.id.zw_location:
			if (NetConnectTools.isNetworkAvailable(getActivity())) {
			} else {
				netDialog.show();
			}
			break;
		case R.id.zw_evse:
			if (NetConnectTools.isNetworkAvailable(getActivity())) {
				Toast.makeText(getActivity(), R.string.no_charger,
						Toast.LENGTH_SHORT).show();
			} else {
				netDialog.show();
			}
			break;
		default:
			break;
		}
	}

	private void intentToLocationFragment() {
		Bundle bundle = new Bundle();
		if (locationList != null) {
			bundle.putSerializable("ChargerLocationData",
					locationList.get(locationPosition - 1));
		}
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		LocationFragment fragment = new LocationFragment();
		fragment.setArguments(bundle);

		ft.replace(R.id.container, fragment)
				.addToBackStack(LocationFragment.TAG).commit();

	}

	private void intentToEventLogFragment() {
		Bundle bundle = new Bundle();
		EventLogPagerFragment eventFragment = new EventLogPagerFragment();
		bundle.putString("LocationId", locationId.get(locationPosition));
		bundle.putString("LocationName", locationName.get(locationPosition));
		bundle.putString("ChargerID", chargerInfoID.get(chargerPosition));
		if (chargerListData_n!=null) {
			if (!TextUtils.isEmpty(chargerListData_n.getType())) {
				bundle.putString("type", chargerListData_n.getType());
			}
		}
		bundle.putStringArrayList("evse", toEventEvse);
		bundle.putSerializable("ChargerListData", chargerListData_n);
		eventFragment.setArguments(bundle);
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.container, eventFragment)
				.addToBackStack(LocationFragment.TAG).commit();
	}

	@Override
	public void onDestroyView() {
		// TODO 自动生成的方法存根
		outState.putBoolean("loc_isFirst", loc_isFirst);
		outState.putBoolean("char_isFirst", char_isFirst);
		outState.putBoolean("evse_isFirst", evse_isFirst);
		outState.putStringArrayList("locationName",
				(ArrayList<String>) locationName);
		outState.putStringArrayList("locationId",
				(ArrayList<String>) locationId);
		outState.putStringArrayList("chargerInfoName",
				(ArrayList<String>) chargerInfoName);
		outState.putStringArrayList("chargerInfoID",
				(ArrayList<String>) chargerInfoID);
		outState.putStringArrayList("evseID", (ArrayList<String>) evseID);
		outState.putStringArrayList("evseName", (ArrayList<String>) evseName);
		outState.putInt("locationPosition", locationPosition);
		outState.putInt("chargerPosition", chargerPosition);
		outState.putInt("evsePosition", evsePosition);
		isBack = false;
		chargerList = null;
		chargerListData_n = null;
		super.onDestroyView();
	}

	private void intentToChargerMapFragment() {
		if (GoogleUtil.isSupportGoogMap()) {
			Bundle bundle = new Bundle();
			if (locationList != null) {
				bundle.putSerializable("location",
						locationList.get(locationPosition - 1));
			}
			ChargerMapFragment mapFragment = new ChargerMapFragment();
			mapFragment.setArguments(bundle);
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();

			ft.replace(R.id.container, mapFragment)
					.addToBackStack(LocationFragment.TAG).commit();
		} else {
			ToastCustom.showToast(getActivity(), getActivity().getResources()
					.getString(R.string.goolge_map_no_support),
					ToastCustom.LENGTH_SHORT);
		}

	}

	@Override
	public boolean onBackPress() {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public void onPause() {
		// TODO 自动生成的方法存根
		super.onPause();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO 自动生成的方法存根
		switch (parent.getId()) {

		case R.id.cf_sp_location:
			if (position == 0) {
				cf_linear.setVisibility(View.GONE);
				sp_location.setSelection(locationPosition);

			} else {
				// chargerPosition = 0;
				// sp_charger.setSelection(0);
				// sp_evse.setSelection(0);
				// chargerAdapter.notifyDataSetChanged();
				locationPosition = position;
				charMap.put("key", locationId.get(locationPosition));
				chargerInfoPresenter.loadData(charMap);
				cf_linear.setVisibility(View.VISIBLE);
				zw_location.setVisibility(View.GONE);
			}

			break;
		case R.id.cf_sp_charger:

			if (position == 0) {
				sp_evse.setSelection(0);
				// if (sp_location.getSelectedItemPosition()==0) {
				// zw_charger.setVisibility(View.VISIBLE);
				// }else{
				// zw_charger.setVisibility(View.GONE);
				// }
				zw_charger.setVisibility(View.GONE);
				Action.setVisibility(View.INVISIBLE);
				Event.setVisibility(View.INVISIBLE);
				if (m_chargerPostion != 0) {
					sp_charger.setSelection(m_chargerPostion);
					Action.setVisibility(View.VISIBLE);
					Event.setVisibility(View.VISIBLE);
					m_chargerPostion = 0;
					return;
				}
				chargerPosition = position;
				// sp_charger.setSelection(chargerPosition);
			} else {
				// sp_evse.setSelection(0);
				chargerPosition = position;
				Action.setVisibility(View.VISIBLE);
				Event.setVisibility(View.VISIBLE);
				evseMap.put("key", chargerInfoID.get(chargerPosition));
				evsePresenter.loadData(evseMap);
				zw_charger.setVisibility(View.GONE);
			}
			break;
		case R.id.cf_sp_evse:
			if (position == 0) {
				if (sp_charger.getSelectedItemPosition() == 0) {
					zw_evse.setVisibility(View.VISIBLE);
				} else {
					zw_evse.setVisibility(View.GONE);
				}

				if (m_evsePosition != 0) {
					sp_evse.setSelection(m_evsePosition);
					m_evsePosition = 0;
					return;
				}
				// sp_evse.setSelection(evsePosition);
				evsePosition = position;
				chargerListData = null;
			} else {
				chargerListData = chargerListData_n;
				evsePosition = position;
				zw_evse.setVisibility(View.GONE);
			}

			break;
		default:
			break;
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO 自动生成的方法存根
	}

	@Override
	public void getLocationListSuccess(ArrayList<ChargerLocationData> datas) {
		// TODO 自动生成的方法存根
		locationList = datas;
		locationName.clear();
		locationId.clear();
		locationName.add("");
		locationId.add("");
		for (ChargerLocationData d : datas) {
			locationName.add(d.getName());
			locationId.add(d.getId());
		}
		zw_location.setVisibility(View.GONE);
		locationAdapter.setList(locationName);
		// sp_location.setAdapter(locationAdapter);
		// sp_charger.setSelection(0);
		// sp_evse.setSelection(0);

	}

	@Override
	public void getLocationListFailed() {
		// TODO 自动生成的方法存根
		mDialog.dismiss();

		Toast.makeText(getActivity(),
				getActivity().getResources().getString(R.string.load_failed),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void getChargerInfoSuccess(ArrayList<ChargerInfoData> datas) {
		chargerList = datas;
		// TODO 自动生成的方法存根
		chargerInfoName.clear();
		chargerInfoID.clear();
		chargerInfoName.add("");
		chargerInfoID.add("");
		for (ChargerInfoData data : datas) {

			chargerInfoName.add(data.getName());
			chargerInfoID.add(data.getId());
		}
		chargerAdapter.setList(chargerInfoName);
		sp_charger.setSelection(0);
		zw_charger.setVisibility(View.GONE);
	}

	@Override
	public void getChargerInfoFailed() {
		// TODO 自动生成的方法存根
		mDialog.dismiss();
		Toast.makeText(getActivity(),
				getActivity().getResources().getString(R.string.load_failed),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStop() {
		// TODO 自动生成的方法存根
		loc_isFirst = true;
		char_isFirst = true;
		evse_isFirst = true;
		super.onStop();
	}

	@Override
	public void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		outState = null;
	}

	@Override
	public void getChargerListSuccess(ChargerListData datas) {
		// TODO 自动生成的方法存根
		this.chargerListData = datas;
		this.chargerListData_n = datas;
		evseID.clear();
		evseID.add("");
		evseName.clear();
		evseName.add("");
		toEventEvse.clear();
		if (datas.getEvse() != null) {
			for (Evse e : datas.getEvse()) {
				evseID.add(e.getId());
				evseName.add(e.getName());
				toEventEvse.add(e.getId());
			}

		}
		zw_evse.setVisibility(View.GONE);
		evseAdapter.setList(evseName);
	}

	@Override
	public void getChargerListFailed() {
		// TODO 自动生成的方法存根
		mDialog.dismiss();
		Toast.makeText(getActivity(),
				getActivity().getResources().getString(R.string.load_failed),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showLoading() {
		if (mDialog == null) {
			mDialog = new ProgressDialog(getActivity());
			mDialog.setCancelable(false);
			mDialog.setMessage(getActivity().getResources().getString(
					R.string.loading));
		}
		mDialog.show();
	}

	@Override
	public synchronized void dissLoading() {
		// TODO 自动生成的方法存根
		if (isBack) {

			if (chargerPosition == 0 && chargerList != null) {
				mDialog.dismiss();
				isBack = false;
				return;
			} else {
				// return;
			}
			if (chargerPosition != 0
					&& sp_charger.getSelectedItemPosition() == chargerPosition
					&& chargerListData_n != null) {
				mDialog.dismiss();
				isBack = false;
				return;
			} else {
				return;
			}
		}
		mDialog.dismiss();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO 自动生成的方法存根
		netDialog.dismiss();
	}
}
