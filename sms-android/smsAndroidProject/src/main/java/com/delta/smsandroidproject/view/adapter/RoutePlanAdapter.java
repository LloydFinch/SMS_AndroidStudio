package com.delta.smsandroidproject.view.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.bean.BestRouteModel;
import com.delta.smsandroidproject.view.adapter.BasicViewHolder.onRecyclerViewItemClickListener;

public class RoutePlanAdapter extends RecyclerView.Adapter<BasicViewHolder> {

	private static Context mContext;
	private List<BestRouteModel> routeModels;

	// itemView的点击事件
	private onRecyclerViewItemClickListener onItemClickListener;

	public RoutePlanAdapter(List<BestRouteModel> routeModels) {
		this.routeModels = routeModels;
	}

	public void setOnItemClickListener(
			onRecyclerViewItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	@Override
	public int getItemCount() {
		return routeModels.size();
	}

	public void setRoutes(List<BestRouteModel> routeModels) {
		this.routeModels = routeModels;
	}

	@Override
	public BasicViewHolder onCreateViewHolder(ViewGroup group, int i) {
		mContext = group.getContext();
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.item_routeplan, group, false);
		return new BasicViewHolder(view)
				.setOnItemClickListener(onItemClickListener);
	}

	@Override
	public void onBindViewHolder(BasicViewHolder holder, int position) {
		BestRouteModel route = routeModels.get(position);
		showRouteInfo(holder, route);
	}

	private void showRouteInfo(BasicViewHolder holder, BestRouteModel route) {
		if (route != null) {

			// 如果有费用就显示,否则就隐藏这个标签
			String fare = route.getFareValue();
			if (fare != null && !fare.equals("0.0")) {
				holder.setText(R.id.route_tv_fare, fare);
			} else {
				holder.setItemVisibility(R.id.route_tv_fare_tag, View.GONE)
						.setItemVisibility(R.id.route_tv_fare, View.GONE);
			}

			// 显示距离(单位:km)
			holder.setText(R.id.route_tv_distance,
					parseNum(route.getDistance() / 1000.0f) + "km");

			String visit = route.getVisit();

			// 显示路径详情
			if (visit != null) {
				holder.setText(R.id.route_tv_visit, visit);
			} else {
				holder.setText(R.id.route_tv_visit, mContext.getResources()
						.getString(R.string.alert_no_route));
			}

			// 显示 copyrights
			holder.setText(R.id.route_tv_copyright,
					"      " + route.getCopyrights());

			// 如果有warns就显示,否则就隐藏这个标签
			String[] warns = route.getWarnings();
			if (warns != null && warns.length > 0) {
				StringBuilder sb = new StringBuilder();
				for (String warn : warns) {
					sb.append(warn).append(",");
				}
				holder.setText(R.id.route_tv_warn, sb.toString());
			} else {
				holder.setItemVisibility(R.id.route_tv_warn_tag, View.GONE)
						.setItemVisibility(R.id.route_tv_warn, View.GONE);
			}
		}
	}

	// 将距离转换为km并精确到小数点后一位,误差为100m
	public static String parseNum(double num) {
		DecimalFormat decimalFormat = new DecimalFormat("#.#");
		String result = decimalFormat.format(num);
		return result;
	}

}
