package com.delta.smsandroidproject.view.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.bean.ChargerLocationData;
import com.delta.smsandroidproject.util.Logg;

public class NearByLocationAdapter extends RecyclerView.Adapter<NearByLocationAdapter.ItemViewHolder> implements OnClickListener{
	
	private Context mContext;
	private List<ChargerLocationData> datas;
	private RecycleviewOnItemClick click;

	public NearByLocationAdapter(Context context, List<ChargerLocationData> datas) {
		this.mContext = context;
		this.datas = datas;
	}
	
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		if (datas.size() == 0) {
			return 0;
		}else {
			return datas.size();
		}
	}

	@Override
	public void onBindViewHolder(ItemViewHolder arg0, int arg1) {
		ItemViewHolder holder = arg0;
		ChargerLocationData locData = datas.get(arg1);
		holder.mDistance.setText(locData.getDistance()+"KM");
		holder.mLocaton.setText(locData.getName());
		holder.itemView.setTag(datas.get(arg1));
		Logg.i("arg1", ""+arg1);

	}

	@Override
	public ItemViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.item_nearby_location,arg0, false);
		ItemViewHolder holder = new ItemViewHolder(view);
		view.setOnClickListener(this);
		return holder;
	}
	
	public static interface RecycleviewOnItemClick{
		public void onItemClick(View v,ChargerLocationData data);
	}
	
	public void setOnItemClick(RecycleviewOnItemClick click){
		this.click = click;
	}
	
	public class ItemViewHolder extends RecyclerView.ViewHolder{

		private TextView mLocaton;
		private TextView mDistance;
		private LinearLayout mLocLayout;

		public ItemViewHolder(View arg0) {
			super(arg0);
			mLocLayout = (LinearLayout) arg0.findViewById(R.id.locLayout);
			mLocaton = (TextView) arg0.findViewById(R.id.location);
			mDistance = (TextView) arg0.findViewById(R.id.distance);
		}

	}

	@Override
	public void onClick(View v) {
		if (click !=null) {
			click.onItemClick(v, (ChargerLocationData)v.getTag());
		}
	}
}
