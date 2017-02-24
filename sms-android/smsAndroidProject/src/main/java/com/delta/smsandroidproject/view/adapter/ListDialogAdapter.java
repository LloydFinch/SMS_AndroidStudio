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
import com.delta.smsandroidproject.dialog.RoundListDialog;
import com.delta.smsandroidproject.util.SetTextUtils;
import com.delta.smsandroidproject.view.fragment.ServiceRoutePlannerFragment;

public class ListDialogAdapter extends
		RecyclerView.Adapter<ListDialogAdapter.ItemViewHolder> {

	private Context mContext;
	private List<ChargerLocationData> datas;
	private RoundListDialog dialog;

	public ListDialogAdapter(Context context, List<ChargerLocationData> datas,
			RoundListDialog dialog) {
		this.mContext = context;
		this.datas = new ArrayList<>(datas);
		this.dialog = dialog;
	}

	@Override
	public int getItemCount() {
		return datas.size();
	}

	@Override
	public void onBindViewHolder(ItemViewHolder holder, int position) {
		ChargerLocationData data = datas.get(position);
		SetTextUtils.setText(holder.mLocation, data.getName());
	}

	@Override
	public ItemViewHolder onCreateViewHolder(ViewGroup group, int position) {
		return new ItemViewHolder(LayoutInflater.from(mContext).inflate(
				R.layout.item_location, group, false));
	}

	public class ItemViewHolder extends RecyclerView.ViewHolder {

		private TextView mLocation;

		@SuppressWarnings("deprecation")
		public ItemViewHolder(View view) {
			super(view);
			mLocation = (TextView) view.findViewById(R.id.location);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
					ChargerLocationData data = datas.get(getPosition());
					ServiceRoutePlannerFragment.locationList.clear();
					ServiceRoutePlannerFragment.locationList.addAll(datas);
					ServiceRoutePlannerFragment.locationList.remove(0);
					ServiceRoutePlannerFragment.locationList.remove(data);
					ServiceRoutePlannerFragment.locationList.add(0, data);

					// ServiceRoutePlannerFragment.wayPointsAdapter.clearChoiceLocations();
					ServiceRoutePlannerFragment.wayPointsAdapter
							.notifyDataSetChanged();
				}
			});
		}
	}
}
