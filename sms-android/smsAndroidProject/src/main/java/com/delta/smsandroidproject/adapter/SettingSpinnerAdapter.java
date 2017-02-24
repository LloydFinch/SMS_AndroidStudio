package com.delta.smsandroidproject.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.delta.smsandroidproject.R;

public class SettingSpinnerAdapter extends BaseAdapter {

	private Context context;
	private List<String> list;

	public SettingSpinnerAdapter(Context context) {
		this.context = context;
	}

	public void setList(List<String> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return list.get(position);
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO 自动生成的方法存根
		super.notifyDataSetChanged();
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		LayoutInflater _LayoutInflater = LayoutInflater.from(context);
		convertView = _LayoutInflater.inflate(R.layout.item_setting_spinner,
				null);
		if (convertView != null) {
			TextView textview = (TextView) convertView
					.findViewById(R.id.location);
			String text = list.get(position);
			textview.setText(text);
			if (TextUtils.isEmpty(text)) {
				// convertView.setVisibility(View.GONE);
				convertView = _LayoutInflater.inflate(
						R.layout.spinner_is_empty, null);
				return convertView;
			}
		}
		return convertView;
	}

}
