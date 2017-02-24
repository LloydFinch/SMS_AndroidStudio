package com.delta.smsandroidproject.util;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.util.Log;

import com.delta.smsandroidproject.bean.ChargerLocationData;
import com.delta.smsandroidproject.bean.LocationInfoData;
import com.google.android.gms.maps.model.LatLng;

public class GoogleUtil {
	/**
	 * 根据经纬度计算距离
	 * @param lat1
	 * @param lon1
	 * @param lat2
	 * @param lon2
	 * @return
	 */
	public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
	     float[] results=new float[1];
	     Location.distanceBetween(lat1, lon1, lat2, lon2, results);
	     return results[0];
	 }
	
	/**
	 * 对list按距离排序(从小到大)
	 * @param list
	 */
	public static List<ChargerLocationData> compare(List<ChargerLocationData> list){
		Collections.sort(list, new Comparator<ChargerLocationData>() {

			@Override
			public int compare(ChargerLocationData lhs, ChargerLocationData rhs) {
				return new Double(lhs.getDistance()).compareTo(new Double(rhs.getDistance()));
			}
		});
		return list;
	}
	
	/**
	 * 根据终点经纬度调起手机上的google地图，进行导航
	 * @param context
	 * @param lat
	 * @param lng
	 */
	public static void navigation(Context context ,String lat, String lng) {
		String address = "http://ditu.google.cn/maps?hl=zh&mrt=loc&q=" + lat
				+ "," + lng;
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
				Uri.parse(address));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				& Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		intent.setClassName("com.google.android.apps.maps",
				"com.google.android.maps.MapsActivity");
		context.startActivity(intent);
	}
	
	/**
	 * 根据始，终地址调起手机上的google地图，进行导航
	 * https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mou
	 * @param context
	 * @param address
	 */
	public static void navigationAddress(Context context ,String address1,String address){
//		String url = "http://ditu.google.cn/maps?f=d&source=s_d&saddr="+GetAddr(String.valueOf(ToolUtil.getCurLocLat()),String.valueOf(ToolUtil.getCurLocLon()))+"&daddr="+address;
		String url = "http://ditu.google.cn/maps?f=d&source=s_d&saddr="+address1+"&daddr="+address;
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
				Uri.parse(url));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				& Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		intent.setClassName("com.google.android.apps.maps",
				"com.google.android.maps.MapsActivity");
		context.startActivity(intent);
	}
	
	/**
	 * 根据始，终点经纬度调起手机上的google地图
	 * @param context
	 * @param lat
	 * @param lon
	 * @param endlat
	 * @param endlon
	 */
	public static void navigationAddress(Context context ,String lat,String lon,String endlat,String endlon){
		String url = "http://ditu.google.cn/maps?f=d&source=s_d&saddr="+lat+","+lon+"&daddr="+endlat+","+endlon+"&hl=zh";
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
				Uri.parse(url));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				& Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		intent.setClassName("com.google.android.apps.maps",
				"com.google.android.maps.MapsActivity");
		context.startActivity(intent);
		
		
//		Uri gmmIntentUri = Uri.parse("google.navigation:q="+endlat+","+endlon);
//		Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//		mapIntent.setPackage("com.google.android.apps.maps");
//		context.startActivity(mapIntent);
	}
	public static ChargerLocationData getTheClosestPoint(List<ChargerLocationData> mStationDatas, LatLng myLocation) {		
		if (mStationDatas!=null && mStationDatas.size()>0) {
			ChargerLocationData minStationData = mStationDatas.get(0);
			Double minLengh = getDistance(myLocation, getStationLatLng(minStationData));		
			
			for (ChargerLocationData mStationData : mStationDatas) {
				if(getDistance(myLocation, getStationLatLng(mStationData)) < minLengh){
					minLengh = getDistance(myLocation, getStationLatLng(mStationData));	
					minStationData = mStationData;
				}
			}
			Log.i("minLengh", minLengh + "km");
			return minStationData;
		}
		return null;
	}
	
	public static LatLng getStationLatLng(ChargerLocationData mStationData) {
		Double latitue = Double.valueOf(mStationData.getLat());
		Double longitude = Double.valueOf(mStationData.getLon());
		return new LatLng(latitue, longitude);
	}
	
	public static Double getDistance(LatLng start, LatLng end) {
		double lat1 = (Math.PI / 180) * start.latitude;
		double lat2 = (Math.PI / 180) * end.latitude;

		double lon1 = (Math.PI / 180) * start.longitude;
		double lon2 = (Math.PI / 180) * end.longitude;

		// 地球半径
		double R = 6371;
		// 两点间距离 km，如果想要米的话，结果*1000就可以了
		double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1)
				* Math.cos(lat2) * Math.cos(lon2 - lon1))
				* R;
//		Logg.i("getDistance", d + "km");
		return d * 1000;
//		return (double) Math.round(d*100/100.0);
	}
	
	public static Double getDistanceString(LatLng start, LatLng end) {
		if (start!=null && end!=null) {
			double lat1 = (Math.PI / 180) * start.latitude;
			double lat2 = (Math.PI / 180) * end.latitude;
			
			double lon1 = (Math.PI / 180) * start.longitude;
			double lon2 = (Math.PI / 180) * end.longitude;
			
			// 地球半径
			double R = 6371;
			// 两点间距离 km，如果想要米的话，结果*1000就可以了
			double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1)
					* Math.cos(lat2) * Math.cos(lon2 - lon1))
					* R;
//		Logg.i("getDistance", d + "km");
//		return d * 1000;
			BigDecimal fDecimal = new BigDecimal(d);
			return fDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
//			DecimalFormat format = new DecimalFormat("#.#");
//			return Double.valueOf(format.format(d));
//			return d;
		}
		return null;
	}
	
	public static String getGoogleMapUrl(ChargerLocationData data, LocationInfoData locInfoData){
		StringBuffer buffer = new StringBuffer();
		buffer.append("Information for charging location:"+data.getName());
		buffer.append("\n");
		buffer.append("Phone:"+locInfoData.getPhone());
		buffer.append("\n");
		buffer.append("Adress:"+getAddress(locInfoData));
		buffer.append("\n");
		buffer.append("Check it out on Google Map:http://maps.google.com?q=");
		buffer.append(data.getLat());
		buffer.append(",");
		StringBuffer stringBuffer = buffer.append(data.getLon());
		return stringBuffer.toString();
	}
	public static String getAddress(LocationInfoData data){
		if (data!=null) {
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(data.getAddress1()!=null?data.getAddress1():"");
			stringBuffer.append(data.getAddress2()!=null?data.getAddress2():"").append(",");
			stringBuffer.append(data.getZip()!=null?data.getZip():"").append(",");
			stringBuffer.append(data.getCity()!=null?data.getCity():"").append(",");
			stringBuffer.append(data.getState()!=null?data.getState():"").append(",");
			stringBuffer.append(data.getCountry()!=null?data.getCountry():"");
			return stringBuffer.toString();
		}
		return "";
		
	}
	
	/**
	 * 判断手机系统是否支持google地图
	 * @return
	 */
	public static boolean isSupportGoogMap(){
		try {
		    Class.forName("com.google.android.maps.MapActivity");
		    return true;
		} catch (Exception e) {
		    return false;
		}
	}
//	/** 
//	  * 根据经纬度反向解析地址，有时需要多尝试几次 
//	  * 注意:(摘自：http://code.google.com/intl/zh-CN/apis/maps/faq.html 
//	  * 提交的地址解析请求次数是否有限制？) 如果在 24 小时时段内收到来自一个 IP 地址超过 2500 个地址解析请求， 或从一个 IP 
//	  * 地址提交的地址解析请求速率过快，Google 地图 API 编码器将用 620 状态代码开始响应。 如果地址解析器的使用仍然过多，则从该 IP 
//	  * 地址对 Google 地图 API 地址解析器的访问可能被永久阻止。 
//	  *  
//	  * @param latitude 
//	  *            纬度 
//	  * @param longitude 
//	  *            经度 
//	  * @return 
//	  */  
//	 public static String GetAddr(String latitude, String longitude) {  
//	  String addr = "";  
//	  
//	  // 也可以是http://maps.google.cn/maps/geo?output=csv&key=abcdef&q=%s,%s，不过解析出来的是英文地址  
//	  // 密钥可以随便写一个key=abc  
//	  // output=csv,也可以是xml或json，不过使用csv返回的数据最简洁方便解析  
//	  String url = String.format(  
//	    "http://ditu.google.cn/maps/geo?output=csv&key=abcdef&q=%s,%s",  
//	    latitude, longitude);  
//	  URL myURL = null;  
//	  URLConnection httpsConn = null;  
//	  try {  
//	   myURL = new URL(url);  
//	  } catch (MalformedURLException e) {  
//	   e.printStackTrace();  
//	   return null;  
//	  }  
//	 try {  
//	   httpsConn = (URLConnection) myURL.openConnection();  
//	   if (httpsConn != null) {  
//	    InputStreamReader insr = new InputStreamReader(  
//	      httpsConn.getInputStream(), "UTF-8");  
//	    BufferedReader br = new BufferedReader(insr);  
//	    String data = null;  
//	    if ((data = br.readLine()) != null) {  
//	     System.out.println(data);  
//	     String[] retList = data.split(",");  
//	     if (retList.length > 2 && ("200".equals(retList[0]))) {  
//	      addr = retList[2];  
//	      addr = addr.replace("/", "");  
//   } else {  
//	      addr = "";  
//	     }  
//	    }  
//	    insr.close();  
//	   }  
//	  } catch (IOException e) {  
//	   e.printStackTrace();  
//	   return null;  
//	  }  
//	  return addr;  
//	}  
}
