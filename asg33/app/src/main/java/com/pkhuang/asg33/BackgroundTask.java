package com.pkhuang.asg33;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;

import java.time.Instant;

import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;


public class BackgroundTask implements Runnable, SensorEventListener{

    Instant first_accel_time;

    private Context context;
    private boolean running;

    Instant start;
    Duration duration30 = Duration.ofSeconds(30);

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private ResultCallback resultCallback;


    public Context getContext() {
        return context;
    }


    final Object m = new Object();

    public BackgroundTask(Context context){
        this.context = context;
        resultCallback = null;
        first_accel_time = null;
        start = Instant.now();
        running = true;
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        String startTime = formatter.format(start);
        Log.d("BackgroundTask", "Starting at: " + startTime);
    }

    DateTimeFormatter formatter = //helper function to format Instant object to string
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                    .withLocale(Locale.US)
                    .withZone(ZoneId.systemDefault());

    @Override
    public void run() {


        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        while (running) {
            if (didItMove()){

                Instant end = Instant.now();
                String endTime = formatter.format(end);
                Log.d("BackgroundTask", "Movement at: " + endTime);
                Duration timeElapsed = Duration.between(start, end);
                if (timeElapsed.compareTo(duration30) > 0) {
                    Log.d("BackgroundTask", "Movement was in " + timeElapsed);
                    notifyResultCallback(true);
                    running = false;
                    sensorManager.unregisterListener(this);
                }
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y));
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta;

        if (mAccel > 1){
            first_accel_time = Instant.now();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public boolean didItMove(){
        boolean moved = false;
        synchronized (m){
            if (first_accel_time != null && (Duration.between(start, first_accel_time).compareTo(duration30) > 0)){
                Log.i("BackgroundTask", "Movement at time" + first_accel_time);
                moved = true;
            }
        }
        return moved;
    }


    public void stopProcessing(){
        running = false;
    }

    private void notifyResultCallback(boolean b){
        ServiceResult result = new ServiceResult();
        if (result != null){
            result.boolValue = b;
            Log.i("BackgroundTask", "calling resultCallback for " + result.boolValue);
            //if (resultCallback != null) {

            resultCallback.onResultReady(result);
            //}
        }
    }

    public void updateResultCallback(ResultCallback result){
        Log.i("BackgroundTask", "Adding result callback");
        resultCallback = result;
    }


    public interface ResultCallback{
        void onResultReady(ServiceResult result);
    }
}