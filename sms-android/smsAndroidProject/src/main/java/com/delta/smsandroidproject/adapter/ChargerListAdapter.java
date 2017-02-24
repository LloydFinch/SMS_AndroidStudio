package com.delta.smsandroidproject.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.bean.ChargerInfoData;
import com.delta.smsandroidproject.bean.ChargerListData;
import com.delta.smsandroidproject.util.ToolUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ChargerListAdapter extends
		RecyclerView.Adapter<ChargerListAdapter.ItemViewHolder> {
	private Context context;
	private ArrayList<ChargerListData> chargerListDatas;
	private ArrayList<ChargerInfoData> infoDatas;
	private ChargerUploadImg upLoadImg;
	//private WrappingLinearLayoutManager[] layoutManagers;
	public ChargerListAdapter(Context context) {
		this.context = context;
	}
	public ChargerListAdapter(Context context,ChargerUploadImg upLoadImg) {
		this.context = context;
		this.upLoadImg = upLoadImg;
	}
	public void setData(ArrayList<ChargerInfoData> infoData,
			ArrayList<ChargerListData> chargerListDatas) {
		this.chargerListDatas = chargerListDatas;
		this.infoDatas = infoData;
		this.notifyDataSetChanged();
//		layoutManagers = new  WrappingLinearLayoutManager[chargerListDatas.size()];
//		for (int i = 0; i < chargerListDatas.size(); i++) {
//			layoutManagers[i] = new WrappingLinearLayoutManager(context, chargerListDatas.get(i).getEvse());
//		}
	}

	@Override
	public int getItemCount() {
		// TODO 自动生成的方法存根
		if (infoDatas == null) {
			return 0;
		}
		return infoDatas.size();
	}

	@Override
	public void onBindViewHolder(ItemViewHolder arg0, int arg1) {
		ItemViewHolder viewHolder = arg0;
		final ChargerInfoData infoData = infoDatas.get(arg1);
		final ChargerListData listData = chargerListDatas.get(arg1);
		viewHolder.charger_id.setText(infoData.getId());
		viewHolder.charger_description.setText(listData.getNote());
		// viewHolder.charger_status.setText(listData.getStatus());
		viewHolder.charger_model.setText(listData.getType());
		viewHolder.charger_name.setText(infoData.getName());
		EvseAdapter adapter = new EvseAdapter(context);
		if (chargerListDatas!=null) {
			WrappingLinearLayoutManager layoutManager = new WrappingLinearLayoutManager(context, chargerListDatas.get(arg1).getEvse());
			viewHolder.evse_recyclerview.setLayoutManager(layoutManager);
		}
//		if (layoutManagers!=null) {
//			WrappingLinearLayoutManager s= layoutManagers[arg1];
//			if (viewHolder.evse_recyclerview.getLayoutManager()==null) {
//				s.getRecycleChildrenOnDetach();
//					viewHolder.evse_recyclerview
//					.setLayoutManager(s);
//				
//				
//			}
//			
//			viewHolder.evse_recyclerview.setAdapter(adapter);
//		}
		
//		viewHolder.evse_recyclerview
//		.setLayoutManager(new WrappingLinearLayoutManager(context,listData.getEvse()));
		Log.e("显示status", listData.getStatus());
		if (TextUtils.isEmpty(listData.getStatus())) {

		} else {
			viewHolder.charger_status.setText(adapter.initStatus(Integer
					.parseInt(listData.getStatus())));
		}

		viewHolder.evse_recyclerview.setAdapter(adapter);
//		ChargerListData.Evse e = new ChargerListData().new Evse();
//		e.setId("aaa");
//		e.setName("dddd");
//		e.setStatus("0");
//		Evse[] es = {e};
		adapter.setData(listData.getEvse());
		//adapter.setData(es);
		listData.getEvse();
		int i = (int) context.getResources().getDimension(R.dimen.image_height);
		Picasso.with(context).load(listData.getImage()) .resize(i, i).centerCrop()
	
		.placeholder(R.drawable.zxc).error(R.drawable.zxc)
				.into(viewHolder.download_img, new Callback() {

					@Override
					public void onSuccess() {
						// TODO 自动生成的方法存根
					}

					@Override
					public void onError() {
						// TODO 自动生成的方法存根
					}
				});
		viewHolder.camera_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				upLoadImg.ChargerUpload(listData,infoData.getId());
			}
		});

	}

	@Override
	public ItemViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO 自动生成的方法存根
		
		return new ItemViewHolder(LayoutInflater.from(context).inflate(
				R.layout.charger_info_itemm, null),arg1);
	}
public interface ChargerUploadImg{
	void ChargerUpload(ChargerListData listData,String Id);
}
	class ItemViewHolder extends RecyclerView.ViewHolder {
		private ImageView download_img, camera_img;
		private TextView charger_id, charger_status, charger_model,
				charger_description, charger_latitude, charger_longitude,charger_name;
		private RecyclerView evse_recyclerview;

		public ItemViewHolder(View arg0) {
			super(arg0);
			download_img = (ImageView) arg0.findViewById(R.id.download_img);
			camera_img = (ImageView) arg0.findViewById(R.id.camera_img);
			charger_id = (TextView) arg0.findViewById(R.id.charger_id);
			charger_model = (TextView) arg0.findViewById(R.id.charger_model);
			charger_status = (TextView) arg0.findViewById(R.id.charger_status);
			charger_name = (TextView) arg0.findViewById(R.id.charger_name);
			charger_description = (TextView) arg0
					.findViewById(R.id.charger_description);
			evse_recyclerview = (RecyclerView) arg0
					.findViewById(R.id.evse_recyclerview);
			if (ToolUtil.notService()) {
				camera_img.setVisibility(View.GONE);
			}else{
				camera_img.setVisibility(View.VISIBLE);
			}
		}
		public ItemViewHolder(View arg0,int arg1) {
			super(arg0);
			download_img = (ImageView) arg0.findViewById(R.id.download_img);
			camera_img = (ImageView) arg0.findViewById(R.id.camera_img);
			charger_id = (TextView) arg0.findViewById(R.id.charger_id);
			charger_model = (TextView) arg0.findViewById(R.id.charger_model);
			charger_status = (TextView) arg0.findViewById(R.id.charger_status);
			charger_name = (TextView) arg0.findViewById(R.id.charger_name);
			charger_description = (TextView) arg0
					.findViewById(R.id.charger_description);
			evse_recyclerview = (RecyclerView) arg0
					.findViewById(R.id.evse_recyclerview);
//			if (chargerListDatas!=null) {
//				Log.e("显示数组222222", chargerListDatas.get(arg1).getEvse().toString());
//				WrappingLinearLayoutManager layoutManager = new WrappingLinearLayoutManager(context, chargerListDatas.get(arg1).getEvse());
//				evse_recyclerview.setLayoutManager(layoutManager);
//			}
			if (ToolUtil.notService()) {
				camera_img.setVisibility(View.GONE);
			}else{
				camera_img.setVisibility(View.VISIBLE);
			}

	}
	}
}
