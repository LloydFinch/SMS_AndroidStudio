/**
 * 
 */
package com.delta.smsandroidproject.util;

import java.util.Map;

import android.content.Context;

/**
 * @author Wenqi.Wang
 * 
 */
public class WebsocketConfig {

	public Context mContext;
	public String url;
	public Map<String, String> params;
	public String message;

	private WebsocketConfig() {
	}

	public static class Builder {

		private Context mContext;
		private String url;
		private Map<String, String> params;
		private String message;

		public Builder(Context context) {
			this.mContext = context;
		}

		public Builder setUrl(String url) {
			this.url = url;
			return this;
		}

		public Builder setParams(Map<String, String> params) {
			this.params = params;
			return this;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public WebsocketConfig build() {
			WebsocketConfig config = new WebsocketConfig();
			config.mContext = this.mContext;
			config.url = this.url;
			config.params = this.params;
			config.message = this.message;
			return config;
		}
	}
}
