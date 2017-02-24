package com.delta.smsandroidproject.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.bean.BestRouteModel;
import com.delta.smsandroidproject.bean.GoogleDirectionModel.Routes.Legs;
import com.delta.smsandroidproject.bean.GoogleDirectionModel.Routes.Legs.Distance;
import com.delta.smsandroidproject.bean.GoogleDirectionModel.Routes.Legs.Steps;

public class RoutedailAdapter extends BaseAdapter {

	private Context mContext;
	private List<Steps> steps = new ArrayList<>();

	public RoutedailAdapter(Context mContext, BestRouteModel bestModel) {
		super();
		this.mContext = mContext;
		List<Legs> legs = bestModel.getLegs();
		for (Legs leg : legs) {
			steps.addAll(leg.getSteps());
		}
	}

	@Override
	public int getCount() {
		return steps.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView == null ? LayoutInflater.from(mContext)
				.inflate(R.layout.item_route_detail, parent, false)
				: convertView;
		BasicViewHolder holder = (BasicViewHolder) view.getTag();
		if (holder == null) {
			holder = new BasicViewHolder(view);
			view.setTag(holder);
		}

		Steps step = steps.get(position);
		showRouteDetail(holder, step);
		return view;
	}

	private void showRouteDetail(BasicViewHolder holder, Steps step) {
		if (step != null) {
			Distance distances = step.getDistance();
			if (distances != null) {
				holder.setText(R.id.item_tv_distance, distances.getText());
			}
			Spanned instruection = step.getHtml_instructions();
			holder.setText(R.id.item_tv_visit, instruection);
		}
	}
}
