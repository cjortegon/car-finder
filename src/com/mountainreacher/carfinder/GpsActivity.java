package com.mountainreacher.carfinder;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.mountainreacher.carfinder.geoservices.Compass;
import com.mountainreacher.carfinder.geoservices.GPSTracker;
import com.mountainreacher.carfinder.geoservices.Geometry;
import com.mountainreacher.carfinder.geoservices.OnCompassUpdateListener;

import visual.AnimationManager;
import visual.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import externalservices.AnalyticsApplication;

public class GpsActivity extends Activity implements OnClickListener, OnCompassUpdateListener, OnCheckedChangeListener {

	// State control
	private static final String AD_UNIT_ID_INTERSTITIAL = "ca-app-pub-0360613478597349/4032141927";
	private static final String APP_ID = "ca-app-pub-0360613478597349~2075364321";
	private static final String SCREEN_NAME = "Home";

	//	private static final String AD_UNIT_ID_BANNER = "ca-app-pub-0360613478597349/3552097529";
	//	private static final String AD_UNIT_ID_BANNER = "ca-app-pub-0360613478597349/7293618326";
	private static final String CAR_FINDER_VERSION = "1.4.1";
	private static final String PREVIOUS_LOCATION = "PREVIOUS_LOCATION";
	private static final String PREVIOUS_LATITUD = "PREVIOUS_LATITUD";
	private static final String PREVIOUS_LONGITUDE = "PREVIOUS_LONGITUDE";
	// Rating and ads control
	private static final String PROMOTE_CONFIG = "PROMOTE_CONFIG";
	private static final String OPEN_TIMES = "OPEN_TIMES";
	private static final String AD_COUNTER = "AD_COUNTER";
	private static final int RATE_OPEN_SUGGESTION = 1;
	private static final int SHOW_INTERSTITIAL_AD = 2;

	// GPS
	private GPSTracker gps;

	// Analytics
	//	private AnalyticsApplication analytics;

	// Visual elements
	private Compass compass;
	private TextView text;
	private AnimationManager animation;
	private InterstitialAd interstitial;
	private CheckBox useNetwork;
	private View getLocation;

	// Locations
	private double carLocation[];
	private double myLocation[];
	private float distanceToCar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps);
		init();
		suggestRateAndCreateInterstitialBanner();
		initBanner();

		// Analytics
		//		analytics = (AnalyticsApplication) getApplication();
		//		analytics.startAnalyticsService(SCREEN_NAME);
	}

	public void initBanner() {
		MobileAds.initialize(getApplicationContext(), APP_ID);
		AdView adView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_gps, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.help_item:
			Intent startActivity2 = new Intent(this, HelpActivity.class);
			startActivity(startActivity2);
			return true;

		case R.id.contact_item:
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("message/rfc822");
			i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"mountainreacher@gmail.com"});
			i.putExtra(Intent.EXTRA_SUBJECT, "Feedback - Car Finder");
			i.putExtra(Intent.EXTRA_TEXT, "The version I'm using is "+CAR_FINDER_VERSION+"\n\n");
			try {
				startActivity(Intent.createChooser(i, "Send mail"));
			} catch (android.content.ActivityNotFoundException ex) {
				Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
			}
			return true;

		case R.id.rate_item:
			rateApp();
			return true;

		case R.id.mountain_reacher_item:
			Intent mountainReacher = new Intent(Intent.ACTION_VIEW);
			mountainReacher.setData(Uri.parse("market://search?q=pub:Mountain+Reacher"));
			startActivity(mountainReacher);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void rateApp() {
		Intent link = new Intent(Intent.ACTION_VIEW);
		link.setData(Uri.parse("market://details?id=com.mountainreacher.carfinder"));
		startActivity(link);
	}

	private void init() {

		// Initialize locations and sensors
		carLocation = new double[2];
		myLocation = new double[2];

		// Start compass sensor
		compass = new Compass(this, (SensorManager) getSystemService(Context.SENSOR_SERVICE));

		// Start location service
		gps = new GPSTracker(this);

		// Reading previous car location
		readPreviousLocation();

		// Init for visual elements
		initializeVisualElements();
		updateVisualObjects();
	}

	// *************************************** Visual elements ****************************************

	private void initializeVisualElements() {

		// TextView
		text = (TextView) findViewById(R.id.textView);

		// getLocation button
		getLocation = findViewById(R.id.getLocationButton);
		getLocation.setOnClickListener(this);

		// SaveLocation button
		Button saveLocation = (Button) findViewById(R.id.saveLoctaionButton);
		saveLocation.setOnClickListener(this);

		// Use network locations
		useNetwork = (CheckBox) findViewById(R.id.checkboxNetworkLocations);
		useNetwork.setOnCheckedChangeListener(this);

		// Canvas
		startCanvas();
	}

	private void startCanvas() {
		FrameLayout frameLayout = (FrameLayout)findViewById(R.id.canvasframe);
		animation = new AnimationManager(getApplicationContext(), new Resources(getResources()), 50);
		frameLayout.addView(animation);
	}

	private void updateVisualObjects() {
		text.setText(getResources().getString(R.string.distance_to_car)+": "+(gps.canGetLocation() ? distanceToCar+"m" : getResources().getString(R.string.unknown))+
				"\n"+getResources().getString(R.string.my_location)+": "+(gps.canGetLocation() ? ("("+myLocation[0]+", "+myLocation[1]+")") : getResources().getString(R.string.turn_on_gps))+
				"\n"+getResources().getString(R.string.car_location)+": ("+carLocation[0]+", "+carLocation[1]+")");
	}

	// *************************************** Visual elements ****************************************

	// *************************************** Manage locations ***************************************

	private void getActualLocation() {
		myLocation[0] = gps.getLatitude();
		myLocation[1] = gps.getLongitude();
	}

	private void saveCarLocation() {

		// Aun no se ha ubicado el dispositivo
		if(myLocation[0] == 0 && myLocation[1] == 0) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle(getResources().getString(R.string.unknown));
			alert.setMessage(getResources().getString(R.string.location_not_available));
			alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					displayInterstitial();
				}
			});
			alert.show();
		} else {
			carLocation[0] = myLocation[0];
			carLocation[1] = myLocation[1];
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle(getResources().getString(R.string.location_saved));
			alert.setMessage("("+carLocation[0]+","+carLocation[1]+")");
			alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					displayInterstitial();
				}
			});
			alert.show();
		}
		//		displayInterstitial();
	}

	// *************************************** Manage locations ***************************************

	// ****************************************** Listeners *******************************************

	@Override
	public void onClick(View v) {
		if(gps.canGetLocation()){
			switch(v.getId()) {
			case R.id.getLocationButton:
				gps.getLocation();
				if(gps.showSettingsAlert())
					displayInterstitial();
				//				getActualLocation();
				break;
			case R.id.saveLoctaionButton:
				saveCarLocation();
				break;
			}
		}else{
			gps.showSettingsAlert();
			displayInterstitial();
		}
		updateVisualObjects();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		gps.allowNetworkLocations = isChecked;
		getLocation.setVisibility(isChecked ? View.GONE : View.VISIBLE);
		gps.getLocation();
	}

	@Override
	public void onCompassUpdate(float pointingValue) {
		this.animation.setCompass(pointingValue);
		this.animation.setGpsState(!(myLocation[0] == 0 && myLocation[1] == 0));
		getActualLocation();
		this.animation.setCarPointing(pointingValue - (float)Geometry.getAngleOfLineBetweenTwoPoints(carLocation[0], carLocation[1], myLocation[0], myLocation[1]));
		this.distanceToCar = (float) Geometry.distanceBetweenPlaces(myLocation[0], myLocation[1], carLocation[0], carLocation[1]);
		this.animation.setDistance(distanceToCar);
		updateVisualObjects();
	}

	// ****************************************** Listeners *******************************************


	// ***************************************** Persistence ******************************************

	public void readPreviousLocation() {
		SharedPreferences settings = getSharedPreferences(PREVIOUS_LOCATION, 0);
		this.carLocation[0] = Double.valueOf(settings.getString(PREVIOUS_LATITUD, "0"));
		this.carLocation[1] = Double.valueOf(settings.getString(PREVIOUS_LONGITUDE, "0"));
	}

	public void savePreviousLocation() {
		SharedPreferences settings = getSharedPreferences(PREVIOUS_LOCATION, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(PREVIOUS_LATITUD, carLocation[0] + "");
		editor.putString(PREVIOUS_LONGITUDE, carLocation[1] + "");
		// Commit the edits!
		editor.commit();
	}

	public void suggestRateAndCreateInterstitialBanner() {

		//		// Read previous state
		//		SharedPreferences settings = getSharedPreferences(PROMOTE_CONFIG, 0);
		//		int openTimes =  Integer.valueOf(settings.getString(OPEN_TIMES, "0"));
		//		int adCounter =  Integer.valueOf(settings.getString(AD_COUNTER, "0"));
		//
		//		// Actions
		//		if(openTimes == RATE_OPEN_SUGGESTION) {
		//			AlertDialog.Builder alert = new AlertDialog.Builder(this);
		//			alert.setTitle(getResources().getString(R.string.would_you_rate));
		//			alert.setMessage(getResources().getString(R.string.rating_for_free));
		//			alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		//				public void onClick(DialogInterface dialog, int whichButton) {
		//					rateApp();
		//					SharedPreferences settings = getSharedPreferences(PROMOTE_CONFIG, 0);
		//					SharedPreferences.Editor editor = settings.edit();
		//					editor.putString(OPEN_TIMES, (RATE_OPEN_SUGGESTION+1) + "");
		//					editor.commit();
		//				}
		//			});
		//			alert.setNegativeButton(getResources().getString(R.string.remind_me_later), new DialogInterface.OnClickListener() {
		//				public void onClick(DialogInterface dialog, int whichButton) {}
		//			});
		//			alert.show();
		//		} else if(openTimes < RATE_OPEN_SUGGESTION)
		//			openTimes ++;
		//		if(adCounter < SHOW_INTERSTITIAL_AD)
		//			adCounter ++;
		//		else {

		// Create the interstitial
		interstitial = new InterstitialAd(this);
		interstitial.setAdUnitId(AD_UNIT_ID_INTERSTITIAL);

		// Create ad request
		AdRequest adRequest = new AdRequest.Builder().build();

		// Begin loading your interstitial
		interstitial.loadAd(adRequest);
		//		}

		//		// Save state
		//		SharedPreferences.Editor editor = settings.edit();
		//		editor.putString(OPEN_TIMES, openTimes + "");
		//		editor.putString(AD_COUNTER, adCounter + "");
		//		editor.commit();
	}

	public void displayInterstitial() {
		if (interstitial != null && interstitial.isLoaded()) {
			interstitial.show();
		}
	}

	// ***************************************** Persistence ******************************************

	// ************************************ Pause and resume App **************************************

	@Override
	protected void onResume() {
		super.onResume();
		compass.resume();
		animation.resume();
		gps.getLocation();
	}

	@Override
	protected void onPause() {
		compass.pause();
		animation.pause();
		gps.stopUsingGPS();
		super.onPause();
	}

	@Override
	protected void onStop() {
		gps.stopUsingGPS();
		savePreviousLocation();
		super.onStop();
	}

	// ************************************ Pause and resume App **************************************
}
