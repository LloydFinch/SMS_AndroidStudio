package com.delta.smsandroidproject.view.fragment;

import java.util.List;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.bean.EventListData;
import com.delta.smsandroidproject.bean.EvseDetailData;
import com.delta.smsandroidproject.manager.AnimRFLinearLayoutManager;
import com.delta.smsandroidproject.presenter.ChargerDetailPresenter.ChargerDetail;
import com.delta.smsandroidproject.util.AnimRFRecyclerView;
import com.delta.smsandroidproject.util.EventLogUtil;
import com.delta.smsandroidproject.util.Logg;
import com.delta.smsandroidproject.util.ToastCustom;
import com.delta.smsandroidproject.view.adapter.EventLogCardViewAdapter;
import com.delta.smsandroidproject.view.iview.ILoadView;
import com.delta.smsandroidproject.view.otherview.DetailDialog;

/**
 * Event Log Charger
 * @author Jianzao.Zhang
 *
 */
public class EventLogChargerFragment extends BaseFragment implements OnClickListener,ILoadView,ChargerDetail {

	public static final String TAG = "EventLogChargerFragment";
	private List<EventListData> datas;
	private FragmentActivity mContext;
	private String locationId;
	private int PAGE_NO =1;//第几页
	private int PER_PAGE = 25;//每页25条数据
	private AnimRFRecyclerView mLoadMoreRecyclerView;
	private EventLogCardViewAdapter mAdapter;
	private EventLogUtil eventLogUtil;
	private Toolbar mToolbar;
	private TextView mTxNoEvent;
	private String chargerId;
	private ProgressDialog mDialog;
	private DetailDialog mDetailDialog;
	private String no_event,no_event_log;
	public EventLogChargerFragment() {
	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		Logg.i(TAG, "onHiddenChanged");
		if (hidden) {
			EventLogChargerFragment.this.onResume();
		}else {
			EventLogChargerFragment.this.onPause();
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflate = inflater.inflate(R.layout.fragment_event_log_charger, container,
						false);
		mContext = getActivity();
		no_event = mContext.getResources().getString(R.string.no_event);
		no_event_log = mContext.getResources().getString(R.string.no_event_log);
		Logg.i(TAG, "onCreateView");
		initData();
		initView(inflate);
		
		return inflate;
	}
	
	public void loadData(String chargerId,String locationId,String ODERBY,List<String> types,String startTime,String endTime,int PAGE_NO,int PER_PAGE){
		this.PAGE_NO = PAGE_NO;
		if (eventLogUtil!=null) {
			eventLogUtil.removeEventLogData();
		}else {
			eventLogUtil = new EventLogUtil(mContext,mAdapter,TAG,mLoadMoreRecyclerView,this);
		}
		eventLogUtil.loadData(chargerId,locationId, ODERBY,types,startTime,endTime,PAGE_NO,PER_PAGE);
	}
	private void initData() {
		Bundle bundle = getArguments();
		if (bundle!=null) {
			locationId = bundle.getString("LocationId");
			chargerId = bundle.getString("ChargerID");
		}
		
	}

	private void initView(View inflate) {
		mTxNoEvent = (TextView) inflate.findViewById(R.id.txNoEvent);
		mLoadMoreRecyclerView = (AnimRFRecyclerView) inflate.findViewById(R.id.loadMoreRecyclerView1);
		mLoadMoreRecyclerView.setHasFixedSize(true);
		mAdapter = new EventLogCardViewAdapter(getActivity());
		mAdapter.setCallBack(this);
		// 使用重写后的线性布局管理器
		mLoadMoreRecyclerView.setLayoutManager(new AnimRFLinearLayoutManager(getActivity()));
//        // 添加头部和脚部，如果不添加就使用默认的头部和脚部
//        mLoadMoreRecyclerView.addHeaderView(headerView);
//        // 设置头部的最大拉伸倍率，默认1.5f，必须写在setHeaderImage()之前
//        mLoadMoreRecyclerView.setScaleRatio(1.7f);
//        // 设置下拉时拉伸的图片，不设置就使用默认的
//        mLoadMoreRecyclerView.setHeaderImage((ImageView) headerView.findViewById(R.id.iv_hander));
//        mLoadMoreRecyclerView.addFootView(footerView);
		// 设置刷新动画的颜色
		mLoadMoreRecyclerView.setColor(Color.RED, Color.BLUE);
		// 设置头部恢复动画的执行时间，默认500毫秒
		mLoadMoreRecyclerView.setHeaderImageDurationMillis(300);
		// 设置拉伸到最高时头部的透明度，默认0.5f
		mLoadMoreRecyclerView.setHeaderImageMinAlpha(0.6f);
		// 设置适配器
		mLoadMoreRecyclerView.setAdapter(mAdapter);
		// 设置刷新和加载更多数据的监听，分别在onRefresh()和onLoadMore()方法中执行刷新和加载更多操作
		mLoadMoreRecyclerView.setLoadDataListener(new AnimRFRecyclerView.LoadDataListener() {
			@Override
			public void onLoadMore() {
				if (!eventLogUtil.isLoadDataFinish()) {
					PAGE_NO++;
					eventLogUtil.loadData(chargerId,locationId, EventLogPagerFragment.ODERBY,EventLogPagerFragment.typeList,
							EventLogPagerFragment.START_TIME,EventLogPagerFragment.END_TIEM,PAGE_NO,PER_PAGE);
				}else {
					ToastCustom.showToast(mContext, mContext.getResources().getString(R.string.load_event_log_data_finish), ToastCustom.LENGTH_SHORT);
					mLoadMoreRecyclerView.getAdapter().notifyDataSetChanged();
					// 加载更多完成后调用，必须在UI线程中
					mLoadMoreRecyclerView.loadMoreComplate();
				}
			}
			@Override
			public void onRefresh() {
				
			}
		});
		mAdapter.setDatas(datas);
		mLoadMoreRecyclerView.setRefreshEnable(false);
//		eventLogUtil = new EventLogUtil(mContext,mAdapter,TAG,mLoadMoreRecyclerView);
		loadData(chargerId,locationId,EventLogPagerFragment.ODERBY,EventLogPagerFragment.typeList,
				EventLogPagerFragment.START_TIME,EventLogPagerFragment.END_TIEM,PAGE_NO,PER_PAGE);
		mDetailDialog = new DetailDialog(getActivity());
	}
	public void noEventPromot(List<EventListData> datas,String promot) {
		Logg.i(TAG, "datas-"+datas+"promot-"+promot );
		if (datas==null) {
			mTxNoEvent.setVisibility(View.VISIBLE);
			mTxNoEvent.setText(R.string.no_event_log);
		}
		if (datas!=null&& datas.size()==0) {
			if (promot!=null&&promot.equals("title")) {
				mTxNoEvent.setText( no_event+" "+promot);
			}else {
				mTxNoEvent.setText(R.string.no_event_log);
			}
		}
		if (datas!=null&& datas.size()>0) {
			mTxNoEvent.setVisibility(View.GONE);
		}
	}
	@Override
	public boolean onBackPress() {
		return false;
	}
	@Override
	public void onResume() {
		super.onResume();
		Logg.i(TAG, "onResume"+this.getClass().getSimpleName());
	}
	@Override
	public void onStart() {
		super.onStart();
		Logg.i(TAG, "onStart");
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Logg.i(TAG, "onDestroyView");
	}
	@Override
	public void onPause() {
		super.onPause();
		Logg.i(TAG, "onPause");
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.itemEventLog:
			break;

		default:
			break;
		}
	}
	@Override
	public void showLoading() {
		if (mDialog == null) {
			mDialog = new ProgressDialog(mContext);
			mDialog.setMessage(mContext.getResources().getString(
					R.string.loading));
		}
		mDialog.show();
		
	}
	@Override
	public void dissLoading() {
		// TODO 自动生成的方法存根
		if (mDialog!=null) {
			mDialog.dismiss();
		}
	}
	@Override
	public void getChDetailSuc(EvseDetailData datas) {
		mDetailDialog.show();
		mDetailDialog.setData(datas);
		
	}
	@Override
	public void getChDetailFia(VolleyError error) {
		if (error==null) {
			Toast.makeText(getActivity(), R.string.no_more, Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(getActivity(), R.string.load_failed, Toast.LENGTH_SHORT).show();
		}
	}

}
