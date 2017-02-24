package com.delta.smsandroidproject.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class GogleMapRoutePlan implements LocationListener {
	private LocationManager locationManager;
	private Location myLocation;

	private static GogleMapRoutePlan instance;

	public static GogleMapRoutePlan getInstance(Context context) {
		if (instance == null) {
			if (context != null) {
				instance = new GogleMapRoutePlan(context);
			} else {
				throw new IllegalArgumentException("Context must be put");
			}
		}
		return instance;
	}

	private GogleMapRoutePlan(Context context) {

		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		myLocation = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, this);
	}

	// create URL for route
	public String getDirectionsUrl(LatLng origin, LatLng dest) {

		String str_origin = "origin=" + origin.latitude + ","
				+ origin.longitude;

		LatLng myLatLng = new LatLng(myLocation.getLatitude(),
				myLocation.getLongitude());

		str_origin = "origin=" + myLatLng.latitude + "," + myLatLng.longitude;
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
		String sensor = "sensor=false";
		String mode = "mode=driving";
		String waypointLatLng = "waypoints=" + "40.036675" + "," + "116.32885";
		String parameters = str_origin + "&" + str_dest + "&" + sensor + "&"
				+ mode + "&" + waypointLatLng;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + parameters;
		return url;
	}

	public String getDirectionsUrl(String origin, String dest) {
		// LatLng myLatLng = new LatLng(myLocation.getLatitude(),
		// myLocation.getLongitude());
		//
		// String str_origin = "origin=" + myLatLng.latitude + ","
		// + myLatLng.longitude;

		String str_origin = "origin=" + origin;

		String str_dest = "destination=" + dest;
		String sensor = "sensor=false";
		String mode = "mode=driving";
		String waypointLatLng = "waypoints=" + "40.036675" + "," + "116.32885";
		String parameters = str_origin + "&" + str_dest + "&" + sensor + "&"
				+ mode + "&" + waypointLatLng;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + parameters;

		return url;
	}

	// get route
	public List<List<HashMap<String, String>>> parse(JSONObject jObject) {
		List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
		JSONArray jRoutes = null;
		JSONArray jLegs = null;
		JSONArray jSteps = null;
		try {
			jRoutes = jObject.getJSONArray("routes");
			for (int i = 0; i < jRoutes.length(); i++) {
				jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
				List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();
				for (int j = 0; j < jLegs.length(); j++) {
					jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");
					for (int k = 0; k < jSteps.length(); k++) {
						String polyline = "";
						polyline = (String) ((JSONObject) ((JSONObject) jSteps
								.get(k)).get("polyline")).get("points");
						List<LatLng> list = getPoly(polyline);
						for (int l = 0; l < list.size(); l++) {
							HashMap<String, String> hm = new HashMap<String, String>();
							hm.put("lat",
									Double.toString(((LatLng) list.get(l)).latitude));
							hm.put("lng",
									Double.toString(((LatLng) list.get(l)).longitude));
							path.add(hm);
						}
					}
					routes.add(path);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}
		return routes;
	}

	// getPoly
	private List<LatLng> getPoly(String encoded) {

		List<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng p = new LatLng((((double) lat / 1E5)),
					(((double) lng / 1E5)));
			poly.add(p);
		}
		return poly;
	}

	public static PolylineOptions lineOptions;

	private void draw(List<List<HashMap<String, String>>> result) {

		ArrayList<LatLng> points = null;

		for (int i = 0; i < result.size(); i++) {
			points = new ArrayList<LatLng>();
			lineOptions = new PolylineOptions();
			List<HashMap<String, String>> path = result.get(i);
			for (int j = 0; j < path.size(); j++) {
				HashMap<String, String> point = path.get(j);
				double lat = Double.parseDouble(point.get("lat"));
				double lng = Double.parseDouble(point.get("lng"));
				LatLng position = new LatLng(lat, lng);
				points.add(position);
			}
			lineOptions.addAll(points);
			lineOptions.width(3);
			lineOptions.color(Color.BLUE);
		}
		for (LatLng l : points) {
			Log.i("LatLng", l.toString());
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		myLocation = location;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

}
