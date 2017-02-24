package com.delta.smsandroidproject.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;

import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.bean.FirmwareModel.FirmWare;
import com.delta.smsandroidproject.util.Logg;
import com.delta.smsandroidproject.util.SetTextUtils;

public class ChooseVersionAdapter extends
		RecyclerView.Adapter<ChooseVersionAdapter.ItemViewHolder> {

	private Context mContext;
	private List<FirmWare> datas = new ArrayList<>();
	private int selected;
	private List<RadioButton> radButtons = new ArrayList<>();

	public ChooseVersionAdapter(Context context, List<FirmWare> datas) {
		this.mContext = context;
		this.datas = datas;
	}

	public void setDatas(List<FirmWare> datas) {
		this.datas = datas;
	}

	@Override
	public int getItemCount() {
		return datas.size();
	}

	public int getSelected() {
		return selected;
	}

	public void addToState(RadioButton button) {
		if (radButtons.size() > 10) {
			radButtons.remove(0);
		}
		radButtons.add(button);
	}

	public void updateState() {
		for (RadioButton radioButton : radButtons) {
			radioButton.setChecked(false);
		}
	}

	@Override
	public void onBindViewHolder(ItemViewHolder holder, int position) {
		addToState(holder.radioChoose);
		FirmWare firmWare = datas.get(position);
		Logg.i("onBindViewHolder", "firmWare:" + firmWare);
		if (firmWare != null) {
			Logg.i("FirmWare", "firmWare:" + firmWare);
			SetTextUtils.setText(holder.tvVersionCode,
					"Version " + firmWare.getVersion());
			SetTextUtils.setText(holder.tvVersionDate, "Release date "
					+ firmWare.getUploadDate().substring(0, 10));
			if (position == selected) {
				holder.radioChoose.setChecked(true);
			} else {
				holder.radioChoose.setChecked(false);
			}
		}
	}

	@Override
	public ItemViewHolder onCreateViewHolder(ViewGroup group, int position) {
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.item_dialog_chooseversion, group, false);
		return new ItemViewHolder(view);
	}

	public class ItemViewHolder extends RecyclerView.ViewHolder implements
			OnCheckedChangeListener {

		private TextView tvVersionCode;
		private TextView tvVersionDate;
		private RadioButton radioChoose;

		public ItemViewHolder(View view) {
			super(view);
			tvVersionCode = (TextView) view.findViewById(R.id.tv_version_code);
			tvVersionDate = (TextView) view.findViewById(R.id.tv_version_date);
			radioChoose = (RadioButton) view
					.findViewById(R.id.radio_select_version);
			radioChoose.setOnCheckedChangeListener(this);
			Logg.i("ItemViewHolder", "ItemViewHolder");
		}

		@SuppressWarnings("deprecation")
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			updateState();
			buttonView.setChecked(isChecked);
			if (isChecked) {
				selected = getPosition();
			}
		}
	}
}
