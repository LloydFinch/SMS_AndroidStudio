package com.delta.smsandroidproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.bean.ChargerListData.Evse.Connectors;

public class ConnectorAdapter extends
		RecyclerView.Adapter<ConnectorAdapter.ConntorViewHolder> {
	private Context context;
	private Connectors[] Connector;
	public ConnectorAdapter(Context context) {
		this.context = context;
	}

	public void setData(Connectors[] Connector){
		this.Connector = Connector;
		this.notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		// TODO 自动生成的方法存根
		if (Connector!=null) {
			return Connector.length;
		}
			return 0;
		
	
	}

	@Override
	public void onBindViewHolder(ConntorViewHolder arg0, int arg1) {
		// TODO 自动生成的方法存根
		ConntorViewHolder viewHolder = arg0;
		String name_format = "(%s)";
		Connectors c = Connector[arg1];
		//Connectors c = new ChargerListData().new Evse().new Connectors("aa", "AA", "cAA", "0");
		StringBuffer sb = new StringBuffer();
		if (!TextUtils.isEmpty(c.getName())) {
			sb.append(c.getName());
		}if (Connector!=null) {
			sb.append(String.format(name_format, Connector.length));
		}
		if (!TextUtils.isEmpty(sb.toString())) {
			viewHolder.conntor_name.setText(sb.toString());
		}
		
		if (initImg(Integer.parseInt(c.getStatus()))) {
			viewHolder.conntor_img.setImageResource(R.drawable.evse_q);
		}else{
			viewHolder.conntor_img.setImageResource(R.drawable.evse_w);
		}

	}

	@Override
	public ConntorViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO 自动生成的方法存根
		return new ConntorViewHolder(LayoutInflater.from(context).inflate(
				R.layout.evse_item_item, null));
	}

	public class ConntorViewHolder extends ViewHolder {
		private TextView conntor_name;
		private ImageView conntor_img;
		public ConntorViewHolder(View arg0) {
			super(arg0);
			conntor_name = (TextView) arg0.findViewById(R.id.conntor_name);
			conntor_img = (ImageView) arg0.findViewById(R.id.conntor_img);
		}

	}
	public boolean initImg(int i){
		switch (i) {
		case 0:
			//return "Available";
			return true;
		case 1:
			//return "Occupied";
			return false;
		case 2:
			//return "Reserved";
			return false;
		case 3:
			//return "Unavailable";
			return false;
		case 4:
			//return "Faulted";
			return false;
		default:
			//return " ";
			return false;
		}
	}

}
