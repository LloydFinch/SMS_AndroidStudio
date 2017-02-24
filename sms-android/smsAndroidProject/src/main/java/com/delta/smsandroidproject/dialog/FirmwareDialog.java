package com.delta.smsandroidproject.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.delta.common.utils.GsonTools;
import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.bean.ChargerListData;
import com.delta.smsandroidproject.bean.FirmwareModel;
import com.delta.smsandroidproject.bean.FirmwareModel.FirmWare;
import com.delta.smsandroidproject.request.JsonCookieRequest;
import com.delta.smsandroidproject.request.ParseResponse;
import com.delta.smsandroidproject.request.SessionTimeoutHandler;
import com.delta.smsandroidproject.request.StringCookieRequest;
import com.delta.smsandroidproject.util.Comment;
import com.delta.smsandroidproject.util.Logg;
import com.delta.smsandroidproject.util.ToastCustom;
import com.delta.smsandroidproject.view.adapter.ChooseVersionAdapter;

public class FirmwareDialog extends Dialog implements
		android.view.View.OnClickListener {

	private Context mContext;
	private TextView tvVersion;
	private TextView tvUpdate, tvCancel;
	private RecyclerView versionList;

	private List<FirmWare> datas = new ArrayList<>();
	private ChooseVersionAdapter adapter;
	private ChargerListData data;

	public static FirmwareDialog instance;
	private ViewStub viewStub;
	private View contentView;

	public FirmwareDialog(Context context, int style) {
		super(context, style);
		this.mContext = context;
		instance = this;
	}

	public static void close() {
		if (instance != null) {
			instance.dismiss();
		}
	}

	public void setData(ChargerListData data) {
		this.data = data;
		Logg.i("dataDetail2", ":" + data);
		init();
	}

	@SuppressLint("InflateParams")
	private void init() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		contentView = LayoutInflater.from(mContext).inflate(
				R.layout.dialog_firmware, null);
		setContentView(contentView);

		initWindow();
		initView(contentView);

		// TODO change type
		String path = String.format(Comment.GET_FIRMWARE_LIST, "2");
		getFirmWareList(path);
	}

	private void initWindow() {
		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.height = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 300, mContext.getResources()
						.getDisplayMetrics());
		lp.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				300, mContext.getResources().getDisplayMetrics());
		lp.gravity = Gravity.CENTER;
		dialogWindow.setAttributes(lp);
	}

	private void initView(View view) {
		versionList = (RecyclerView) view.findViewById(R.id.list_version);
		versionList.setLayoutDirection(RecyclerView.VERTICAL);
		versionList.setLayoutManager(new LinearLayoutManager(mContext));
		versionList.setItemAnimator(new DefaultItemAnimator());

		adapter = new ChooseVersionAdapter(mContext, datas);
		versionList.setAdapter(adapter);

		tvVersion = (TextView) view.findViewById(R.id.tv_whitelist_versioncode);
		if (data != null) {
			String version = mContext.getResources()
					.getString(R.string.version);
			String code = data.getFirmware();
			tvVersion.setText(String.format(version, code));
		}

		tvUpdate = (TextView) view.findViewById(R.id.button_ok_firm);
		tvCancel = (TextView) view.findViewById(R.id.button_cancel_firm);
		tvUpdate.setOnClickListener(this);
		tvCancel.setOnClickListener(this);

		viewStub = (ViewStub) view.findViewById(R.id.websocket_progress_stub);
	}

	// 获取firmWare列表
	public void getFirmWareList(String path) {
		Logg.i("FirmWare", "path:" + path);
		StringCookieRequest request = new StringCookieRequest(path,
				new Listener<String>() {
					@Override
					public void onResponse(String response) {
						response = ParseResponse.parseResponse(response);
						Logg.i("FirmWare", "response:" + response);
						FirmwareModel firmwareModel = GsonTools
								.changeGsonToBean(response, FirmwareModel.class);
						if (firmwareModel != null) {
							Logg.i("FirmWare", "firmwareModel:" + firmwareModel);
							Logg.i("FirmWare", "size():"
									+ firmwareModel.getResults().size());

							datas.clear();
							datas.addAll(firmwareModel.getResults());
							adapter.notifyDataSetChanged();
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Logg.i("FirmWare", "error:" + error.toString());
						if (SessionTimeoutHandler.handSessionTimeout(
								getContext(), error)) {
							return;
						}
					}
				});
		SMSApplication.getRequestQueue().add(request);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_ok_firm:
			if (datas.size() > 0) {
				FirmWare firmWare = datas.get(adapter.getSelected());
				if (firmWare != null) {
					HashMap<String, String> map = new HashMap<>();
					// TODO change id
					map.put("ChargerIds", "A1-10,A1-11");
					map.put("FirmwareId", firmWare.getFirmwareId());
					updateFirmWare(Comment.UPDATE_FIRMWARE, map);

					showSnackBar();
					viewStub.inflate();
				}
				Logg.i("FirmWare", "version:" + firmWare.getVersion() + ",id:"
						+ firmWare.getFirmwareId());
			} else {
				Logg.i("FirmWare", "list is null? yes!");
			}
			break;
		case R.id.button_cancel_firm:
			this.dismiss();
			break;

		default:
			break;
		}
	}

	// 更新FirmWare
	private void updateFirmWare(String path, HashMap<String, String> map) {
		Logg.i("updateFirmWare", "path:" + path);
		JSONObject jsonObject = new JSONObject(map);
		Logg.i("updateFirmWare", "map:" + jsonObject.toString());
		JsonRequest<JSONObject> jsonRequest = new JsonCookieRequest(
				Method.POST, path, jsonObject,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Logg.d("updateFirmWare",
								"response -> " + response.toString());
						String message = mContext
								.getString(R.string.no_charge_link);
						try {
							message = response.getString("Message");
						} catch (JSONException e) {
							e.printStackTrace();
							Logg.i("updateFirmWare", "No charge to link");
						}
						ToastCustom.showToast(mContext, message,
								Toast.LENGTH_SHORT);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("updateFirmWare", error.getMessage(), error);
						if (SessionTimeoutHandler.handSessionTimeout(
								getContext(), error)) {
							return;
						}
						ToastCustom.showToast(mContext,
								R.string.dialog_net_alert, Toast.LENGTH_SHORT);
					}
				});

		jsonRequest.setRetryPolicy(new DefaultRetryPolicy(6000 * 10, 1, 1.0f));
		SMSApplication.getRequestQueue().add(jsonRequest);
	}

	private void showSnackBar() {
		String message = mContext.getString(R.string.command_alert);
		Snackbar.make(contentView, message, Snackbar.LENGTH_SHORT).show();
	}
}
