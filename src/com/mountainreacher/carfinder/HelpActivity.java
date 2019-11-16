package com.mountainreacher.carfinder;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import externalservices.AnalyticsApplication;

public class HelpActivity extends Activity {

	private static final String SCREEN_NAME = "Help";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		setTitle(getResources().getString(R.string.help));

		// Analytics
//		AnalyticsApplication analytics = (AnalyticsApplication) getApplication();
//		analytics.startAnalyticsService(SCREEN_NAME);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

}
