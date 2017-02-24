package com.delta.smsandroidproject.dialog;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
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
import com.delta.smsandroidproject.bean.ChargerInfoData;
import com.delta.smsandroidproject.bean.ChargerListData;
import com.delta.smsandroidproject.bean.ChargerListData.Evse;
import com.delta.smsandroidproject.bean.InfoByPush;
import com.delta.smsandroidproject.bean.InfoByPush.ChargerPush;
import com.delta.smsandroidproject.bean.InfoByPush.EvsePush;
import com.delta.smsandroidproject.request.JsonCookieRequest;
import com.delta.smsandroidproject.request.ParseResponse;
import com.delta.smsandroidproject.request.SessionTimeoutHandler;
import com.delta.smsandroidproject.request.StringCookieRequest;
import com.delta.smsandroidproject.util.Comment;
import com.delta.smsandroidproject.util.Logg;
import com.delta.smsandroidproject.util.SetTextUtils;
import com.delta.smsandroidproject.util.ToolUtil;
import com.delta.smsandroidproject.view.BindViewTool;
import com.delta.smsandroidproject.view.ViewInitHelper;

public class ActionDialog extends Dialog implements
		android.view.View.OnClickListener {

	private static final String OFFLINE = "Offline";// -1
	private static final String AVAILABLE = "Available"; // 0
	private static final String OCCUPIED = "Occupied"; // 1
	private static final String UNAVAILABLE = "Unavailable";// 3
	private static final String RESERVED = "Reserved";// 4
	private static final String FAULTED = "Faulted";

	@BindViewTool(id = R.id.action_title, changeFont = true)
	private TextView tvTitle;

	@BindViewTool(id = R.id.action_charger_status, changeFont = true)
	private TextView tvStatus;

	@BindViewTool(id = R.id.tv_action_reboot, clickable = true, canTouch = true, changeFont = true)
	private TextView tvRebootCharger;

	@BindViewTool(id = R.id.tv_action_reset, clickable = true, canTouch = true, changeFont = true)
	private TextView tvResetCharger;

	@BindViewTool(id = R.id.tv_action_disable, clickable = true, canTouch = true, changeFont = true)
	private TextView tvDisableCharger;

	@BindViewTool(id = R.id.tv_action_enable, clickable = true, canTouch = true, changeFont = true)
	private TextView tvEnableCharger;

	@BindViewTool(id = R.id.tv_action_firmware, clickable = true, canTouch = true, changeFont = true)
	private TextView tvFirmwareUpdate;

	@BindViewTool(id = R.id.tv_action_whitelist, clickable = true, canTouch = true, changeFont = true)
	private TextView tvWhitelistUpdate;

	@BindViewTool(id = R.id.action_title_evse, changeFont = true)
	private TextView tvTitleEVSE;

	@BindViewTool(id = R.id.action_evse_status, changeFont = true)
	private TextView tvEVSEStatus;

	@BindViewTool(id = R.id.tv_action_start, clickable = true, canTouch = true, changeFont = true)
	private TextView tvStartChargingSession;

	@BindViewTool(id = R.id.tv_action_stop, clickable = true, canTouch = true, changeFont = true)
	private TextView tvStopChargingSession;

	@BindViewTool(id = R.id.tv_action_release, clickable = true, canTouch = true, changeFont = true)
	private TextView tvReleaseLock;

	@BindViewTool(id = R.id.tv_action_disable_evse, clickable = true, canTouch = true, changeFont = true)
	private TextView tvDisableEVSE;

	@BindViewTool(id = R.id.tv_action_enable_evse, clickable = true, canTouch = true, changeFont = true)
	private TextView tvEnableEVSE;

	private Context mContext;
	private ChargerInfoData data;
	private ChargerListData dataDetail;
	private Evse evse;
	private String userId;

	public static ActionDialog instance;
	private LinearLayout llEvseContainer;
	private MyProgressDialog dialog;

	private View contentView;
	private LinearLayout llProgress;

	String chargerStatus = FAULTED;
	String evseStatus = FAULTED;

	public ActionDialog(Context context, int style) {
		super(context, style);
		this.mContext = context;
		instance = this;
	}

	public static void close() {

		if (instance != null) {
			instance.dismiss();
		}
	}

	public void setData(ChargerInfoData chargerInfoData,
			ChargerListData pDataDetail, Evse pEvse) {
		data = chargerInfoData;
		dataDetail = pDataDetail;
		evse = pEvse;

		Logg.i("chargerInfoData", ":" + data);
		Logg.i("dataDetail", ":" + dataDetail);
		Logg.i("evse", ":" + evse);

		init();
	}

	@SuppressLint("InflateParams")
	private void init() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		contentView = LayoutInflater.from(mContext).inflate(
				R.layout.dialog_action, null);
		setContentView(contentView);

		boolean isSuper = !ToolUtil.notService();

		initView(isSuper, contentView);
		initWindow(isSuper);

		dialog = new MyProgressDialog(mContext);

		if (data != null) {
			getStatusFromServer(data.getId());
		}
	}

	// 初始化Window
	private void initWindow(boolean isSuper) {
		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		int height = 580;
		if (evse == null) {
			llEvseContainer.setVisibility(View.GONE);
			height = 320;
		}

		isSuper = !ToolUtil.notService();
		if (!isSuper) {
			height -= 80;
		}

		lp.height = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, height, mContext.getResources()
						.getDisplayMetrics());
		lp.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				300, mContext.getResources().getDisplayMetrics());
		lp.gravity = Gravity.CENTER;
		dialogWindow.setAttributes(lp);
	}

	private void initView(Boolean isSuper, View view) {
		// 初始化所有view成员
		ViewInitHelper.initView(this, view);

		llProgress = (LinearLayout) view.findViewById(R.id.view_stub_progress);
		llProgress.setVisibility(View.GONE);

		llEvseContainer = (LinearLayout) view
				.findViewById(R.id.ll_evse_container);

		LinearLayout llsuper = (LinearLayout) view.findViewById(R.id.ll_super);
		if (!isSuper) {
			llsuper.setVisibility(View.GONE);
		}

		if (data != null) {
			SetTextUtils.setText(tvTitle,
					mContext.getResources().getString(R.string.charger_title)
							+ data.getName());

			chargerStatus = reflectStatus(data.getStatus());
			SetTextUtils.setText(tvStatus, "(" + chargerStatus + ")");
			if (evse != null) {
				SetTextUtils.setText(tvTitleEVSE, mContext.getResources()
						.getString(R.string.evse_title) + evse.getName());
				evseStatus = reflectStatus(evse.getStatus());
				SetTextUtils.setText(tvEVSEStatus, "(" + evseStatus + ")");
			}
		}

		// 根据状态置灰
		setGrayByStatus();
	}

	public static String reflectStatus(String singal) {
		switch (singal) {
		case "-1":
			return OFFLINE;
		case "0":
			return AVAILABLE;
		case "1":
			return OCCUPIED;
		case "3":
			return UNAVAILABLE;
		case "4":
			return RESERVED;
		default:
			return FAULTED;
		}
	}

	// 根据Charger和EVSE的状态置灰
	public void setGrayByStatus() {
		Logg.i("setGrayByStatus", chargerStatus + "," + evseStatus);
		SetTextUtils.setText(tvStatus, "(" + chargerStatus + ")");
		SetTextUtils.setText(tvEVSEStatus, "(" + evseStatus + ")");
		resetStatus();

		if (chargerStatus.equals(OFFLINE)) {
			setAll2Gray();
		} else {
			if (!chargerStatus.equals(UNAVAILABLE)) {
				setView2Gray(tvEnableCharger);
			} else {
				setView2Gray(tvDisableCharger);
			}
			if (!(evseStatus.equals(AVAILABLE) || evseStatus.equals(RESERVED))) {
				setView2Gray(tvStartChargingSession);
			}
			if (!evseStatus.equals(OCCUPIED)) {
				setView2Gray(tvStopChargingSession);
			}
			if (!evseStatus.equals(UNAVAILABLE)) {
				setView2Gray(tvEnableEVSE);
			} else {
				setView2Gray(tvDisableEVSE);
			}
		}
	}

	// 将View内部所有的子View置灰并不可点击
	public void setAll2Gray() {
		Field[] fields = this.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				BindViewTool bindView = field.getAnnotation(BindViewTool.class);
				if (bindView != null) {
					int id = bindView.id();
					TextView childView = (TextView) contentView
							.findViewById(id);
					field.setAccessible(true);
					boolean touchable = bindView.canTouch();
					if (touchable) {
						childView.setTextColor(Color.GRAY);
						childView.setClickable(false);
					}
				}
			}
		}
	}

	// 将所有View重置为原来状态
	public void resetStatus() {
		Field[] fields = this.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				BindViewTool bindView = field.getAnnotation(BindViewTool.class);
				if (bindView != null) {
					int id = bindView.id();
					TextView childView = (TextView) contentView
							.findViewById(id);
					field.setAccessible(true);
					boolean touchable = bindView.canTouch();
					if (touchable) {
						childView.setTextColor(Color.BLACK);
						childView.setClickable(true);
					}
				}
			}
		}
	}

	public static void setView2Gray(TextView view) {
		view.setTextColor(Color.GRAY);
		view.setClickable(false);
	}

	@Override
	public void onClick(View v) {
		if (data != null) {
			userId = ToolUtil.getUid();
			Log.d("enable", "userId:" + userId);
			switch (v.getId()) {
			case R.id.tv_action_reboot:
				rebootCharger();
				break;
			case R.id.tv_action_reset:
				resetCharger();
				break;
			case R.id.tv_action_disable:
				disableCharger();
				break;
			case R.id.tv_action_enable:
				enableCharger();
				break;
			case R.id.tv_action_firmware:
				firmwareUpdate();
				break;
			case R.id.tv_action_whitelist:
				whitelistUpdate();
				break;
			case R.id.tv_action_start:
				startChargingSession();
				break;
			case R.id.tv_action_stop:
				stopChargingSession();
				break;
			case R.id.tv_action_release:
				releaseLock();
				break;
			case R.id.tv_action_enable_evse:
				enableEVSE();
				break;
			case R.id.tv_action_disable_evse:
				disableEVSE();
				break;
			default:
				break;
			}
		}
	}

	private void rebootCharger() {
		HashMap<String, String> map = new HashMap<>();
		map.put("ChargerId", data.getId());
		map.put("UserId", userId);
		map.put("Type", "Hard");
		commandToRemote(Comment.RESET_AND_REBOOT, map, true);

		showSnackBar();
		// viewStub.inflate();
	}

	private void resetCharger() {
		HashMap<String, String> map = new HashMap<>();
		map.put("ChargerId", data.getId());
		map.put("UserId", userId);
		map.put("Type", "Soft");
		commandToRemote(Comment.RESET_AND_REBOOT, map, false);
		this.dismiss();
	}

	private void disableCharger() {
		HashMap<String, String> map = new HashMap<>();
		map.put("ChargerId", data.getId());
		map.put("UserId", userId);
		map.put("Type", "Inoperative");
		commandToRemote(Comment.ENABLE_AND_DISENABLE, map, false);
		this.dismiss();
	}

	private void enableCharger() {
		HashMap<String, String> map = new HashMap<>();
		map.put("ChargerId", data.getId());
		map.put("UserId", userId);
		map.put("Type", "Operative");
		commandToRemote(Comment.ENABLE_AND_DISENABLE, map, false);
		this.dismiss();
	}

	private void firmwareUpdate() {
		FirmwareDialog firmwareDialog = new FirmwareDialog(mContext,
				R.style.radius_dialog);
		firmwareDialog.setData(dataDetail);
		firmwareDialog.show();
		this.dismiss();
	}

	private void whitelistUpdate() {
		WhiteListDialog whiteListDialog = new WhiteListDialog(mContext,
				R.style.radius_dialog);
		whiteListDialog.setData(data.getId(), "0", "differential", "0",
				dataDetail);
		whiteListDialog.show();
		this.dismiss();
	}

	private void startChargingSession() {
		HashMap<String, String> map = new HashMap<>();
		map.put("ChargerId", data.getId());
		map.put("EvseId", evse.getId());
		map.put("UserId", userId);
		String idTag = "start" + System.currentTimeMillis();
		saveRecordID(idTag);
		map.put("idTag", idTag);
		commandToRemote(Comment.START_REMOTE, map, true);

		showSnackBar();
		// viewStub.inflate();
	}

	private void stopChargingSession() {
		HashMap<String, String> map = new HashMap<>();
		map.put("ChargerId", data.getId());
		map.put("EvseId", evse.getId());
		map.put("UserId", userId);
		SharedPreferences sp = mContext.getSharedPreferences("action",
				Context.MODE_PRIVATE);
		String idTag = sp.getString("recordId", null);
		if (idTag != null) {
			map.put("idTag", idTag);
			commandToRemote(Comment.STOP_REMOTE, map, true);
		}

		showSnackBar();
		// viewStub.inflate();
	}

	private void releaseLock() {
		HashMap<String, String> map = new HashMap<>();
		map.put("ChargerId", data.getId());
		map.put("EvseId", evse.getId());
		map.put("UserId", userId);
		commandToRemote(Comment.UNLOCK, map, false);
		this.dismiss();
	}

	private void enableEVSE() {
		HashMap<String, String> map = new HashMap<>();
		map.put("ChargerId", data.getId());
		map.put("EvseId", evse.getId());
		map.put("UserId", userId);
		map.put("Type", "Operative");
		commandToRemote(Comment.ENABLE_AND_DISENABLE, map, false);
		this.dismiss();
	}

	private void disableEVSE() {
		HashMap<String, String> map = new HashMap<>();
		map.put("ChargerId", data.getId());
		map.put("EvseId", evse.getId());
		map.put("UserId", userId);
		map.put("Type", "Inoperative");
		commandToRemote(Comment.ENABLE_AND_DISENABLE, map, false);
		this.dismiss();
	}

	// 发送命令到服务器
	private void commandToRemote(String path, HashMap<String, String> map,
			final boolean isWebSocket) {
		Logg.i("commandToRemote", "path:" + path);
		if (!isWebSocket) {
			dialog.show(mContext.getResources().getString(R.string.loading));
		}
		JSONObject jsonObject = new JSONObject(map);
		Logg.i("commandToRemote", "map:" + jsonObject.toString());
		JsonRequest<JSONObject> jsonRequest = new JsonCookieRequest(
				Method.POST, path, jsonObject,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.d("enable", "response -> " + response.toString());
						try {
							String result = response.getString("Result");
							if (result != null) {
								if (result.equals("failed")) {
									showAlertMessage(R.string.action_failed,
											isWebSocket);
								} else {
									showAlertMessage(result, isWebSocket);
								}
							} else {
								showAlertMessage(R.string.dialog_net_alert,
										isWebSocket);
							}
						} catch (JSONException e) {
							e.printStackTrace();
							Log.e("exception", e.getMessage());
						}
						dialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("enable", error.getMessage(), error);
						dialog.dismiss();
						if (SessionTimeoutHandler.handSessionTimeout(
								getContext(), error)) {
							return;
						}
						showAlertMessage(R.string.dialog_net_alert, isWebSocket);
					}
				});

		jsonRequest.setRetryPolicy(new DefaultRetryPolicy(6000 * 10, 1, 1.0f));
		SMSApplication.getRequestQueue().add(jsonRequest);
	}

	private void saveRecordID(String idTag) {
		Logg.i("enable", "recordId:" + idTag);
		SharedPreferences sp = mContext.getSharedPreferences("action",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("recordId", idTag);
		editor.commit();
	}

	private void showAlertMessage(int messageId, boolean isWebSocket) {
		if (!isWebSocket) {
			Toast.makeText(mContext,
					mContext.getResources().getString(messageId),
					Toast.LENGTH_SHORT).show();
		}
	}

	private void showAlertMessage(String message, boolean isWebSocket) {
		if (!isWebSocket) {
			Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
		}
	}

	private void showSnackBar() {
		String message = mContext.getString(R.string.command_alert);
		Snackbar.make(contentView, message, Snackbar.LENGTH_SHORT).show();
	}

	// 根据WebSocket的推送信息置灰
	public void setGrayByPushInfo(String info) {
		if (instance != null && instance.isShowing()) {
			if (info != null) {
				InfoByPush pushInfo = new InfoByPush();
				try {
					pushInfo = GsonTools.changeGsonToBean(info,
							InfoByPush.class);
				} catch (Exception e) {
					Logg.i("setGrayByPushInfo", "exception:" + e);
					return;
				}
				ChargerPush chargerPush = pushInfo.getCharger();
				if (chargerPush != null) {
					chargerStatus = reflectStatus(chargerPush
							.getChargerStatus());
				}
				if (evse != null) {
					List<EvsePush> evsePushs = pushInfo.getEvse();
					if (evsePushs != null && evsePushs.size() > 0) {
						for (EvsePush e : evsePushs) {
							if (e.getEvseId().equals(evse.getId())) {
								evseStatus = reflectStatus(e.getEvseStatus());
							}
						}
					}
				}
				setGrayByStatus();
				Logg.i("setGrayByPushInfo", "chargerStatus-" + chargerStatus
						+ ",evseStatus-" + evseStatus);
			}
		}
		Logg.i("setGrayByPushInfo", "come here:" + info);
	}

	public void getStatusFromServer(String chargerId) {
		llProgress.setVisibility(View.VISIBLE);
		String path = String.format(Comment.CHARGER_LIST_URL, chargerId);
		Logg.i("getStatusFromServer", "path:" + path);
		StringCookieRequest request = new StringCookieRequest(path,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						response = ParseResponse.parseResponse(response);
						Logg.i("getStatusFromServer", "response:" + response);
						ChargerListData data = GsonTools.changeGsonToBean(
								response, ChargerListData.class);
						llProgress.setVisibility(View.GONE);
						if (data != null) {
							setStatusByServerInfo(data);
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						llProgress.setVisibility(View.GONE);
						if (SessionTimeoutHandler.handSessionTimeout(
								getContext(), error)) {
							return;
						}
						Logg.i("getStatusFromServer", "error:" + error);
					}
				});

		SMSApplication.getRequestQueue().add(request);
	}

	private void setStatusByServerInfo(ChargerListData data) {
		chargerStatus = reflectStatus(data.getStatus());

		Logg.i("Data-1", "data:" + this.data);
		Logg.i("Data-2", "data:" + data);

		Evse[] evses = data.getEvse();
		if (evses != null) {
			for (Evse evse : evses) {
				if (this.evse != null && this.evse.getId().equals(evse.getId())) {
					evseStatus = reflectStatus(evse.getStatus());

					Logg.i("EVSE-1", "evse:" + this.evse);
					Logg.i("EVSE-2", "evse:" + evse);
				}
			}
		}

		Logg.i("setStatusByServerInfo", "evse:" + this.evse);
		Logg.i("setStatusByServerInfo", "chargerStatus:" + chargerStatus);
		Logg.i("setStatusByServerInfo", "evseStatus:" + evseStatus);
		setGrayByStatus();
	}
}
