package com.delta.smsandroidproject.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import android.content.Intent;
import android.content.IntentFilter;

import com.delta.smsandroidproject.receiver.ActionReceiver;

/**
 * @author Wenqi.Wang
 * 
 */
public final class WebsocketClient {

	public static WebsocketClient instance = new WebsocketClient();
	public static final String TAG = "MyWebsocketClient";
	public static final String WEBSOCKET_MESSAGE = "message";
	public static final int WEBSOCKET_CODE = 10086;

	private HandWebSocket webSocket;
	private WebsocketConfig config;
	private IntentFilter filter;
	private ActionReceiver receiver;

	public static WebsocketClient getInstance() {
		return instance;
	}

	private WebsocketClient() {
	}

	public WebsocketClient config(WebsocketConfig config) {
		this.config = config;

		// 注册监听广播
		receiver = new ActionReceiver();
		filter = new IntentFilter(ActionReceiver.ACTION);
		config.mContext.registerReceiver(receiver, filter);

		return this;
	}

	public void connect() throws URISyntaxException, InterruptedException {

		// HandWebSocket websocket = new HandWebSocket(new URI(config.url),
		// new Draft_17(), config.params, 5 * 1000);
		final URI serverUri = new URI(config.url);
		webSocket = new HandWebSocket(serverUri);

		new Thread() {
			public void run() {
				try {
					webSocket.connectBlocking();
					Logg.i("WebSocket", "connect!");
				} catch (InterruptedException e) {
					e.printStackTrace();
					Logg.i("WebSocket", "disconnect:" + e.getMessage());
				}
			};
		}.start();
	}

	public class HandWebSocket extends WebSocketClient {
		public HandWebSocket(URI serverUri) {
			super(serverUri);
		}

		public HandWebSocket(URI serverUri, Draft draft) {
			super(serverUri, draft);
		}

		public HandWebSocket(URI serverUri, Draft protocolDraft,
				Map<String, String> httpHeaders, int connectTimeout) {
			super(serverUri, protocolDraft, httpHeaders, connectTimeout);
		}

		@Override
		public void onOpen(ServerHandshake handShakeData) {

			webSocket.send(config.message == null ? "Test!" : config.message);
			Logg.i(TAG, "onOpen-status:" + handShakeData.getHttpStatus());
			Logg.i(TAG, "onOpen-msg:" + handShakeData.getHttpStatusMessage());
		}

		@Override
		public void onMessage(String message) {
			Logg.i(TAG, "onMessage:" + message);

			Intent intent = new Intent(ActionReceiver.ACTION);
			intent.putExtra(WEBSOCKET_MESSAGE, message);
			config.mContext.sendBroadcast(intent);
		}

		@Override
		public void onError(Exception e) {
			Logg.i(TAG, "onError:" + e.getMessage());
		}

		@Override
		public void onClose(int code, String reason, boolean remote) {
			Logg.i(TAG, "onClose:" + "code--" + code + ",reason--" + reason
					+ ",remote--" + remote);

			config.mContext.unregisterReceiver(receiver);
		}
	}
}
