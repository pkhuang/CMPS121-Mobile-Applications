package com.pkhuang.asg3;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * A background service remembers the init_time when it was started, and listens to the accelerations.
 */
public class MotionDetectorService extends Service {

    private static final String LOG_TAG = "MotionDetectorService";

    // handle to notification manager
    private NotificationManager notificationManager;
    private int ONGOING_NOTIFICATION_ID = 1;

    // motion detector thread and runnable
    private Thread myThread;
    private MotionDetectorTask myTask;

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

        // start the task thread
        if (!myThread.isAlive()) {
            myTask = new MotionDetectorTask(getApplicationContext());
            myThread = new Thread(myTask);
            myThread.start();
        }

        // we want this service to continue running until it is explicitly stopped, so return sticky
        return START_STICKY;
    }

    /**
     * Call task method.
     */
    public boolean didItMove() {
        return myTask.didItMove();
    }

    @Override
    public void onDestroy() {
        // cancel the persistent notification
        notificationManager.cancel(ONGOING_NOTIFICATION_ID);
        Log.i(LOG_TAG, "Stopping.");
        // stop the motion detector
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
        myTask = new MotionDetectorTask(getApplicationContext());
        myThread = new Thread(myTask);
        myThread.start();
    }
}
