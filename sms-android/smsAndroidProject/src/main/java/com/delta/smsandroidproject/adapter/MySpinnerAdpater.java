package com.delta.smsandroidproject.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.delta.smsandroidproject.R;

public class MySpinnerAdpater extends BaseAdapter{
	private Context context;
	private List<String> list;
	private String hint;
	private String title;
	public MySpinnerAdpater(Context context,List<String> list){
		this.context = context;
		this.list = list;
	}
	public MySpinnerAdpater(Context context,List<String> list,String title){
		this.context = context;
		this.list = list;
		this.title = title;
	}
	public MySpinnerAdpater(Context context){
		this.context = context;
	}
	public MySpinnerAdpater(Context context,String title){
		this.context = context;
		this.title = title;
	}
	public void setList(List<String> list){
		this.list = list;
	//	this.notifyDataSetInvalidated();
		notifyDataSetChanged();
	}
	public void setHint(String hint){
		this.hint = hint;
	}
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		if (list==null) {
			return 0;
		}
		return list.size();
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

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		LayoutInflater _LayoutInflater = LayoutInflater.from(context);  
        convertView = _LayoutInflater.inflate(R.layout.item_location, parent,false);  
        if (convertView!=null) {
			TextView textview = (TextView) convertView.findViewById(R.id.location);
			textview.setText(list.get(position));
			if (TextUtils.isEmpty(list.get(position))&&position==0) {
				//convertView.setVisibility(View.GONE);
//				convertView = _LayoutInflater.inflate(R.layout.spinner_null, parent,false);  
//				TextView t = (TextView) convertView.findViewById(R.id.spinner_chose);
//				t.setText(title);
				textview.setText(title);
				return convertView;
			}
		}
		return convertView;
	}

}
