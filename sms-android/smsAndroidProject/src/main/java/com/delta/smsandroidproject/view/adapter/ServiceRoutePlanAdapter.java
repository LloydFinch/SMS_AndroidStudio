package com.delta.smsandroidproject.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.bean.ChargerLocationData;
import com.delta.smsandroidproject.util.Logg;
import com.delta.smsandroidproject.util.SetTextUtils;
import com.delta.smsandroidproject.view.adapter.ServiceRoutePlanAdapter.MyViewHolder;
import com.delta.smsandroidproject.view.fragment.ServiceRoutePlannerFragment;

public class ServiceRoutePlanAdapter extends RecyclerView.Adapter<MyViewHolder> {

	private Context mContext;

	// 选中的途径点
	private ArrayList<ChargerLocationData> choiceLocations = new ArrayList<>();
	private List<ChargerLocationData> datas;

	@SuppressLint("UseSparseArrays")
	public ServiceRoutePlanAdapter(Context context,
			List<ChargerLocationData> datas) {
		this.datas = datas;
		this.mContext = context;
	}

	public void changeData(List<ChargerLocationData> datas) {
		this.datas = datas;
	}

	public ArrayList<ChargerLocationData> getChoiceLocations() {
		return choiceLocations;
	}

	@Override
	public int getItemCount() {
		return datas.size();
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup group, int i) {
		mContext = group.getContext();
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.item_routeplan_service, group, false);

		return new MyViewHolder(view);
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		Logg.i("onBindViewHolder", "ServiceRoutePlanAdapter");
		SetTextUtils.setText(holder.tvDes, datas.get(position).getName());
		ChargerLocationData data = datas.get(position);
		if (data != null && search(data)) {
			holder.checkBox.setChecked(true);
		} else {
			holder.checkBox.setChecked(false);
		}
	}

	// 判断选择的点是否已经在途经点中
	public boolean search(ChargerLocationData data) {
		for (ChargerLocationData d : choiceLocations) {
			if (d.getId().equals(data.getId())) {
				return true;
			}
		}
		return false;
	}

	// 将选择的点添加到途经点列表
	public void addWaypoint(ChargerLocationData data) {
		if (data != null) {
			Log.i("choice-add:", "data:" + data.getName() + "," + data.getId());
			if (choiceLocations.size() <= 0) {
				choiceLocations.add(data);
			} else {
				ChargerLocationData addDatas = data;
				for (ChargerLocationData addData : choiceLocations) {
					String addId = addData.getId();
					String id = data.getId();
					if (addId.equals(id)) {
						addDatas = null;
						break;
					}
				}
				if (addDatas != null) {
					choiceLocations.add(addDatas);
				}
			}
		}

		for (ChargerLocationData s : choiceLocations) {
			Log.i("choice:", s.getName());
		}
		Log.i("choice1:", "size:" + choiceLocations.size());
	}

	// 从途经点列表中移除选择的点
	public void removeWaypoint(ChargerLocationData data) {
		if (data != null) {
			Log.i("choice-remove:",
					"data:" + data.getName() + "," + data.getId());
			int index = -1;
			for (ChargerLocationData removeData : choiceLocations) {
				if (removeData.getId().equals(data.getId())) {
					index = choiceLocations.indexOf(removeData);
				}
			}
			if (index >= 0) {
				choiceLocations.remove(index);
			}
		}
		for (ChargerLocationData s : choiceLocations) {
			Log.i("choice:", s.getName());
		}
		Log.i("choice1:", "size:" + choiceLocations.size());
	}

	class MyViewHolder extends RecyclerView.ViewHolder implements
			OnCheckedChangeListener {

		private TextView tvDes;
		private CheckBox checkBox;

		public MyViewHolder(View view) {
			super(view);
			tvDes = (TextView) view.findViewById(R.id.tv_route_service_des);
			checkBox = (CheckBox) view
					.findViewById(R.id.cb_route_service_check);
			checkBox.setOnCheckedChangeListener(this);
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			int position = getLayoutPosition();
			ChargerLocationData data = datas.get(position);
			if (data != null && isChecked) {

				// 途经点不能超过21个,带起点和终点不能超过23个
				if (choiceLocations.size() > 21) {
					Toast.makeText(
							mContext,
							mContext.getResources().getString(
									R.string.alert_too_more_points),
							Toast.LENGTH_SHORT).show();
				} else {
					addWaypoint(data);
				}
			} else {
				removeWaypoint(data);
			}
			// 有途经点被选中就可以点击按钮跳转到路径规划
			if (choiceLocations.size() < 1) {
				ServiceRoutePlannerFragment.buttonPlan.setVisibility(View.GONE);
			} else {
				ServiceRoutePlannerFragment.buttonPlan
						.setVisibility(View.VISIBLE);
			}
		}
	}
}
