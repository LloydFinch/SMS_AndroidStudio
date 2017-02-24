package com.delta.smsandroidproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.bean.ChargerListData.Evse;

public class EvseAdapter extends
		RecyclerView.Adapter<EvseAdapter.EvseItemViewHolder> {
	private Context context;
	private Evse[] evses;
private MyGridLayoutManager[] myGridLayoutManagers;
	public EvseAdapter(Context context) {
		this.context = context;
	}

	public void setData(Evse[] evses) {
		this.evses = evses;
		this.notifyDataSetChanged();
		//myGridLayoutManagers = new MyGridLayoutManager[evses.length];
	}

	@Override
	public int getItemCount() {
		// TODO 自动生成的方法存根
		if (evses == null) {
			return 0;
		}
		return evses.length;
	}

	@Override
	public void onBindViewHolder(EvseItemViewHolder arg0, int arg1) {
		// TODO 自动生成的方法存根
		EvseItemViewHolder viewHolder = arg0;
		Evse evse = evses[arg1];
		String m_format = "%s(%s)";
		arg0.evse.setText(evse.getName());

		if (evse.getConnector() == null) {

		} else {
			//WrappingLinearLayoutManager w = new WrappingLinearLayoutManager(context,10);
			MyGridLayoutManager s = new MyGridLayoutManager(context,2);
			//w.setOrientation(WrappingLinearLayoutManager.HORIZONTAL);
			viewHolder.evse_list
					.setLayoutManager(s);
			
			ConnectorAdapter adapter = new ConnectorAdapter(context);
			viewHolder.evse_list.setAdapter(adapter);
			adapter.setData(evse.getConnector());
		}
		if (TextUtils.isEmpty(evse.getStatus())) {

		} else {
			arg0.status.setText(initStatus(Integer.parseInt(evse.getStatus())));
		}

	}

	@Override
	public EvseItemViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO 自动生成的方法存根
		return new EvseItemViewHolder(LayoutInflater.from(context).inflate(
				R.layout.evse_item, null));
	}

	public class EvseItemViewHolder extends ViewHolder {
		private TextView evse, status, ch, ccs;
		private RecyclerView evse_list;

		public EvseItemViewHolder(View arg0) {
			super(arg0);
			evse = (TextView) arg0.findViewById(R.id.evse_name);
			status = (TextView) arg0.findViewById(R.id.evse_status);
			
			evse_list = (RecyclerView) arg0.findViewById(R.id.evse_list);
		}

	}

	public String initStatus(int i) {
		switch (i) {
		case -1:
			String offline  = SMSApplication.getInstance().getResources().getString(R.string.offline);
			return offline;
		case 0:
			String Available  = SMSApplication.getInstance().getResources().getString(R.string.available);
			return Available;
		case 1:
			String Occupied  = SMSApplication.getInstance().getResources().getString(R.string.occupied);
			return Occupied;
		case 2:
			String Reserved  = SMSApplication.getInstance().getResources().getString(R.string.reserved);
			return Reserved;
		case 3:
			String Unavailable  = SMSApplication.getInstance().getResources().getString(R.string.unavailable);
			return Unavailable;
		case 4:
			String Faulted  = SMSApplication.getInstance().getResources().getString(R.string.faulted);
			return Faulted;
		default:
			return " ";
		}
	}

}
