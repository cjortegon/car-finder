package com.mountainreacher.carfinder.geoservices;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class GPSTracker extends Service implements LocationListener {

	// Context
	private final Context mContext;

	// Flags
	private boolean isGPSEnabled = false;
	private boolean isNetworkEnabled = false;
	public boolean allowNetworkLocations = false;
	private boolean canGetLocation = false;

	private Location location; // location
	private double latitude; // latitude
	private double longitude; // longitude

	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 2 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 1; // 5 seconds

	// Declaring a Location Manager
	protected LocationManager locationManager;

	public GPSTracker(Context context) {
		this.mContext = context;
		getLocation();
	}

	public Location getLocation() {
		try {
			locationManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = allowNetworkLocations && locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
			} else {
				this.canGetLocation = true;
				// First get location from Network Provider
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS Enabled", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}
	@Override
	public void onLocationChanged(Location location) {
		this.latitude = location.getLatitude();
		this.longitude = location.getLongitude();
		//		Toast.makeText(mContext, "Location ("+location.getLatitude()+","+location.getLongitude()+")", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	/**
	 * Function to get latitude
	 * */
	public double getLatitude(){
		//		if(location != null) {
		//			latitude = location.getLatitude();
		//		}

		return latitude;
	}

	/**
	 * Function to get longitude
	 * */
	public double getLongitude(){
		//		if(location != null) {
		//			longitude = location.getLongitude();
		//		}

		return longitude;
	}

	/**
	 * Function to check the location service is available
	 * @return boolean
	 * */
	public boolean canGetLocation() {
		return this.canGetLocation;
	}

	/**
	 * Function to show settings alert dialog
	 * */
	public boolean showSettingsAlert(){

		if(!isGPSEnabled) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

			// Setting Dialog Title
			alertDialog.setTitle("GPS settings");

			// Setting Dialog Message
			alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

			// Setting Icon to Dialog
			//alertDialog.setIcon(R.drawable.delete);

			// On pressing Settings button
			alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int which) {
					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					mContext.startActivity(intent);
				}
			});

			// on pressing cancel button
			alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});

			// Showing Alert Message
			alertDialog.show();
			return true;
		}
		return false;
	}

	/**
	 * Stop using GPS listener
	 * Calling this function will stop using GPS in your app
	 * */
	public void stopUsingGPS() {
		if(locationManager != null) {
			locationManager.removeUpdates(GPSTracker.this);
		}
		this.canGetLocation = false;
	}

}
