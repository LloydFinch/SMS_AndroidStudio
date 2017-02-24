package com.delta.smsandroidproject.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.bean.ChargerListData;
import com.delta.smsandroidproject.bean.EventListData;
import com.delta.smsandroidproject.util.Logg;
import com.delta.smsandroidproject.view.fragment.EventLogEvseFragment;

public class EventLogPagerAdapter extends FragmentPagerAdapter{
	private static final String TAG = "EventLogPagerAdapter";
	private static final int POSITION_ALL = 0;
	private static final int POSITION_CHARGER = 1;
	private static final int POSITION_EVSE = 2;
	private static final int PAGER_COUNT = 3;
	
	private String[] title = null;
	private Context mContext;
	private List<EventListData> datas;
	private List<EventListData> chargerIdDatas;
	private List<EventListData> evseIdDatas;
	private FragmentManager fm;
	private FragmentTransaction transaction;
	private String locationId;
	private List<Fragment> fragments ;
	private ViewPager viewPager;
	private String ODERBY;
	private String chargerid;
	private List<String> evseList;
	private Fragment curFragment = null;
	
	public EventLogPagerAdapter(FragmentManager fm,Context context, String locationId,String chargerid,ArrayList<String> evseList, List<String> evseTypeList, ChargerListData chargerListData, List<Fragment> fragments) {
		super(fm);
		this.mContext = context;
		this.fm = fm;
		this.locationId = locationId;
		this.chargerid= chargerid;
		this.evseList = evseList;
		this.fragments = fragments;
		Logg.i(TAG+"fragments.size()", ""+fragments.size());
		if (evseList!=null&&evseList.size()>0) {
			title = new String[evseList.size()+1];
			title[0] = mContext.getResources().getString(R.string.event_log_charger);
			if (evseTypeList!=null&&evseTypeList.size()>0) {
				if (evseList!=null) {
					for (int i = 1; i < evseList.size()+1; i++) {
						StringBuffer buffer = new StringBuffer();
						String evseId = evseList.get(i-1);
						buffer.append(mContext.getResources().getString(R.string.event_log_evse)+evseId);
						buffer.append("\n");
						buffer.append("(");
						buffer.append(evseTypeList.get(i-1));
						buffer.append(")");
						title[i] = buffer.toString();
					}
				}
			}
		}
	}
	
	@Override
	public Fragment getItem(int arg0) {
		Logg.i(TAG+"getItem", ""+arg0);
		Bundle bundle = bundle = new Bundle();
		bundle.putString("LocationId", locationId);
		bundle.putString("ChargerID", chargerid);
		curFragment = fragments.get(arg0);
		if (evseList!=null&&arg0!=0) {
			String arg2 = arg0+"";
			bundle.putString("EvseId", evseList.get(arg0-1));
			Logg.i(TAG+"EvseId", ""+evseList.get(arg0-1));
			if (curFragment instanceof EventLogEvseFragment && arg2.equals(evseList.get(arg0-1))) {
				((EventLogEvseFragment) curFragment).setTag(EventLogEvseFragment.TAG+evseList.get(arg0-1));
				Logg.i(TAG+"getTag", ""+((EventLogEvseFragment) curFragment).getTag1());
			}
		}
		curFragment.setArguments(bundle);
		return curFragment;
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return title==null?"":title[position];
	}
	
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}
	
	private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }
	
}
