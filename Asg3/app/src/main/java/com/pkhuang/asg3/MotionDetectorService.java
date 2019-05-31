package com.pkhuang.asg3;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.service.carrier.CarrierMessagingService;
import android.util.Log;
import android.view.View;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A background service remembers the init_time when it was started, and listens to the accelerations.
 */
public class MotionDetectorService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float mAccel; // acceleration not related to gravity
    private float mAccelCurrent; // current accel including gravity
    private float mAccelLast; // last accel including gravity

    private static final String LOG_TAG = "MotionDetectorService";

    // handle to notification manager
    private NotificationManager notificationManager;
    private int ONGOING_NOTIFICATION_ID = 1;

    // motion detector thread and runnable
    private Thread myThread;
    private MotionDetectorTask myTask;

    // vars for calculating time
    private long init_time; // start time of the app
    private long first_accel_time; // the time of the first acceleration event

    // Binder class
    public class MyBinder extends Binder {
        MotionDetectorService getService() {
            // returns the underlying service
            return MotionDetectorService.this;
        }
    }

    // Binder given to clients
    private final IBinder myBinder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(LOG_TAG, "Service is being bound");
        // returns the binder to this service
        return myBinder;
    }

    // empty constructor
    public MotionDetectorService() {
    }

    @Override
    public void onCreate() {
        Log.i(LOG_TAG, "Service is being created");
        // display a notification about starting service by placing icon in status bar
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        showMyNotification();

        init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOG_TAG, "Received start id " + startId + ": " + intent);

        // sensor shit, might go in task instead
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_UI, new Handler());

        // start the task thread
        if (!myThread.isAlive()) {
            myThread.start();
        }

        // we want this service to continue running until it is explicitly stopped, so return sticky
        return START_STICKY;
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

        // modify value for first_accel_time
        first_accel_time = System.currentTimeMillis();

        // check to see if phone was moved
        if (mAccel > 3 && didItMove() == true) {
            // change text to show motion is detected
            MainActivity.text_timer.setVisibility(View.INVISIBLE);
            MainActivity.text_state.setText(getResources().getString(R.string.state_triggered));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * Checks to see if phone position changed only after 30 seconds have passed
     */
    public boolean didItMove() {
        boolean moved = false;
        long time_elapsed = first_accel_time - init_time;
        if(first_accel_time != 0 && (time_elapsed > 30000)) { // remember to change to 30000
            moved = true;
        }
        return moved;
    }

    @Override
    public void onDestroy() {
        // cancel the persistent notification
        notificationManager.cancel(ONGOING_NOTIFICATION_ID);
        Log.i(LOG_TAG, "Stopping.");
        // Stops the motion detector.
        myTask.stopProcessing();
        Log.i(LOG_TAG, "Stopped.");
    }

    /**
     * Show a notification while this service is running.
     */
    private void showMyNotification() {
        int id = 234;
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle("Service Message")
                .setContentText("Motion Detection Running.")
                .setSmallIcon(R.drawable.ic_stat_name).setChannelId(CHANNEL_ID);

        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(id, builder.build());
    }

    public void updateResultCallback(MotionDetectorTask.ResultCallback resultCallback) {
        myTask.updateResultCallback(resultCallback);
    }

    private void init() {
        // Creates the thread running the service.
        myTask = new MotionDetectorTask(getApplicationContext());
        myThread = new Thread(myTask);
        myThread.start();

        // sets time vars
        init_time = System.currentTimeMillis();
        first_accel_time = 0;
    }
}
