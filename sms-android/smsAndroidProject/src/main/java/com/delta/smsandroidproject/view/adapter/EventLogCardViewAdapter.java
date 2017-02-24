package com.delta.smsandroidproject.view.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.bean.EventListData;
import com.delta.smsandroidproject.presenter.ChargerDetailPresenter;
import com.delta.smsandroidproject.presenter.ChargerDetailPresenter.ChargerDetail;
import com.delta.smsandroidproject.presenter.EvseDetailPresenter;
import com.delta.smsandroidproject.presenter.EvseDetailPresenter.EvseDetail;
import com.delta.smsandroidproject.presenter.ipresenter.IPresenter;
import com.delta.smsandroidproject.util.Logg;

public class EventLogCardViewAdapter extends RecyclerView.Adapter<EventLogCardViewAdapter.ItemViewHolder>{
	private static final String TAG = "EventLogCardViewAdapter";
	private Context mContext;
	private List<EventListData> datas;
	private Object mObject;
	private IPresenter evsePresenter,chargerPresenter;
	private Map<String, String> map;
	public EventLogCardViewAdapter(Context context) {
		this.mContext = context;
		map = new HashMap<String, String>();
	}
	
	public void setCallBack(Object mObject){
		if (mObject instanceof EvseDetail ) {
			evsePresenter = new EvseDetailPresenter(mContext,mObject);
		}
		if (mObject instanceof ChargerDetail) {
			chargerPresenter = new ChargerDetailPresenter(mContext,mObject);
		}
		
	}
	public void setDatas(List<EventListData> datas){
		Logg.d(TAG, "setDatas");
		Logg.d(TAG, "datas"+datas);
		this.datas = datas;
		this.notifyDataSetChanged();
	}
	
	public List<EventListData> getDatas(){
		return datas;
		
	}
	
	@Override
	public int getItemCount() {
//		return 10;
		return datas!=null?datas.size():0;
	}

	@Override
	public void onBindViewHolder(ItemViewHolder arg0, int arg1) {
		if (arg0!=null) {
			final EventListData data = datas.get(arg1);
			
			arg0.mClearance.setText(data.getClearance());
			arg0.mEvDetail.setText(data.getTitle());
			arg0.mOccur.setText(data.getOccurrence());
			arg0.mEvent.setText(data.getLevel());
			if (data!=null) {
				if (data.getLevel()!=null) {
					if (data.getLevel().equals(EventListData.CHARGE)) {
						arg0.mEvent.setBackgroundColor(mContext.getResources().getColor(R.color.event_log_charger));
					}else if (data.getLevel().equals(EventListData.CONFIGURATION)) {
						arg0.mEvent.setBackgroundColor(mContext.getResources().getColor(R.color.event_log_config));
						arg0.mEvent.setText(mContext.getResources().getString(R.string.event_log_config));
					}else if (data.getLevel().equals(EventListData.INFORMATION)) {
						arg0.mEvent.setBackgroundColor(mContext.getResources().getColor(R.color.event_log_information));
						arg0.mEvent.setText(mContext.getResources().getString(R.string.event_log_info));
					}else if (data.getLevel().equals(EventListData.WARNING)) {
						arg0.mEvent.setBackgroundColor(mContext.getResources().getColor(R.color.event_log_warn));
					}else if (data.getLevel().equals(EventListData.FAULT)) {
						arg0.mEvent.setBackgroundColor(mContext.getResources().getColor(R.color.event_log_fault));
					}else if (data.getLevel().equals(EventListData.EMERGENCY)) {
						arg0.mEvent.setBackgroundColor(mContext.getResources().getColor(R.color.event_log_emergency));
					}
				}
				
				if (data.getChargerId()!=null) {
					arg0.mChargerId.setText(data.getChargerId());
					arg0.mChargerIdLayout.setVisibility(View.VISIBLE);
				}else {
					arg0.mChargerIdLayout.setVisibility(View.GONE);
				}
				if (data.getEvseId()!=null&&!data.getEvseId().equals("0")) {
					arg0.mEvseId.setText(data.getEvseId());
					arg0.mEvseIdLayout.setVisibility(View.VISIBLE);
				}else {
					arg0.mEvseIdLayout.setVisibility(View.GONE);
				}
				arg0.view.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (data.getChargerId()!=null) {
							map.clear();
							map.put("key", data.getEventId());
							if (chargerPresenter!=null) {
								chargerPresenter.loadData(map);
							}
							if (data.getEvseId()!=null&&!data.getEvseId().equals("0")) {
								map.clear();
								map.put("key", data.getEventId());
								if (evsePresenter!=null) {
									evsePresenter.loadData(map);
								}
							}
//						}else{
//							map.clear();
//							map.put("key", data.getEvseId());
//							if (evsePresenter!=null) {
//								evsePresenter.loadData(map);
//							}
						}
					}
				});
			}
		}
	}

	@Override
	public ItemViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		Logg.i("arg1--", ""+arg1);
		return new ItemViewHolder(LayoutInflater.from(mContext)
				.inflate(R.layout.item_event_log_all_cardview, arg0,false));//解决android 6.0在cardview布局中设置margin无效
	}
	
	public class ItemViewHolder extends RecyclerView.ViewHolder{
		private View view;
		private TextView mEvent;
		private TextView mEvDetail;
		private TextView mOccur;
		private TextView mClearance;
		private TextView mChargerId;
		private TextView mEvseId;
		private LinearLayout mChargerIdLayout;
		private LinearLayout mEvseIdLayout;

		public ItemViewHolder(View arg0) {
			super(arg0);
			view = arg0;
			mEvent = (TextView) arg0.findViewById(R.id.event);
			mEvDetail = (TextView) arg0.findViewById(R.id.eventDetail);
			mOccur = (TextView) arg0.findViewById(R.id.occur);
			mClearance = (TextView) arg0.findViewById(R.id.clearance);
			mChargerId = (TextView) arg0.findViewById(R.id.chargerId);
			mEvseId = (TextView) arg0.findViewById(R.id.evseId);
			mChargerIdLayout = (LinearLayout) arg0.findViewById(R.id.chargerIdLayout);
			mEvseIdLayout = (LinearLayout) arg0.findViewById(R.id.evseIdLayout);
		}
		
	}

}
