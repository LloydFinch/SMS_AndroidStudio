package com.delta.smsandroidproject.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.bean.ChargerLocationData;
import com.delta.smsandroidproject.view.ChargingLocMapView;

public class ChooseLocationAdapter extends RecyclerView.Adapter<ChooseLocationAdapter.ItemViewHolder>{
	
	private Context mContext;
	private List<ChargerLocationData> datas = new ArrayList<ChargerLocationData>();
	private ChargingLocMapView chargingLocMapView;
	private RecycleviewOnItemClick itemClick;
	public static interface RecycleviewOnItemClick{
		public void onItemClick(View v,ChargerLocationData data);
	}
	
	public void setOnItemClick(RecycleviewOnItemClick itemClick){
		this.itemClick =itemClick;
	}
	public ChooseLocationAdapter(Context context,List<ChargerLocationData> datas, ChargingLocMapView chargingLocMapView) {
		this.mContext = context;
		this.datas = datas;
		this.chargingLocMapView = chargingLocMapView;
	}
	
	@Override
	public int getItemCount() {
		if (datas == null) {
			return 0;
		}else {
			return datas.size();
		}
			
	}

	@Override
	public void onBindViewHolder(ItemViewHolder arg0, int position) {
		ItemViewHolder holder = arg0;
		final ChargerLocationData data = datas.get(position);
		holder.mLocation.setText(data.getName());
		holder.mLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (itemClick!=null) {
					itemClick.onItemClick(v, data);
				}
			}
		});
	}

	@Override
	public ItemViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		return new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_choose_location, null));
	}
	
	public class ItemViewHolder extends RecyclerView.ViewHolder{

		private TextView mLocation;

		public ItemViewHolder(View arg0) {
			super(arg0);
			mLocation = (TextView) arg0.findViewById(R.id.location);
//			arg0.setOnClickListener(this);
		}

//		@Override
//		public void onClick(View v) {
//			chargingLocMapView.setCurrentLocation(mLocation.getText().toString());
//			chargingLocMapView.dismissChooseLocationDialog();
//		}
		
	}
}
