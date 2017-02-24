package com.delta.common.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

/**
 * Gson to json tool
 * 
 * @author Jianzao.Zhang
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
	 * json
	 * 
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
	 * json
	 * 
	 * @param json
	 * @param cls
	 * @return
	 */
	public static <T> ArrayList<T> changeGsonToList(String json, Class<T> cls) {
		Type type = new TypeToken<ArrayList<JsonObject>>() {
		}.getType();
		ArrayList<JsonObject> jsonObjs = new Gson().fromJson(json, type);

		ArrayList<T> listOfT = new ArrayList<T>();
		for (JsonObject jsonObj : jsonObjs) {
			listOfT.add(new Gson().fromJson(jsonObj, cls));
		}

		return listOfT;
	}

	/**
	 * 
	 * 
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
	 * 
	 * 
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
