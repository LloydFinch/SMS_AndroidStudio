package com.delta.smsandroidproject.view.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.bean.ChargerLocationData;
import com.delta.smsandroidproject.util.Logg;
import com.delta.smsandroidproject.view.activity.MainActivity;
import com.delta.smsandroidproject.view.adapter.NearByLocationAdapter;
import com.delta.smsandroidproject.view.adapter.NearByLocationAdapter.RecycleviewOnItemClick;
import com.delta.smsandroidproject.widget.DividerItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class NearByLocationsFragment extends BaseFragment {

	private List<ChargerLocationData> datas = new ArrayList<ChargerLocationData>();
	public static final String TAG = "NearByLocationsFragment";

	public NearByLocationsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflate = inflater.inflate(R.layout.fragment_near_by_locations, container,
						false);
		Log.i("NearByLocationsFragment", "onCreateView");
		initData();
		initView(inflate);
		listener();
		return inflate;
	}
	
	private void listener() {
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		showIcon();
		Log.i("NearByLocationsFragment", "onResume");
	}
	

	private void initData() {
		Bundle bundle = getArguments();
		if (bundle!=null) {
			datas = (List<ChargerLocationData>) bundle.getSerializable("ChargerLocationData");
		}
	}

	private void showIcon() {
		((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.near_by_location));
	}

	private void initView(View inflate) {
		RecyclerView mRecyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
		StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1,LinearLayoutManager.VERTICAL);
		
		mRecyclerView.setLayoutManager(layoutManager);
		NearByLocationAdapter nearByLocationAdapter = new NearByLocationAdapter(getActivity(),datas);
		mRecyclerView.setAdapter(nearByLocationAdapter);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
		nearByLocationAdapter.setOnItemClick(new RecycleviewOnItemClick(){

			@Override
			public void onItemClick(View v, ChargerLocationData data) {
				Logg.i("ChargerLocationData", ""+data);
				intentToLocationFragment(data);
			}});
	}

	@Override
	public boolean onBackPress() {
		return false;
	}
	
	private void intentToLocationFragment(ChargerLocationData data) {
		if (data != null) {
			Bundle bundle = new Bundle();
			bundle.putSerializable("ChargerLocationData", data);
			LocationFragment fragment = new LocationFragment();
			fragment.setArguments(bundle);
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.container, fragment,TAG ).addToBackStack(null).commit();
		}
	}
}
