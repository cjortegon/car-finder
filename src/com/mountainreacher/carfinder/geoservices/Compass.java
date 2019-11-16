package com.mountainreacher.carfinder.geoservices;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Compass implements SensorEventListener {

	private static final float ALPHA = 0.75f;

	// Compass feed updates instance
	private OnCompassUpdateListener listener;

	// Sensor objects
	private SensorManager sensorManager;
	private Sensor sensorAccelerometer;
	private Sensor sensorMagneticField;

	// Sensor values
	private float[] accelerometerValues;
	private float[] magneticFieldValues;
	private float[] matrixR;
	private float[] matrixI;
	private float[] matrixValues;
	private float pointing;

	public Compass(OnCompassUpdateListener listener, SensorManager sensorManager) {

		this.listener = listener;
		this.sensorManager = sensorManager;

		accelerometerValues = new float[3];
		magneticFieldValues = new float[3];
		matrixR = new float[9];
		matrixI = new float[9];
		matrixValues = new float[3];

		sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		
		resume();
	}

	public void resume() {
		sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, sensorMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
	}

	public void pause() {
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub

		switch(event.sensor.getType()){
		case Sensor.TYPE_ACCELEROMETER:
			for(int i =0; i < 3; i++){
				accelerometerValues[i] = relax(accelerometerValues[i], event.values[i]);
			}
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			for(int i =0; i < 3; i++){
				magneticFieldValues[i] = relax(magneticFieldValues[i], event.values[i]);
			}
			break;
		}

		boolean success = SensorManager.getRotationMatrix(matrixR, matrixI, accelerometerValues, magneticFieldValues);

		if(success){
			SensorManager.getOrientation(matrixR, matrixValues);
			//			double azimuth = Math.toDegrees(matrixValues[0]);
			//			double pitch = Math.toDegrees(matrixValues[1]);
			//			double roll = Math.toDegrees(matrixValues[2]);
			pointing = matrixValues[0];
		}

		listener.onCompassUpdate(pointing);
	}

	private float relax(float original, float newValue) {
		return original*(1-ALPHA)+newValue*ALPHA;
	}

}
