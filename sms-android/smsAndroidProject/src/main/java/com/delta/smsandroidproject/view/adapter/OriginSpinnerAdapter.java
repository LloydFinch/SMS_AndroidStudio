package com.delta.smsandroidproject.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.bean.ChargerLocationData;
import com.delta.smsandroidproject.util.SetTextUtils;

public class OriginSpinnerAdapter extends BaseAdapter {

	private Context mContext;
	private List<ChargerLocationData> datas = new ArrayList<ChargerLocationData>();

	public OriginSpinnerAdapter(Context mContext,
			List<ChargerLocationData> datas) {
		super();
		this.mContext = mContext;
		this.datas = datas;
	}

	public void changeData(List<ChargerLocationData> datas) {
		this.datas = datas;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView == null ? LayoutInflater
				.from(mContext)
				.inflate(
						com.delta.smsandroidproject.R.layout.item_spinner_location,
						parent, false)
				: convertView;
		ViewHolder holder = (ViewHolder) view.getTag();
		if (holder == null) {
			holder = new ViewHolder();
			holder.textView = (TextView) view.findViewById(R.id.location);
			view.setTag(holder);
		}

		ChargerLocationData data = datas.get(position);
		if (data != null) {
			SetTextUtils.setText(holder.textView, data.getName());
		}

		return view;
	}

	public class ViewHolder {
		TextView textView;
	}
}
