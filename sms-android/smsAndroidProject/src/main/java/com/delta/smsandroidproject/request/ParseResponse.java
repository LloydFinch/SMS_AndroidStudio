/**
 * 
 */
package com.delta.smsandroidproject.request;

/**
 * @author Wenqi.Wang
 * 
 */
public class ParseResponse {
	public static String parseResponse(String response) {
		if (response != null) {
			return response.replace("window.sessionStorage.clear() ;", "")
					.replace("\r\n", "").replace("location.reload();", "");
		}
		return "";
	}
}
