package com.delta.smsandroidproject.dialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.delta.smsandroidproject.R;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.bean.ChargerListData;
import com.delta.smsandroidproject.request.JsonCookieRequest;
import com.delta.smsandroidproject.request.SessionTimeoutHandler;
import com.delta.smsandroidproject.util.Comment;
import com.delta.smsandroidproject.util.ToolUtil;

public class WhiteListDialog extends Dialog implements
		android.view.View.OnClickListener {

	private Context mContext;
	private TextView tvVersionCode, tvUpdate, tvCancel;
	private MyProgressDialog dialog;

	private String chargerId;
	private String evseId;
	private String updateType;
	private String listVersion;
	private ChargerListData data;

	public WhiteListDialog(Context context, int style) {
		super(context, style);
		this.mContext = context;
	}

	public void setData(String chargerId, String evseId, String updateType,
			String listVersion, ChargerListData data) {

		this.chargerId = chargerId;
		this.evseId = evseId;
		this.updateType = updateType;
		this.listVersion = listVersion;
		this.data = data;

		init();
	}

	@SuppressLint("InflateParams")
	private void init() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		View contentview = LayoutInflater.from(mContext).inflate(
				R.layout.dialog_whitelist, null);
		setContentView(contentview);

		initWindow();
		initView(contentview);

		dialog = new MyProgressDialog(mContext);
	}

	private void initWindow() {
		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.height = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 230, mContext.getResources()
						.getDisplayMetrics());
		lp.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				300, mContext.getResources().getDisplayMetrics());
		lp.gravity = Gravity.CENTER;
		dialogWindow.setAttributes(lp);
	}

	private void initView(View view) {
		tvVersionCode = (TextView) view
				.findViewById(R.id.tv_whitelist_versioncode);
		tvUpdate = (TextView) view.findViewById(R.id.button_update);
		tvCancel = (TextView) view.findViewById(R.id.button_cancel);

		tvVersionCode.setOnClickListener(this);
		if (data != null) {
			String version = mContext.getResources()
					.getString(R.string.version);
			String code = data.getFirmware();
			tvVersionCode.setText(String.format(version, code));
		}
		tvUpdate.setOnClickListener(this);
		tvCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_update:
			HashMap<String, String> map = new HashMap<>();
			map.put("ChargerId", chargerId);
			map.put("EvseId", evseId);
			map.put("UserId", ToolUtil.getUid());
			map.put("UpdateType", updateType);
			map.put("ListVersion", listVersion);
			commandToRemote(Comment.UNDATE_LOCAL_WHITELIST, map);
			break;
		case R.id.button_cancel:
			break;

		default:
			break;
		}
		if (this.isShowing()) {
			this.dismiss();
		}
	}

	public static void downLoadFromUrl(String urlStr, String fileName,
			String savePath) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.connect();
		if (conn.getResponseCode() == 200) {
			InputStream inputStream = conn.getInputStream();
			byte[] getData = readInputStream(inputStream);
			File saveDir = new File(savePath);
			if (!saveDir.exists()) {
				saveDir.mkdir();
			}
			File file = new File(saveDir + File.separator + fileName);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(getData);
			if (fos != null) {
				fos.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		} else if (conn.getResponseCode() == 400) {

		} else {

		}

		Log.i("Update-whitelist", "info:" + url + " download success");

	}

	public static byte[] readInputStream(InputStream inputStream)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}

	private void commandToRemote(String path, HashMap<String, String> map) {
		dialog.show(mContext.getResources().getString(R.string.loading));
		JSONObject jsonObject = new JSONObject(map);
		Log.d("updateWhiteList", "path-> " + path);
		Log.d("updateWhiteList", "map-> " + jsonObject.toString());
		JsonRequest<JSONObject> jsonRequest = new JsonCookieRequest(
				Method.POST, path, jsonObject,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.d("enable", "response -> " + response.toString());
						try {
							String result = response.getString("Result");
							if (result != null) {
								Toast.makeText(mContext, result,
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(mContext,
										R.string.dialog_net_alert,
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
							Log.e("exception", e.getMessage());
							Toast.makeText(mContext, "Server error!",
									Toast.LENGTH_SHORT).show();
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
						Toast.makeText(mContext, R.string.dialog_net_alert,
								Toast.LENGTH_SHORT).show();
					}
				});

		jsonRequest.setRetryPolicy(new DefaultRetryPolicy(6000 * 10, 1, 1.0f));
		SMSApplication.getRequestQueue().add(jsonRequest);
	}
}
