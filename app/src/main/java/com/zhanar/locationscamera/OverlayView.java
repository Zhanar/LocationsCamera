package com.zhanar.locationscamera;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * Created by Жанар on 29.05.2016.
 */
public class OverlayView extends View implements SensorEventListener, LocationListener {

    public static final String DEBUG_TAG = "OverlayView Log";
    String accelData = "Accelerometer Data";
    String compassData = "Compass Data";
    String gyroData = "Gyro Data";
    SensorManager sensors;
    Sensor accelSensor;
    Sensor compassSensor;
    Sensor gyroSensor;
    Camera mCamera;

    private Location lastLocation = null;
    public LocationManager locationManager;
    float[] curBearingToMW;
    float cameraRotation[] = new float[9];
    // orientation vector
    float orientation[] = new float[3];

    float accelArray[] = new float[3];
    float gyroArray[] = new float[3];
    float compassArray[] = new float[3];

    boolean isAccelAvailable, isCompassAvailable, isGyroAvailable;

    List<CustomLocation> points;

    public OverlayView(Context context, Camera camera, List<CustomLocation> capitalCitiesLocs) {
        super(context);
        sensors = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelSensor = sensors.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        compassSensor = sensors.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        gyroSensor = sensors.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        isAccelAvailable = sensors.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
        isCompassAvailable = sensors.registerListener(this, compassSensor, SensorManager.SENSOR_DELAY_NORMAL);
        isGyroAvailable = sensors.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);

        String best = locationManager.getBestProvider(criteria, true);

        Log.v(DEBUG_TAG, "Best provider: " + best);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return ;
        }
        locationManager.requestLocationUpdates(best, 50, 0, this);

        mCamera = camera;
        points = capitalCitiesLocs;

        curBearingToMW = new float[capitalCitiesLocs.size()];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint contentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        contentPaint.setTextAlign(Paint.Align.RIGHT);
        contentPaint.setTextSize(21);
        contentPaint.setColor(Color.RED);
        canvas.drawText(accelData, canvas.getWidth() / 2, canvas.getHeight() / 4, contentPaint);
        canvas.drawText(compassData, canvas.getWidth() / 2, canvas.getHeight() / 2, contentPaint);
        canvas.drawText(gyroData, canvas.getWidth() / 2, (canvas.getHeight() * 3) / 4, contentPaint);

        int i = 0;
        for(CustomLocation curLoc:points) {
            Camera.Parameters params = mCamera.getParameters();
            float verticalFOV = params.getVerticalViewAngle();
            float horizontalFOV = params.getHorizontalViewAngle();

            canvas.rotate((float)(0.0f- Math.toDegrees(orientation[2])));
            // Translate, but normalize for the FOV of the camera -- basically, pixels per degree, times degrees == pixels
            float dx = (float) ( (canvas.getWidth()/ horizontalFOV) * (Math.toDegrees(orientation[0])-curBearingToMW[i]));
            float dy = (float) ( (canvas.getHeight()/ verticalFOV) * Math.toDegrees(orientation[1])) ;

            // wait to translate the dx so the horizon doesn't get pushed off
            canvas.translate(0.0f, 0.0f - dy);
            contentPaint.setColor(Color.RED);
            // make our line big enough to draw regardless of rotation and translation
            canvas.drawLine(0f - canvas.getHeight(), canvas.getHeight() / 2, canvas.getWidth() + canvas.getHeight(), canvas.getHeight() / 2, contentPaint);
            //canvas.drawLine();
            // now translate the dx
            canvas.translate(0.0f - dx, 0.0f);
            contentPaint.setColor(Color.GREEN);
            // draw our point -- we've rotated and translated this to the right spot already
            canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, 8.0f, contentPaint);
            contentPaint.setColor(Color.RED);
            canvas.drawText(String.format("%s", curLoc.toString()), canvas.getWidth() / 2, canvas.getHeight() / 2, contentPaint);
            i++;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        StringBuilder msg = new StringBuilder(event.sensor.getName()).append(" ");
        for(float value: event.values)
        {
            msg.append("[").append(value).append("]");
        }

        switch(event.sensor.getType())
        {
            case Sensor.TYPE_ACCELEROMETER:
                accelData = msg.toString();
                accelArray[0] = event.values[0];
                accelArray[1] = event.values[1];
                accelArray[2] = event.values[2];

                break;
            case Sensor.TYPE_GYROSCOPE:
                gyroData = msg.toString();
                gyroArray[0] = event.values[0];
                gyroArray[1] = event.values[1];
                gyroArray[2] = event.values[2];

                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                compassData = msg.toString();
                compassArray[0] = event.values[0];
                compassArray[1] = event.values[1];
                compassArray[2] = event.values[2];

                break;
        }

        this.invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;

        int i=0;
        for(CustomLocation l:points) {
            float br = lastLocation.bearingTo(l.location);
            curBearingToMW[i] = br;
            i++;
        }
        //lastLocation.bearingTo(mountWashington);

        // compute rotation matrix
        float rotation[] = new float[9];
        float identity[] = new float[9];
        boolean gotRotation = SensorManager.getRotationMatrix(rotation, identity, accelArray, compassArray);

        if (gotRotation) {
            // remap such that the camera is pointing straight down the Y axis
            SensorManager.remapCoordinateSystem(rotation, SensorManager.AXIS_X, SensorManager.AXIS_Z, cameraRotation);

            // orientation vector
            SensorManager.getOrientation(cameraRotation, orientation);
        }
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
