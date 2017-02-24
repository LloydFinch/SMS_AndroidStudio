package com.delta.smsandroidproject.model;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.delta.smsandroidproject.app.SMSApplication;
import com.delta.smsandroidproject.request.StringCookieRequest;
import com.delta.smsandroidproject.util.Comment;
import com.delta.smsandroidproject.util.Logg;
import com.google.gson.GsonBuilder;

public class LoginModelImp implements LoginModel {

	protected static final String TAG = "LoginModelImp";

	@Override
	public void cancelAll() {
		
	}

	@Override
	public void loadData(Listener<String> responseListener,
			ErrorListener errorLoginListener,final String uid,final String pw) {
		StringCookieRequest request = new StringCookieRequest(Method.POST, Comment.LOGIN_URL, responseListener, errorLoginListener){
			public byte[] getBody() throws AuthFailureError {
				// TODO Auto-generated method stub
				Map<String, String>map=new HashMap<>();
				map.put("Uid", uid);
				map.put("Password", pw);
				Log.i("Uid", ""+uid);
				Logg.i(TAG, "pw-"+pw);
//			    JSONObject jo = new JSONObject(map);
//				Gson gson = new Gson();
//				String json = gson.toJson(map);
//			    Log.i(TAG, "json-"+json);
//			    json.replace("\u003d", "=");
//			    Log.i(TAG, "json-after"+json);
//			    for (int i = 0; i < array.length; i++) {
//					
//				}
			    GsonBuilder gb =new GsonBuilder();
			    gb.disableHtmlEscaping();
			    String json = gb.create().toJson(map); 
			    Log.i(TAG, "json-"+json);//
			   return json.toString().getBytes();
				
			}
		};
		SMSApplication.getRequestQueue().add(request);
	}

}
