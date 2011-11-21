/* Copyright (c) 2006-2011 Regents of the University of Minnesota.
   For licensing terms, see the file LICENSE.
 */

package com.citizensense.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.citizensense.android.conf.Constants;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

/**
 * Map activity
 * @author Phil Brown
 * @author Renji Yu
 */
public class Map extends MapActivity {
	
	/** campaigns showed on the map */
	private ArrayList<Campaign> campaigns;
	/** overlays for displaying point/circle */
	private List<Overlay> mapOverlays;

	/** Initialize the map */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		G.map = (MapView) findViewById(R.id.mapview);
		G.map.setBuiltInZoomControls(true);
		mapOverlays = G.map.getOverlays();
	}// onCreate

	@Override
	public void onResume() {
		super.onResume();
		PointOverlay pointOverlay;
		CircleOverlay circleOverlay;
		//When we open the map, clear the overlay first
		mapOverlays.clear();
		// update intent data
		campaigns = getIntent().getParcelableArrayListExtra(
				getString(R.string.campaigns));
		if (campaigns != null) {
			for (Campaign campaign : campaigns) {
				for (String loc : campaign.getLocations()) {
					GeoPoint point = getGeopoint(loc);
					if (point != null) {
						if (getLocType(loc) == Constants.EXACT_LOCATION) {
							pointOverlay = new PointOverlay(getGeopoint(loc));
							mapOverlays.add(pointOverlay);
						}
						circleOverlay = new CircleOverlay(getGeopoint(loc),
								getRadius(loc));
						mapOverlays.add(circleOverlay);
						G.map.getController().animateTo(getGeopoint(loc));
					} else {
						Toast.makeText(
								this,
								"Geocoder failed, can't show some campaigns, please try later.",
								Toast.LENGTH_LONG).show();
					}
				}
			}
			setZoomLevel();
		}
	}

	/** Required by MapActivity. Currently unused. */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}// isRouteDisplayed

	/** Get campaigns from the local Android database. */
	ArrayList<Campaign> getCampaigns() {
		ArrayList<Campaign> results = new ArrayList<Campaign>();
		for (String campaignID : G.user.getCampaignIDs()) {
			results.add((Campaign) G.db.getCampaign(campaignID));
		}
		return results;
	}// getCampaigns

	/** Set zoom level of the map. */
	// Should be updated.
	public void setZoomLevel() {
		G.map.getController().setZoom(12);
	}
	/**
	 * Get locations of the campaign from database by searching the campaign's
	 * id.
	 */
	public String[] getLocsByCampaignId(String id) {
		Campaign c = (Campaign) G.db.getCampaign(id);
		return c.getLocations();
	}// getLocsByCampaignId

	/**
	 * Get the type of the location: (longitude,latitude) is EXACT_LOCATION,
	 * (city, state) is NONEXACT_LOCATION.
	 */
	public static boolean getLocType(String loc) {
		Pattern pattern = Pattern.compile("\\d");
		Matcher matcher = pattern.matcher(loc);
		if (matcher.find() == true)
			return Constants.EXACT_LOCATION;
		return Constants.NONEXACT_LOCATION;
	}

	/**
	 * Get the radius of the circle centered at loc. If loc is EXACT_LOCATION,
	 * set the radius to 10 meters, otherwise, set the radius to 10000 meters.
	 * This function may be updated if we can get more geo information.
	 */
	public static float getRadius(String loc) {
		if (getLocType(loc) == Constants.EXACT_LOCATION) {
			return 10;
		} else {
			return 10000;
		}
	}

	public GeoPoint getGeopoint(String loc) {
		return getGeopoint(this, loc);
	}

	/** static function to be used for LocationService */
	public static GeoPoint getGeopoint(Context context, String loc) {
		if (getLocType(loc) == Constants.EXACT_LOCATION) {
			loc = loc.replaceAll(" ", "");
			String[] long_lat = loc.split(",");
			int longitude = (int) (Integer.parseInt(long_lat[0]) * 1E6);
			int latitude = (int) (Integer.parseInt(long_lat[1]) * 1E6);
			return new GeoPoint(latitude, longitude);
		} else if (getLocType(loc) == Constants.NONEXACT_LOCATION) {
			Geocoder mygeoCoder = new Geocoder(context, Locale.getDefault());
			List<Address> lstAddress = null;
			try {
				// getFromLocationName() will return a list of Address which
				// matches the location name(first parameter)
				lstAddress = mygeoCoder.getFromLocationName(loc, 1);
				if (lstAddress.isEmpty()) {
					Log.e("Map.getGeopoint", "Geocoder did not find any "
							+ "addresses. Returning null.");
					return null;
				} else {
					Address address = lstAddress.get(0);
					int latitude = (int) (address.getLatitude() * 1E6);
					int longitude = (int) (address.getLongitude() * 1E6);
					return new GeoPoint(latitude, longitude);
				}
			} catch (IOException e) {
				Log.e("Map.getGeopoint", "Geocoder failed. Returning null");
				return null;
			}
		} else {
			Log.e("Map.getGeopoint", "Invalid Location type. Returning null");
			return null;
		}
	}// getGeopoint

	/**
	 * This function calculate the corresponding amount of pixels for the radius
	 * in meters. Merely use metersToEquatorPixels() will not work. Google Maps
	 * uses a Mercator projection. This means that the further you get from the
	 * equator the more distorted the distances become. I got this function
	 * from: http://stackoverflow.com/questions/2077054/how-to-compute-a-radius
	 * -around-a-point-in-an-android-mapview
	 */
	public int getPixelsFromMeters(float meters, MapView map, double latitude) {
		double m = (map.getProjection().metersToEquatorPixels(meters) * (1 / Math
				.cos(Math.toRadians(latitude))));
		return (int) m;
	}// getPixelsFromMeters

	/**
	 * PointOverlay is an inner class for showing a location as a marker on the
	 * map.
	 */
	class PointOverlay extends Overlay {
		/** The GeoPoint to be displayed. */
		private GeoPoint geoPoint;

		/**
		 * FIXME add documentation
		 * 
		 * @param geoPoint
		 */
		public PointOverlay(GeoPoint geoPoint) {
			this.geoPoint = geoPoint;
		}// PointOverlay

		@Override
		public void draw(Canvas canvas, MapView mapV, boolean shadow) {
			if (shadow) {
				Projection projection = mapV.getProjection();
				Point pt = new Point();
				projection.toPixels(geoPoint, pt);
				Paint paint = new Paint();
				Bitmap markerBitmap = BitmapFactory.decodeResource(
						getApplicationContext().getResources(),
						R.drawable.marker);
				// change the coordinates of the marker to make sure the pin
				// is located at the GeoPoint.
				canvas.drawBitmap(markerBitmap, pt.x - markerBitmap.getWidth()
						/ 2, pt.y - markerBitmap.getHeight(), paint);
			}
		}// draw
	}// PointOverlay

	/**
	 * CircleOverlay is an inner class for drawing a circle on the map. Its
	 * constructor receives two parameters, the first parameter is a GeoPoint
	 * indicating the center of the circle, the second parameter is the radius
	 * of the circle.
	 */
	class CircleOverlay extends Overlay {

		/** The center GeoPoint of the circle. */
		private GeoPoint center;
		/** The radius of the circle. */
		private float radius;

		public CircleOverlay(GeoPoint center, float radius) {
			this.center = center;
			this.radius = radius; // radius received should be in meters
		}// CircleOverlay

		@Override
		public void draw(Canvas canvas, MapView mapV, boolean shadow) {
			if (shadow) {
				Projection projection = mapV.getProjection();
				Point pt = new Point();
				projection.toPixels(center, pt);

				float radiusInPixels = getPixelsFromMeters(radius, mapV,
						center.getLatitudeE6() / 1000000);
				Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
				// set color and style for the inner part of the circle
				int color = Color.argb(127, 255, 0, 255); // set color
				circlePaint.setColor(color);
				circlePaint.setStyle(Style.FILL_AND_STROKE);
				canvas.drawCircle((float) pt.x, (float) pt.y, radiusInPixels,
						circlePaint);
				// set color and style for the border
				circlePaint.setColor(0x99000000);
				circlePaint.setStyle(Style.STROKE);
				canvas.drawCircle((float) pt.x, (float) pt.y, radiusInPixels,
						circlePaint);
			}
		}// draw
	}// CircleOverlay
}// Map