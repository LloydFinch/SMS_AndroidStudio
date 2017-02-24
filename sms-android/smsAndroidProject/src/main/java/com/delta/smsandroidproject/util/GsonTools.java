package com.delta.smsandroidproject.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
/**
 * Gson解析json工具
 * @author Jianzao.Zhang
 *[{"",""}]用string.substring(,)-->{"",""}才能解析成功
 */
public class GsonTools {

	public GsonTools() {
	}
	
	public static String createGsonString(Object object) {
		Gson gson = new Gson();
		String gsonString = gson.toJson(object);
		return gsonString;
	}
	/**
	 * json -->对象
	 * @param gsonString
	 * @param cls
	 * @return
	 */
	public static <T> T changeGsonToBean(String gsonString, Class<T> cls) {
		Gson gson = new Gson();
		T t = gson.fromJson(gsonString, cls);
		return t;
	}
	/**
	 * json-->对象列表
	 * @param json
	 * @param cls
	 * @return
	 */
   public static <T> ArrayList<T> changeGsonToList(String json, Class<T> cls) {
        Type type = new TypeToken<ArrayList<JsonObject>>(){}.getType();
        ArrayList<JsonObject> jsonObjs = new Gson().fromJson(json, type);// 反序列化出ArrayList<JsonObject>，
        
        ArrayList<T> listOfT = new ArrayList<T>();
        for (JsonObject jsonObj : jsonObjs) {// 遍历添加到数组
            listOfT.add(new Gson().fromJson(jsonObj, cls));
        }
        
        return listOfT;
    }
   /**
    * 没测试成功
    * @param gsonString
    * @return
    */
	public static <T> List<Map<String, T>> changeGsonToListMaps(
			String gsonString) {
		List<Map<String, T>> list = null;
		Gson gson = new Gson();
		list = gson.fromJson(gsonString, new TypeToken<List<Map<String, T>>>() {
		}.getType());
		return list;
	}
	/**
    * 没测试成功
    * @param gsonString
    * @return
    */
	public static <T> Map<String, T> changeGsonToMaps(String gsonString) {
		Map<String, T> map = null;
		Gson gson = new Gson();
		map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
		}.getType());
		return map;
	}

}
