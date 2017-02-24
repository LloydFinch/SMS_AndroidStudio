/**
 * 
 */
package com.delta.smsandroidproject.view.adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.delta.smsandroidproject.util.SetTextUtils;

/**
 * @author Wenqi.Wang
 * 
 */
public class BasicViewHolder extends ViewHolder implements OnClickListener {

	// itemView
	private View convertView;

	// itemView里面包括的子item
	private SparseArray<View> itemViewChildren;

	// itemView的点击事件
	private onRecyclerViewItemClickListener onItemClickListener;

	public BasicViewHolder(View itemView) {
		super(itemView);

		convertView = itemView;
		convertView.setOnClickListener(this);

		itemViewChildren = new SparseArray<>();
	}

	public BasicViewHolder setOnItemClickListener(
			onRecyclerViewItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
		return this;
	}

	@Override
	public void onClick(View v) {
		if (onItemClickListener != null) {
			onItemClickListener.onItemClick(v, getLayoutPosition());
		}
	}

	public <T extends View> View findItemViewChildById(int id) {
		View itemViewChild = itemViewChildren.get(id);
		if (itemViewChild == null) {
			itemViewChild = convertView.findViewById(id);
			itemViewChildren.put(id, itemViewChild);
		}
		return itemViewChild;
	}

	public BasicViewHolder setItemVisibility(int id, int visibility) {
		View itemView = findItemViewChildById(id);
		itemView.setVisibility(visibility);
		return this;
	}

	public BasicViewHolder setText(int id, CharSequence content) {
		TextView textView = (TextView) findItemViewChildById(id);
		SetTextUtils.setText(textView, content);
		return this;
	}

	public interface onRecyclerViewItemClickListener {
		void onItemClick(View itemView, int position);
	}
}
