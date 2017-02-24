package com.delta.smsandroidproject.util;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.delta.smsandroidproject.EventView;
import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.bean.EventListData;
import com.delta.smsandroidproject.popupview.EventLogTimePopuView;
import com.delta.smsandroidproject.presenter.EventPresenter;
import com.delta.smsandroidproject.view.adapter.EventLogCardViewAdapter;
import com.delta.smsandroidproject.view.fragment.EventLogChargerFragment;
import com.delta.smsandroidproject.view.fragment.EventLogEvseFragment;

/**
 * load eventlog data
 * @author Jianzao.Zhang
 *
 */
public class EventLogUtil implements EventView{
	private final static String TAG = "EventLogUtil";
	private ProgressDialog mDialog;
	private Context mContext;
	private List<EventListData> datas;
	private List<EventListData> allDatas = new ArrayList<>();//mLoadMoreRecyclerView加载更多-累加数据
	private EventPresenter presenter;
	private EventLogCardViewAdapter mAdapter;
	private String tag;
	private static EventLogUtil eventLogUtil;
	private String locationId;
	private AnimRFRecyclerView mLoadMoreRecyclerView;
	private boolean isLoadEvenLogDataFinish;
	private Fragment fragment;
	private List<String> types;
	private String OrderById;
	
	public EventLogUtil(Context context,EventLogCardViewAdapter adapter, String tag, AnimRFRecyclerView mLoadMoreRecyclerView, Fragment fragment) {
		this.mContext = context;
		this.mAdapter = adapter;
		this.tag = tag;
		this.fragment = fragment;
		this.mLoadMoreRecyclerView = mLoadMoreRecyclerView;
		presenter = new EventPresenter(this,context);
	}
	
	/**
	 * charger eventlog数据加载
	 * @param ChargerId
	 * @param LocationId
	 * @param OrderById
	 * @param types
	 * @param pageNo
	 * @param perPage
	 */
	public void loadData(String ChargerId,String LocationId,String OrderById,List<String> types,String startTime,String endTime,int pageNo,int perPage) {
		this.locationId = LocationId;
//		if (this.types!=null) {
//			this.types.clear();
//		}
		this.OrderById = OrderById;
		this.types = types;
		Logg.i(TAG+"loadData-types", ""+types);
		StringBuilder sBuilder = new StringBuilder();
		if (!ChargerId.isEmpty()) {
			sBuilder.append("ChargerId=").append(ChargerId).append("&");
		}
		if (!LocationId.isEmpty()) {
			sBuilder.append("LocationId=").append(LocationId).append("&");
		}
		if (!OrderById.isEmpty()) {
			sBuilder.append("OrderById=").append(OrderById).append("&");
		}
		Logg.i(TAG, "startTime"+startTime);
		Logg.i(TAG, "endTime"+endTime);
		if (!startTime.isEmpty()&&(!startTime.equals(endTime)||startTime.equals(EventLogTimePopuView.ALL))) {
			sBuilder.append("StartTime=").append(startTime).append("&");
		}
		if (!endTime.isEmpty()&&(!startTime.equals(endTime)||endTime.equals(EventLogTimePopuView.ALL))) {
			sBuilder.append("StopTime=").append(endTime).append("&");
		}
		if (types!=null&&!types.isEmpty()) {
			sBuilder.append("LevelId=");
			for (int i = 0; i < types.size(); i++) {
				sBuilder.append(types.get(i));
				if (i<types.size()-1) {
					sBuilder.append(",");
				}
			}
			sBuilder.append("&");
		}
		sBuilder.append("PageNo=").append(pageNo).append("&");
		sBuilder.append("PerPage=").append(perPage);
		presenter.loadData(sBuilder.toString());
	}
	
	/**
	 * evse eventlog 数据加载
	 * @param LocationId
	 * @param ChargerId
	 * @param EvseId
	 * @param OrderById
	 * @param types
	 * @param pageNo
	 * @param perPage
	 */
	public void loadEvseEventData(String LocationId,String ChargerId,String EvseId,String OrderById,List<String> types,String startTime,String endTime,int pageNo,int perPage){
//		if (this.types!=null) {
//			this.types.clear();
//		}
		this.OrderById = OrderById;
		this.types = types;
		Logg.i(TAG+"loadEvseEventData-types", ""+types);
		StringBuilder sBuilder = new StringBuilder();
		if (!LocationId.isEmpty()) {
			sBuilder.append("LocationId=").append(LocationId).append("&");
		}
		if (!ChargerId.isEmpty()) {
			sBuilder.append("ChargerId=").append(ChargerId).append("&");
		}
		if (!EvseId.isEmpty()) {
			sBuilder.append("EvseId=").append(EvseId).append("&");
		}
		if (!OrderById.isEmpty()) {
			sBuilder.append("OrderById=").append(OrderById).append("&");
		}
		Logg.i(TAG, "startTime"+startTime);
		Logg.i(TAG, "endTime"+endTime);
		if (!startTime.isEmpty()&&!startTime.equals(endTime)) {
			sBuilder.append("StartTime=").append(startTime).append("&");
		}else if (!startTime.isEmpty()&&startTime.equals(EventLogTimePopuView.ALL)){
			sBuilder.append("StartTime=").append(startTime).append("&");
		}
		if (!endTime.isEmpty()&&!startTime.equals(endTime)) {
			sBuilder.append("StopTime=").append(endTime).append("&");
		}else if (!endTime.isEmpty()&&endTime.equals(EventLogTimePopuView.ALL)){
			sBuilder.append("StopTime=").append(startTime).append("&");
		}
		if (types!=null&&!types.isEmpty()) {
			sBuilder.append("LevelId=");
			for (int i = 0; i < types.size(); i++) {
				sBuilder.append(types.get(i));
				if (i<types.size()-1) {
					sBuilder.append(",");
				}
			}
			sBuilder.append("&");
		}
		sBuilder.append("PageNo=").append(pageNo).append("&");
		sBuilder.append("PerPage=").append(perPage);
		presenter.loadData(sBuilder.toString());
		Logg.i(TAG, "sBuilder.toString()"+sBuilder.toString());
	}
	
	/**
	 * 过滤eventtype
	 * @param types
	 */
	private List<EventListData> filtEventType(List<EventListData> allDatas){
		if (allDatas!=null&&allDatas.size()>0) {
				List<EventListData> eventChargerIdDatas = getEventChargerIdDatas(allDatas);
				return eventChargerIdDatas;
		}
		return null;
	}
	
	/**
	 * 清空数据
	 */
	public void removeEventLogData(){
		Logg.i("removeEventLogData--mAdapter", ""+mAdapter);
		if (allDatas.size()>0 && mAdapter!=null) {
			allDatas.clear();
			mAdapter.setDatas(null);
		}
	}
	
	@Override
	public void showDialog() {
		if (mDialog == null) {
			mDialog = new ProgressDialog(mContext);
			mDialog.setMessage(mContext.getResources().getString(
					R.string.loading));
		}
		mDialog.show();
	}

	@Override
	public void dismiss() {
		if (mDialog!=null) {
			mDialog.dismiss();
		}
	}


	@Override
	public List<EventListData> getEventDatas() {
		return datas;
	}

	@Override
	public void setEventDatas(List<EventListData> datas) {
		this.datas = datas;
		Logg.i(TAG, "tag-"+tag+"datas-"+datas);
		if (datas!=null) {
			for (EventListData eventListData : datas) {
				allDatas.add(eventListData);
			}
			if (tag.equals(EventLogChargerFragment.TAG)) {
				List<EventListData> list = filtEventType(allDatas);
				mAdapter.setDatas(list);
				if (fragment!=null) {
					((EventLogChargerFragment) fragment).noEventPromot(list,OrderById);
				}
			}else {
				mAdapter.setDatas(allDatas);
				if (fragment!=null) {
					((EventLogEvseFragment) fragment).noEventPromot(allDatas,OrderById);
				}
			}
			mLoadMoreRecyclerView.getAdapter().notifyDataSetChanged();
			// 加载更多完成后调用，必须在UI线程中 
			mLoadMoreRecyclerView.loadMoreComplate();
		}else {
			
		}
	}
	
	private List<EventListData> getEventChargerIdDatas(List<EventListData> datas){
		if (datas!=null) {
			List<EventListData> list = new ArrayList<>();
			for (EventListData data : datas) {
				if (data.getEvseId()==null) {
					list.add(data);
				}
				if (data.getEvseId()!=null&&data.getEvseId().equals("0")) {
					list.add(data);
				}
			}
			Logg.i("list.size()", ""+list.size());
			return list;
		}
		return null;
	}
	
	private List<EventListData> getEventEvseIdDatas(List<EventListData> datas){
		if (datas!=null) {
			List<EventListData> list = new ArrayList<>();
			for (EventListData data : datas) {
				if (data.getEvseId()!=null&&!data.getEvseId().equals("0")) {
					list.add(data);
				}
			}
			Logg.i("getEventEvseIdDatas--list.size()", ""+list.size());
			return list;
		}
		return null;
	}

	@Override
	public void isLoadDataFinish(boolean b) {
		this.isLoadEvenLogDataFinish = b;
		if (b) {
			ToastCustom.showToast(mContext, mContext.getResources().getString(R.string.load_event_log_data_finish), ToastCustom.LENGTH_SHORT);
		}
	}
	
	public boolean isLoadDataFinish(){
		return isLoadEvenLogDataFinish;
		
	}
}
