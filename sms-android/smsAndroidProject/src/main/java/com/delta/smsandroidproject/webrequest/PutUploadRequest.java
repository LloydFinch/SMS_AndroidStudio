package com.delta.smsandroidproject.webrequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.delta.smsandroidproject.app.SMSApplication;

public class PutUploadRequest extends Request<String> {
	// public class PostUploadRequest extends StringRequest {
	/**
	 * 正确数据的时候回掉用
	 */
	private ResponseListener mListener;
	/* 请求 数据通过参数的形式传入 */
	private List<FormImage> mListItem;
	private String BOUNDARY = "--------------ast1212"; // 数据分隔线
	private String MULTIPART_FORM_DATA = "multipart/form-data";
	private static Map<String, String> map;
	private List<RequestItem> requestList;

	// private final Map<String, String> mHeaders;
	public static void uploadImg(Bitmap bitmap, ResponseListener listener,
			List<RequestItem> requestList, String url) {
		List<FormImage> imageList = new ArrayList<FormImage>();
		imageList.add(new FormImage(bitmap));
		Request<String> request = new PutUploadRequest(url, imageList,
				listener, requestList) {

		};
		SMSApplication.getRequestQueue().add(request);
	}

	public PutUploadRequest(String url, List<FormImage> listItem,
			ResponseListener listener, List<RequestItem> requestList) {
		// super(Method.PUT, url, requestListener(), errorListener());
		super(Method.PUT, url, listener);
		this.mListener = listener;
		setShouldCache(false);
		this.requestList = requestList;
		mListItem = listItem;
		// 设置请求的响应事件，因为文件上传需要较长的时间，所以在这里加大了，设为5秒
		setRetryPolicy(new DefaultRetryPolicy(5000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	}

	/**
	 * 这里开始解析数据
	 * 
	 * @param response
	 *            Response from the network
	 * @return
	 */
	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		try {
			String mString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			Log.v("ljy-parseNetworkResponse", "====mString===" + mString);

			return Response.success(mString,
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		}
	}

	/**
	 * 回调正确的数据
	 * 
	 * @param response
	 *            The parsed response returned by
	 */
	@Override
	protected void deliverResponse(String response) {
		mListener.onResponse(response);
	}

	// @Override
	// public Map<String, String> getHeaders() throws AuthFailureError {
	// return mHeaders != null ? mHeaders : super.getHeaders();
	// }
	@Override
	public byte[] getBody() throws AuthFailureError {

		if (mListItem == null || mListItem.size() == 0) {
			return super.getBody();
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// ByteArrayOutputStream bos1 = null;
		int N = mListItem.size();
		FormImage formImage;
		for (int i = 0; i < N; i++) {
			formImage = mListItem.get(i);
			StringBuffer sb = new StringBuffer();
			/* 第一行 */
			sb.append("--" + BOUNDARY);
			sb.append("\r\n");
			/* 第二行 */
			sb.append("Content-Disposition: form-data;");
			sb.append(" name=\"");
			sb.append(formImage.getName());
			sb.append("\"");
			sb.append("; filename=\"");
			sb.append(formImage.getFileName());
			sb.append("\"");
			sb.append("\r\n");
			/* 第三行 */
			sb.append("Content-Type: ");
			sb.append(formImage.getMime());
			sb.append("\r\n");
			/* 第四行 */
			sb.append("\r\n");
			try {
				bos.write(sb.toString().getBytes("utf-8"));
				/* 第五行 */
				bos.write(formImage.getValue());
				bos.write("\r\n".getBytes("utf-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			for (int j = 0; j < requestList.size(); j++) {

				StringBuffer sb1 = new StringBuffer();
				/* 第一行 */
				sb1.append("--" + BOUNDARY);
				sb1.append("\r\n");
				/* 第二行 */
				sb1.append("Content-Disposition: form-data;");
				sb1.append(" name=\"");
				sb1.append(requestList.get(j).getmName());
				sb1.append("");
				// sb.append("; filename=\"") ;
				// sb.append(formImage.getFileName()) ;
				sb1.append("\"");
				sb1.append("\r\n");
				/* 第三行 */
				sb1.append("");
				// sb.append("String") ;
				// sb.append("\r\n") ;
				/* 第四行 */
				sb1.append("\r\n");
				sb1.append(requestList.get(j).getmValue());

				try {
					bos.write(sb1.toString().getBytes("utf-8"));
					/* 第五行 */
					// bos.write(valus[i].getBytes("utf-8"));
					bos.write("\r\n".getBytes("utf-8"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		/* 结尾行 */
		String endLine = "--" + BOUNDARY + "--" + "\r\n";
		try {
			bos.write(endLine.toString().getBytes("utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Log.v("zgy", "=====formImage====\n" + bos.toString());
		return bos.toByteArray();
	}

	// Content-Type: multipart/form-data; boundary=----------
	@Override
	public String getBodyContentType() {
		return MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY;
		// return MULTIPART_FORM_DATA;
	}

	public static Response.ErrorListener errorListener() {
		return new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("login failed", "" + error.getMessage());

			}
		};

	}

	public static Listener<String> requestListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String json) {
				Log.i("login json", "" + json);

			}
		};
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = super.getHeaders();
		if (headers == null || headers.equals(Collections.emptyMap())) {
			headers = new HashMap<String, String>();
		}
		SMSApplication.getInstance().addSession(headers);
		return headers;
	}
}
