/**
 * 
 */
package com.delta.smsandroidproject.util;

import java.net.URISyntaxException;

import android.content.Context;

/**
 * @author Wenqi.Wang
 * 
 */
public final class WebSocketRegisterTool {

	public static void registerWebSocket(Context context, String url) {
		Logg.i("WebSocketRegisterTool", "url:" + url);
		WebsocketConfig config = new WebsocketConfig.Builder(context)
				.setUrl(url).setParams(null).setMessage("Test!").build();
		try {
			WebsocketClient.getInstance().config(config).connect();
		} catch (URISyntaxException | InterruptedException e) {
			e.printStackTrace();
			Logg.i("WebSocketRegisterTool", "onError:" + e.getMessage());
		}
		Logg.i("WebSocketRegisterTool", "url-end:" + url);
	}
}
