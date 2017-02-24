package com.delta.smsandroidproject.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.delta.smsandroidproject.bean.ChargerListData.Evse;

public class WrappingLinearLayoutManager extends LinearLayoutManager {
	private int hg = 0;
	private Evse[] evse;
	private int[] is;

	public WrappingLinearLayoutManager(Context context) {
		super(context);
	}

	public WrappingLinearLayoutManager(Context context, int hg) {
		super(context);
		this.hg = hg;
	}

	public WrappingLinearLayoutManager(Context context, Evse[] evse) {
		super(context);
		if (evse != null) {

			if (evse.length > 0) {
				this.evse = evse;
				is = new int[evse.length];
				for (int i = 0; i < evse.length; i++) {
					if (evse[i].getConnector() != null) {
						is[i] = evse[i].getConnector().length;
					}

				}
			}
		}
	}

	private int[] mMeasuredDimension = new int[2];

	@Override
	public boolean canScrollVertically() {
		return false;
	}

	@Override
	public void onMeasure(RecyclerView.Recycler recycler,
			RecyclerView.State state, int widthSpec, int heightSpec) {
		final int widthMode = View.MeasureSpec.getMode(widthSpec);
		final int heightMode = View.MeasureSpec.getMode(heightSpec);

		final int widthSize = View.MeasureSpec.getSize(widthSpec);
		final int heightSize = View.MeasureSpec.getSize(heightSpec);

		int width = 0;
		int height = 0;
		for (int i = 0; i < getItemCount(); i++) {
			if (getOrientation() == HORIZONTAL) {
				measureScrapChild(recycler, i,
						View.MeasureSpec.makeMeasureSpec(0,
								View.MeasureSpec.UNSPECIFIED), heightSpec,
						mMeasuredDimension);

				width = width + mMeasuredDimension[0];
				if (i == 0) {
					height = mMeasuredDimension[1];
				}
			} else {
				measureScrapChild(recycler, i, widthSpec,
						View.MeasureSpec.makeMeasureSpec(0,
								View.MeasureSpec.UNSPECIFIED),
						mMeasuredDimension);

				height = height + mMeasuredDimension[1];
				if (is != null) {
					int evsehg = is[i];
					if (evsehg != 0) {
						if (evsehg % 3 > 0) {
							height = (int) (height + (evsehg / 3 + 1)
									* mMeasuredDimension[1] * 1.4);
						} else {
							height = height + (evsehg / 3)
									* mMeasuredDimension[1];
						}
					}
				}
				if (i == 0) {
					width = mMeasuredDimension[0];
				}
			}
		}

		switch (widthMode) {
		case View.MeasureSpec.EXACTLY:
			width = widthSize;
		case View.MeasureSpec.AT_MOST:
		case View.MeasureSpec.UNSPECIFIED:
		}

		switch (heightMode) {
		case View.MeasureSpec.EXACTLY:
			height = heightSize;
		case View.MeasureSpec.AT_MOST:
		case View.MeasureSpec.UNSPECIFIED:
		}

		setMeasuredDimension(width, height);
	}

	private void measureScrapChild(RecyclerView.Recycler recycler,
			int position, int widthSpec, int heightSpec, int[] measuredDimension) {

		View view = recycler.getViewForPosition(position);
		if (view.getVisibility() == View.GONE) {
			measuredDimension[0] = 0;
			measuredDimension[1] = 0;
			return;
		}
		// For adding Item Decor Insets to view
		super.measureChildWithMargins(view, 0, 0);
		RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view
				.getLayoutParams();
		int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec,
				getPaddingLeft() + getPaddingRight() + getDecoratedLeft(view)
						+ getDecoratedRight(view), p.width);
		int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
				getPaddingTop() + getPaddingBottom() + getDecoratedTop(view)
						+ getDecoratedBottom(view), p.height);
		view.measure(childWidthSpec, childHeightSpec);

		// Get decorated measurements
		measuredDimension[0] = getDecoratedMeasuredWidth(view) + p.leftMargin
				+ p.rightMargin;
		measuredDimension[1] = getDecoratedMeasuredHeight(view)
				+ p.bottomMargin + p.topMargin;
		recycler.recycleView(view);
	}
}