package externalservices;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * This is a subclass of {@link Application} used to provide shared objects for this app, such as
 * the {@link Tracker}.
 */
public class AnalyticsApplication extends Application {

	private static final String TAG = "AnalyticsApplication";
	private static final String ANALYTICS_APP_ID = "UA-57422643-4";

	private Tracker tracker;

	/**
	 * Gets the default {@link Tracker} for this {@link Application}.
	 * @return tracker
	 */
	synchronized public Tracker getDefaultTracker() {
		if (tracker == null) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			// To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
			tracker = analytics.newTracker(ANALYTICS_APP_ID);
		}
		return tracker;
	}

	public void startAnalyticsService(String screen) {
		getDefaultTracker();
		tracker.setScreenName(screen);
		tracker.send(new HitBuilders.ScreenViewBuilder().build());
	}

	public void sendAnalyticsEvent(String category, String action) {
		Log.i(TAG, "category="+category+", action="+action);
		tracker.send(new HitBuilders.EventBuilder()
				.setCategory(category)
				.setAction(action)
				.build());
	}

	public void sendAnalyticsEvent(String category, String action, String label) {
		Log.i(TAG, "category="+category+", action="+action+", label="+label);
		tracker.send(new HitBuilders.EventBuilder()
				.setCategory(category)
				.setAction(action)
				.setLabel(label)
				.build());
	}

	public void sendAnalyticsEvent(String category, String action, long value) {
		Log.i(TAG, "category="+category+", action="+action+", value="+value);
		tracker.send(new HitBuilders.EventBuilder()
				.setCategory(category)
				.setAction(action)
				.setValue(value)
				.build());
	}

	public void sendAnalyticsEvent(String category, String action, String label, long value) {
		Log.i(TAG, "category="+category+", action="+action+", label="+label+", value="+value);
		tracker.send(new HitBuilders.EventBuilder()
				.setCategory(category)
				.setAction(action)
				.setLabel(label)
				.setValue(value)
				.build());
	}

	public void sendException(Exception e, String method, boolean fatal) {
		tracker.send(new HitBuilders.ExceptionBuilder()
				.setDescription(method + ": " + e.getMessage())
				.setFatal(fatal)
				.build());
	}

	public void sendException(String message, String method, boolean fatal) {
		tracker.send(new HitBuilders.ExceptionBuilder()
				.setDescription(method + ": " + message)
				.setFatal(fatal)
				.build());
	}

}
