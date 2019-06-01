package com.pkhuang.asg3;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.time.Instant;

import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;


/**
 * A task that determines if the phone has been moved or not.
 * Implements Runnable to run this on a background thread.
 */
public class MotionDetectorTask implements Runnable, SensorEventListener {

    public static final String LOG_TAG = "MotionDetectorTask";

    private boolean running;
    private Context context;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float mAccel; // acceleration not related to gravity
    private float mAccelCurrent; // current accel including gravity
    private float mAccelLast; // last accel including gravity

    Instant first_accel_time;
    Instant start_time;
    Duration duration = Duration.ofSeconds(30);

    ResultCallback resultCallback;

    public Context getContext() {
        return context;
    }

    final Object myLock = new Object();

    public MotionDetectorTask(Context context) {
        this.context = context;

        init();
    }

    // formats Instant objs to string
    DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
            .withLocale(Locale.US).withZone(ZoneId.systemDefault());

    /**
     * Ticks down the visual timer and detects motion.
     */
    @Override
    public void run() {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        running = true;
        int countdown = 30;
        while (running) {
            // sleep one second for the timer
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.getLocalizedMessage();
            }
            // count down from 30 seconds
            if (countdown > 0) {
                countdown--;
            }
            // sends it to the UI thread in MainActivity (if MainActivity is running).
            notifyResultCallback(countdown, false);

            if (didItMove()){
                Instant instant = Instant.now();
                String instant_string = formatter.format(instant);
                Log.d("BackgroundTask", "Movement at: " + instant_string);
                Duration timeElapsed = Duration.between(start_time, first_accel_time);
                if (timeElapsed.compareTo(duration) > 0) {
                    notifyResultCallback(countdown, true);
                    running = false;
                    mSensorManager.unregisterListener(this);
                }
            }
        }
    }

    /**
     * changes state text on acceleration of phone
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];

        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y));
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta; // perform low-cut filter

        // if over arbitrary accel, save first time of accel
        if (mAccel > 3) {
            first_accel_time = Instant.now();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public boolean didItMove(){
        boolean moved = false;
        synchronized (myLock){
            if (first_accel_time != null &&
                    (Duration.between(start_time, first_accel_time).compareTo(duration) > 0)){
                moved = true;
            }
        }
        return moved;
    }

    public void stopProcessing() {
        // no need to bother with a synchronized statement; booleans are atomically updated.
        running = false;
    }

    /**
     * Call this function to return the integer i and boolean j to the activity.
     */
    private void notifyResultCallback(int i, boolean b) {
        ServiceResult result = new ServiceResult();
        // if null result, there's no more space in the buffer, so drop the integer rather than
        // sending it back
        if (result != null) {
            result.intValue = i;
            result.boolValue = b;
            Log.i(LOG_TAG, "calling resultCallback for " + result.intValue +
                    " and " + result.boolValue);
            resultCallback.onResultReady(result);
        }
    }

    public void updateResultCallback(ResultCallback result) {
        Log.i(LOG_TAG, "Adding result callback");
        resultCallback = result;
    }

    public interface ResultCallback {
        void onResultReady(ServiceResult result);
    }

    private void init() {
        resultCallback = null;
        first_accel_time = null;
        start_time = Instant.now();
        running = true;
        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
}
