package com.delta.smsandroidproject.bean;

import java.io.Serializable;
import java.util.List;

import android.text.Html;
import android.text.Spanned;

import com.delta.smsandroidproject.util.Logg;

//parse the API GoogleDirection
public class GoogleDirectionModel {

	private String status;
	private List<GeocodedWaypoints> geocoded_waypoints;
	private List<Routes> routes;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<GeocodedWaypoints> getGeocoded_waypoints() {
		return geocoded_waypoints;
	}

	public void setGeocoded_waypoints(List<GeocodedWaypoints> geocoded_waypoints) {
		this.geocoded_waypoints = geocoded_waypoints;
	}

	public List<Routes> getRoutes() {
		return routes;
	}

	public void setRoutes(List<Routes> routes) {
		this.routes = routes;
	}

	public GoogleDirectionModel() {

	}

	public class GeocodedWaypoints implements Serializable {

		private static final long serialVersionUID = -2115395817666714926L;
		private String geocoder_status;
		private String place_id;
		private List<String> types;

		public String getGeocoder_status() {
			return geocoder_status;
		}

		public void setGeocoder_status(String geocoder_status) {
			this.geocoder_status = geocoder_status;
		}

		public String getPlace_id() {
			return place_id;
		}

		public void setPlace_id(String place_id) {
			this.place_id = place_id;
		}

		public List<String> getTypes() {
			return types;
		}

		public void setTypes(List<String> types) {
			this.types = types;
		}

	}

	public class Routes implements Serializable {

		private static final long serialVersionUID = -3962688639730226372L;
		private List<Legs> legs;
		private Fare fare;
		private String copyrights;
		private String[] warnings;
		private List<Integer> waypoint_order;

		public List<Legs> getLegs() {
			return legs;
		}

		public void setLegs(List<Legs> legs) {
			this.legs = legs;
		}

		public Fare getFare() {
			return fare;
		}

		public void setFare(Fare fare) {
			this.fare = fare;
		}

		public String getCopyrights() {
			return copyrights;
		}

		public void setCopyrights(String copyrights) {
			this.copyrights = copyrights;
		}

		public String[] getWarnings() {
			return warnings;
		}

		public void setWarnings(String[] warnings) {
			this.warnings = warnings;
		}

		public List<Integer> getWaypoint_order() {
			return waypoint_order;
		}

		public void setWaypoint_order(List<Integer> waypoint_order) {
			this.waypoint_order = waypoint_order;
		}

		public class Legs implements Serializable {

			private static final long serialVersionUID = 6144130625177057222L;
			private Distance distance;
			private Duration duration;
			private String start_address;
			private String end_address;
			private StartLocation start_location;
			private EndLocation end_location;
			private List<Steps> steps;

			public Distance getDistance() {
				return distance;
			}

			public void setDistance(Distance distance) {
				this.distance = distance;
			}

			public Duration getDuration() {
				return duration;
			}

			public void setDuration(Duration duration) {
				this.duration = duration;
			}

			public String getStart_address() {
				return start_address;
			}

			public void setStart_address(String start_address) {
				this.start_address = start_address;
			}

			public String getEnd_address() {
				return end_address;
			}

			public void setEnd_address(String end_address) {
				this.end_address = end_address;
			}

			public StartLocation getStart_location() {
				return start_location;
			}

			public void setStart_location(StartLocation start_location) {
				this.start_location = start_location;
			}

			public EndLocation getEnd_location() {
				return end_location;
			}

			public void setEnd_location(EndLocation end_location) {
				this.end_location = end_location;
			}

			public List<Steps> getSteps() {
				return steps;
			}

			public void setSteps(List<Steps> steps) {
				this.steps = steps;
			}

			public class Distance implements Serializable {

				private static final long serialVersionUID = 1594097823451439474L;
				private String text;
				private long value;

				public String getText() {
					return text;
				}

				public void setText(String text) {
					this.text = text;
				}

				public long getValue() {
					return value;
				}

				public void setValue(long value) {
					this.value = value;
				}

			}

			public class Duration implements Serializable {

				private static final long serialVersionUID = -7069165979308397216L;
				private String text;
				private long value;

				public String getText() {
					return text;
				}

				public void setText(String text) {
					this.text = text;
				}

				public long getValue() {
					return value;
				}

				public void setValue(long value) {
					this.value = value;
				}

			}

			public class StartLocation implements Serializable {

				private static final long serialVersionUID = -7074001759181907417L;
				private double lat;
				private double lng;

				public double getLat() {
					return lat;
				}

				public void setLat(double lat) {
					this.lat = lat;
				}

				public double getLng() {
					return lng;
				}

				public void setLng(double lng) {
					this.lng = lng;
				}

			}

			public class EndLocation implements Serializable {

				private static final long serialVersionUID = -5073801702978651182L;
				private double lat;
				private double lng;

				public double getLat() {
					return lat;
				}

				public void setLat(double lat) {
					this.lat = lat;
				}

				public double getLng() {
					return lng;
				}

				public void setLng(double lng) {
					this.lng = lng;
				}

			}

			public class Steps implements Serializable {
				private static final long serialVersionUID = -6514748627175592941L;
				private String travel_mode;
				private StartLocation start_location;
				private EndLocation end_location;
				private String html_instructions;
				private Distance distance;
				private Duration duration;

				public StartLocation getStart_location() {
					return start_location;
				}

				public void setStart_location(StartLocation start_location) {
					this.start_location = start_location;
				}

				public EndLocation getEnd_location() {
					return end_location;
				}

				public void setEnd_location(EndLocation end_location) {
					this.end_location = end_location;
				}

				public Spanned getHtml_instructions() {
					Logg.i("steps", html_instructions);
					return Html.fromHtml(html_instructions);
				}

				public void setHtml_instructions(String html_instructions) {
					this.html_instructions = html_instructions;
				}

				public String getTravel_mode() {
					return travel_mode;
				}

				public void setTravel_mode(String travel_mode) {
					this.travel_mode = travel_mode;
				}

				public Distance getDistance() {
					return distance;
				}

				public void setDistance(Distance distance) {
					this.distance = distance;
				}

				public Duration getDuration() {
					return duration;
				}

				public void setDuration(Duration duration) {
					this.duration = duration;
				}
			}
		}

		public class Fare implements Serializable {
			private static final long serialVersionUID = -8411055269714882777L;
			private String currency;
			private int value;
			private String text;

			public String getCurrency() {
				return currency;
			}

			public void setCurrency(String currency) {
				this.currency = currency;
			}

			public int getValue() {
				return value;
			}

			public void setValue(int value) {
				this.value = value;
			}

			public String getText() {
				return text;
			}

			public void setText(String text) {
				this.text = text;
			}
		}
	}
}
