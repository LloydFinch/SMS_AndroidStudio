package com.delta.smsandroidproject.view.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.bean.ChargerListData;
import com.delta.smsandroidproject.bean.ChargerListData.Evse;
import com.delta.smsandroidproject.bean.ChargerListData.Evse.Connectors;
import com.delta.smsandroidproject.bean.EventListData;
import com.delta.smsandroidproject.dialog.EvenlogOderbyDialog;
import com.delta.smsandroidproject.dialog.EvenlogOderbyDialog.RadioButtonCheckListener;
import com.delta.smsandroidproject.dialog.EventlogTypeFilterDialog;
import com.delta.smsandroidproject.popupview.EventLogTimePopuView;
import com.delta.smsandroidproject.presenter.EventPresenter;
import com.delta.smsandroidproject.util.Logg;
import com.delta.smsandroidproject.util.ToolUtil;
import com.delta.smsandroidproject.view.activity.MainActivity;
import com.delta.smsandroidproject.view.adapter.EventLogPagerAdapter;

/**
 * event log fragment
 * @author Jianzao.Zhang
 *
 */
public class EventLogPagerFragment extends BaseFragment implements OnClickListener, OnPageChangeListener{

	private static final int PAGER_COUNT = 3;
	public static final String TAG = "EventLogPagerFragment";
	private ViewPager mPager;
	private TabLayout mTabLayout;
	private Toolbar mToolbar;
	private ProgressDialog mDialog;
	private EventPresenter presenter;
	private FragmentActivity mContext;
	private List<EventListData> datas;
	private EventLogPagerAdapter mAdapter;
	private String locationId;
	private String locationName;
	private List<Fragment> fragments = new ArrayList<>();
	private EvenlogOderbyDialog dialog;
	private int PAGE_NO =1;//第几页
	private int PER_PAGE = 25;//每页25条数据
	private String chargerId;
	public static String ODERBY = EventListData.LEVEL;
	private EventlogTypeFilterDialog mEventlogTypeFilterDialog;
	private ArrayList<String> evseList = new ArrayList<>();
	public static List<String> typeList = new ArrayList<>();
	private ChargerListData chargerListData;
	private List<String> evseTypeList = new ArrayList<>();
	private EventLogTimePopuView mTimePopuView;
	public static String START_TIME = "";
	public static String END_TIEM = "";
	public EventLogPagerFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflate = inflater.inflate(R.layout.fragment_event_log_pager, container,
						false);
		mContext = getActivity();
		Logg.i(TAG, "onCreateView");
//		typeList = getTypeList();
		initData();
		initView(inflate);
		dialog = new EvenlogOderbyDialog(mContext);
		mEventlogTypeFilterDialog = new EventlogTypeFilterDialog(mContext,typeList);
		ODERBY = dialog.getOderby();
		typeList = mEventlogTypeFilterDialog.getEvetType();
		START_TIME = ToolUtil.getFilterStartTime();
		END_TIEM = ToolUtil.getFilterEndTime();
		
		Logg.i(TAG+"typeList.size()",""+typeList.size());
		Logg.i(TAG+"ODERBY",""+ODERBY);
		initListener();
		return inflate;
	}

	private List<String> getTypeList() {
//		typeList.add(EventListData.EMERGENCY);
//		typeList.add(EventListData.FAULT);
//		typeList.add(EventListData.WARNING);
//		typeList.add(EventListData.INFORMATION);
//		typeList.add(EventListData.CONFIGURATION);
//		typeList.add(EventListData.CHARGE);
		
		return typeList;
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	private void initListener() {
		dialog.registerOncheckListener(new RadioButtonCheckListener() {
			
			@Override
			public void onCheckListener(String oderby) {
				Logg.i(TAG+"oderby", ""+oderby);
				ODERBY = oderby;
				PAGE_NO = 1;
				Logg.i(TAG+"PAGE_NO", ""+PAGE_NO);
				for (int i = 0; i < fragments.size(); i++) {
					Fragment fragment = fragments.get(i);
					Logg.i(TAG+"fragment", ""+fragment);
					if (fragment instanceof EventLogChargerFragment) {
						((EventLogChargerFragment) fragment).loadData(chargerId,locationId,oderby,mEventlogTypeFilterDialog.getEvetType(),
								START_TIME,END_TIEM,PAGE_NO,PER_PAGE);
					}else if (fragment instanceof EventLogEvseFragment) {
						if (evseList.size()>0) {
							Logg.i(TAG+"tag", ""+EventLogEvseFragment.TAG+evseList.get(i-1)+"-getTag1()-"+((EventLogEvseFragment) fragment).getTag1());
							if (((EventLogEvseFragment) fragment).getTag1().equals(EventLogEvseFragment.TAG+evseList.get(i-1))) {
								Logg.i(TAG+"fragment", ""+fragment);
								((EventLogEvseFragment) fragment).loadData(
										locationId,chargerId,evseList.get(i-1),oderby,mEventlogTypeFilterDialog.getEvetType(),
										START_TIME,END_TIEM,PAGE_NO,PER_PAGE,EventLogEvseFragment.TAG+i);
							}
						}
					}
				}
				dialog.dismiss();
			}
		});
		mEventlogTypeFilterDialog.registerOncheckListener(new EventlogTypeFilterDialog.RadioButtonCheckListener() {
			
			@Override
			public void onCheckListener(List<String> types) {
				Logg.i(TAG+"types", ""+types);
				PAGE_NO = 1;
				Logg.i(TAG+"PAGE_NO", ""+PAGE_NO);
				for (int i = 0; i < fragments.size(); i++) {
					Fragment fragment = fragments.get(i);
					Logg.i(TAG+"fragment", ""+fragment);
					if (fragment instanceof EventLogChargerFragment) {
						((EventLogChargerFragment) fragment).loadData(chargerId,locationId,ODERBY,mEventlogTypeFilterDialog.getEvetType()
								,START_TIME,END_TIEM,PAGE_NO,PER_PAGE);
						
					}else if (fragment instanceof EventLogEvseFragment) {
						if (((EventLogEvseFragment) fragment).getTag1().equals(EventLogEvseFragment.TAG+evseList.get(i-1))) {
							((EventLogEvseFragment) fragment).loadData(
									locationId,chargerId,evseList.get(i-1),ODERBY,mEventlogTypeFilterDialog.getEvetType(),
									START_TIME,END_TIEM,PAGE_NO,PER_PAGE,EventLogEvseFragment.TAG+i);
						
						}
					}
				}
				mEventlogTypeFilterDialog.dismiss();
			}

		});
	}
	
	private void initData() {
		Bundle bundle = getArguments();
		if (bundle!=null) {
			locationId = bundle.getString("LocationId");
			locationName = bundle.getString("LocationName");
			chargerId = bundle.getString("ChargerID");
			evseList = bundle.getStringArrayList("evse");
			chargerListData = (ChargerListData) bundle.getSerializable("ChargerListData");
			evseTypeList = getEvseType(chargerListData);
			Logg.i(TAG+"chargerId", ""+chargerId);
			Logg.i(TAG+"locationName", ""+locationName);
			Logg.i(TAG+"locationId", ""+locationId);
		}
		fragments.add(new EventLogChargerFragment());
		for (int i = 0; i <evseList.size(); i++) {
			Logg.i(TAG+"evses"+i, ""+evseList.get(i));
			EventLogEvseFragment fragment = new EventLogEvseFragment();
			fragments.add(fragment);
		}
	}
	
	@SuppressWarnings("unused")
	private List<String> getEvseIdList(ChargerListData data){
		if(data!=null){
			List<String> list = new ArrayList<>();
			Evse[] evse = data.getEvse();
			List<String> evseIdList = new ArrayList<>();
			if (evse!=null) {
				for (int i = 0; i < evse.length; i++) {
					evseIdList.add(evse[i].getId());
				}
			}
			return evseIdList;
		}
		return null;
	}
	
	/**
	 * 获取evseTypeList
	 * @param data
	 * @return
	 */
	private List<String> getEvseType(ChargerListData data){
		if(data!=null){
			List<String> list = new ArrayList<>();
			Evse[] evse = data.getEvse();
			if (evse!=null) {
				for (int i = 0; i < evse.length; i++) {
					Connectors[] connector = evse[i].getConnector();
					if(connector!=null){
						for (int j = 0; j < connector.length; j++) {
							String type = connector[j].getName();
							list.add(type);
						}
					}
				}
				return list;
			}
		}
		return null;
		
	}
	private void showTabLayout() {
		mTabLayout = (TabLayout) getActivity().findViewById(R.id.sliding_tabs);
		mTabLayout.setVisibility(View.VISIBLE);
//		mTabLayout.setTabGravity(Gravity.FILL);
//		if(evseList!=null&&evseList.size()>4){
//			mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//		}else {
//			mTabLayout.setTabMode(TabLayout.MODE_FIXED);
//		}
		mTabLayout.setupWithViewPager(mPager);	
		mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
		
		mToolbar.inflateMenu(R.menu.event_log_time_menu);
		View mItemEventLogTime = mToolbar.findViewById(R.id.itemEventLogTime);
		mItemEventLogTime.setOnClickListener(this);
		
		mToolbar.inflateMenu(R.menu.event_log_type_filter_menu);
		View mItemEventLogTpyeFilter = mToolbar.findViewById(R.id.itemEventLogTpyeFilter);
		mItemEventLogTpyeFilter.setOnClickListener(this);
		
		mToolbar.inflateMenu(R.menu.event_log_menu);
		mToolbar.setNavigationIcon(R.drawable.ic_back);//left icon
		View mItemEventLog = mToolbar.findViewById(R.id.itemEventLog);
		mItemEventLog.setOnClickListener(this);
		
	}

	/**
	 * init
	 */
	private void initView(View inflate) {
		FrameLayout mEventTimePopLayout = (FrameLayout) inflate.findViewById(R.id.eventTimePopLayout);
		mTimePopuView = new EventLogTimePopuView(mContext, mEventTimePopLayout, new EventLogTimePopviewClickListener());
		mPager = (ViewPager) inflate.findViewById(R.id.pager);
		mPager.setOffscreenPageLimit(fragments.size());//设置缓存
		mAdapter = new EventLogPagerAdapter(getChildFragmentManager(), mContext, locationId,chargerId, evseList,evseTypeList,chargerListData,fragments);
		mPager.setAdapter(mAdapter);
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(this);
	}
	
	private class EventLogTimePopviewClickListener implements EventLogTimePopuView.ClickListener{

		@Override
		public void onClick(String startTime,String endTime) {
			mTimePopuView.showPopViewWindow();
			START_TIME = startTime;
			END_TIEM = endTime;
			for (int i = 0; i < fragments.size(); i++) {
				Fragment fragment = fragments.get(i);
				Logg.i(TAG+"fragment", ""+fragment);
				if (fragment instanceof EventLogChargerFragment) {
					((EventLogChargerFragment) fragment).loadData(chargerId,locationId,ODERBY,mEventlogTypeFilterDialog.getEvetType(),
							startTime,endTime,PAGE_NO,PER_PAGE);
					
				}else if (fragment instanceof EventLogEvseFragment) {
					if (((EventLogEvseFragment) fragment).getTag1().equals(EventLogEvseFragment.TAG+evseList.get(i-1))) {
						((EventLogEvseFragment) fragment).loadData(
								locationId,chargerId,evseList.get(i-1),ODERBY,mEventlogTypeFilterDialog.getEvetType(),
								startTime,endTime,PAGE_NO,PER_PAGE,EventLogEvseFragment.TAG+i);
					}
				}
			}
		}
	}
	
	@Override
	public boolean onBackPress() {
		return false;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		if (mTabLayout!=null) {
			mTabLayout.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Logg.i(TAG, "onPause");
		if (mToolbar !=null) {
			mToolbar.getMenu().removeItem(R.id.itemEventLogTime);
			mToolbar.getMenu().removeItem(R.id.itemEventLog);
			mToolbar.getMenu().removeItem(R.id.itemEventLogTpyeFilter);
			((MainActivity) getActivity()).setActionBarTitle("","");
			mToolbar = null;
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Logg.i(TAG, "onStop");
		Logg.i(TAG, "onStop"+evseList.size());
		
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Logg.i(TAG, "onDestroyView");
		if (evseList!=null) {
			evseList.clear();
			Logg.i(TAG, "onStop"+evseList.size());
		}
		if (typeList!=null) {
			typeList.clear();
		}
	
	}

	@Override
	public void onResume() {
		super.onResume();
		Logg.i(TAG, "onResume");
		showTabLayout();
		((MainActivity) getActivity()).setActionBarTitle(getActivity()
				.getResources().getString(R.string.event_log),locationName);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.itemEventLogTime:
			mTimePopuView.showPopViewWindow();
			break;
		case R.id.itemEventLog:
			dialog.show();
			break;
		case R.id.itemEventLogTpyeFilter:
			mEventlogTypeFilterDialog.show();
//			if (typeList!=null) {
//				typeList.clear();
//				typeList = mEventlogTypeFilterDialog.getEvetType();
//			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
//		Logg.i("onPageScrollStateChanged", ""+arg0);

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
//		Logg.i("onPageScrolled", ""+arg0);

	}
	
	@Override
	public void onPageSelected(int arg0) {
		Logg.i("onPageSelected", ""+arg0);
	}
}
