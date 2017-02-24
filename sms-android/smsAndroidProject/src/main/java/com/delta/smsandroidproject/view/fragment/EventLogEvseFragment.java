package com.delta.smsandroidproject.view.fragment;

import java.util.List;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.bean.EventListData;
import com.delta.smsandroidproject.bean.EvseDetailData;
import com.delta.smsandroidproject.manager.AnimRFLinearLayoutManager;
import com.delta.smsandroidproject.presenter.EvseDetailPresenter.EvseDetail;
import com.delta.smsandroidproject.util.AnimRFRecyclerView;
import com.delta.smsandroidproject.util.EventLogUtil;
import com.delta.smsandroidproject.util.Logg;
import com.delta.smsandroidproject.util.ToastCustom;
import com.delta.smsandroidproject.view.adapter.EventLogCardViewAdapter;
import com.delta.smsandroidproject.view.iview.ILoadView;
import com.delta.smsandroidproject.view.otherview.DetailDialog;


/**
 * Event Log Evse
 * @author Jianzao.Zhang
 *
 */
public class EventLogEvseFragment extends BaseFragment implements ILoadView ,EvseDetail{
	public static String TAG = "EventLogEvseFragment";
	public String tag = "";
	private List<EventListData> datas;
	private FragmentActivity mContext;
	private String locationId;
	private AnimRFRecyclerView mLoadMoreRecyclerView;
	private int PAGE_NO =1;//第几页
	private int PER_PAGE = 25;//每页25条数据
	private EventLogCardViewAdapter mAdapter;
	private EventLogUtil eventLogUtil;
	private Toolbar mToolbar;
	private TextView mTxNoEvent;
	private String chargerId;
	private String evseId;
	private ProgressDialog mDialog;
	private DetailDialog mDetailDialog;
	public EventLogEvseFragment() {
	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		Logg.i(TAG, "onHiddenChanged");
		if (hidden) {
			EventLogEvseFragment.this.onResume();
		}else {
			EventLogEvseFragment.this.onPause();
		}
	}
	
	public void setTag(String tag){
		this.tag = tag;
		Logg.i(TAG+"tag", ""+tag);
	}
	
	public String getTag1(){
		return tag;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflate = inflater.inflate(R.layout.fragment_event_log_evse, container,
						false);
		mContext = getActivity();
		Logg.i(TAG, "onCreateView");
		initData();
		initView(inflate);
		
		return inflate;
	}
	
	/**
	 * 加载事件
	 * @param locationId
	 * @param chargerId
	 * @param evseId
	 * @param PAGE_NO
	 * @param PER_PAGE
	 * @param tag
	 */
	public void loadData(String locationId,String chargerId,String evseId,String OrderById,List<String> types,String startTime,String endTime,int PAGE_NO,int PER_PAGE,String tag){
		Logg.i(TAG+"PAGE_NO", ""+PAGE_NO);
		this.PAGE_NO = PAGE_NO;
		if (eventLogUtil!=null) {
			eventLogUtil.removeEventLogData();
		}else {
			eventLogUtil = new EventLogUtil(mContext,mAdapter,tag,mLoadMoreRecyclerView,this);
		}
		eventLogUtil.loadEvseEventData(locationId,chargerId,evseId,OrderById,types,startTime,endTime,PAGE_NO,PER_PAGE);
	}
	
	private void initData() {
		Bundle bundle = getArguments();
		if (bundle!=null) {
			locationId = bundle.getString("LocationId");
			chargerId = bundle.getString("ChargerID");
			evseId = bundle.getString("EvseId");
			Logg.i(TAG+"chargerId", ""+chargerId);
			Logg.i(TAG+"locationId", ""+locationId);
			Logg.i(TAG+"evseId", ""+evseId);
		}
	}

	/**
	 * 初始化布局
	 * @param inflate
	 */
	private void initView(View inflate) {
		mTxNoEvent = (TextView) inflate.findViewById(R.id.txNoEvent);
		mLoadMoreRecyclerView = (AnimRFRecyclerView) inflate.findViewById(R.id.loadMoreRecyclerView1);
		mLoadMoreRecyclerView.setHasFixedSize(true);
		mAdapter = new EventLogCardViewAdapter(getActivity());
		mAdapter.setCallBack(this);
		// 使用重写后的线性布局管理器
		mLoadMoreRecyclerView.setLayoutManager(new AnimRFLinearLayoutManager(getActivity()));
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
					eventLogUtil.loadEvseEventData(locationId,chargerId,evseId,EventLogPagerFragment.ODERBY,EventLogPagerFragment.typeList,
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
		loadData(locationId,chargerId,evseId, EventLogPagerFragment.ODERBY,EventLogPagerFragment.typeList,
				EventLogPagerFragment.START_TIME,EventLogPagerFragment.END_TIEM,PAGE_NO,PER_PAGE,tag);
		Logg.i(TAG, ""+TAG);
		mDetailDialog = new DetailDialog(getActivity());
	}
	public void noEventPromot(List<EventListData> datas,String promot) {
		if (datas==null) {
			mTxNoEvent.setVisibility(View.VISIBLE);
			mTxNoEvent.setText("No event log");
		}
		if (datas!=null&& datas.size()==0) {
			mTxNoEvent.setVisibility(View.VISIBLE);
			if (promot!=null&&promot.equals("title")) {
				//mTxNoEvent.setText("No event "+promot);
				mTxNoEvent.setText("No event log");
			}else {
				mTxNoEvent.setText("No event log");
			}
		}
		if (datas!=null&& datas.size()>0) {
			mTxNoEvent.setVisibility(View.GONE);
		}
	}
	@Override
	public void onPause() {
		super.onPause();
		Logg.i(TAG, "onPause");
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Logg.i(TAG, "onDestroyView");
	}
	@Override
	public void onResume() {
		super.onResume();
		Logg.i(TAG, "onResume-tag"+tag);
	}
	@Override
	public boolean onBackPress() {
		return false;
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
		if (mDialog!=null) {
			mDialog.dismiss();
		}
	}
	@Override
	public void getEvseDetailSuc(EvseDetailData datas) {
		// TODO 自动生成的方法存根
		mDetailDialog.show();
		mDetailDialog.setData(datas);
		
	}
	@Override
	public void getEvseDetailFai(VolleyError error) {
		// TODO 自动生成的方法存根
		if (error==null) {
			Toast.makeText(getActivity(), "没有更多信息", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(getActivity(), R.string.load_failed, Toast.LENGTH_SHORT).show();
		}
	}

}
