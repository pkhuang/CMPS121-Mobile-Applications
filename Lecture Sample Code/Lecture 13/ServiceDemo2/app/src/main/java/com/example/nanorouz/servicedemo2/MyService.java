package com.example.nanorouz.servicedemo2;

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

import com.example.nanorouz.servicedemo2.MyServiceTask.ResultCallback;

public class MyService extends Service {

    private static final String LOG_TAG = "MyService";

    // Handle to notification manager.
    private NotificationManager notificationManager;
    private int ONGOING_NOTIFICATION_ID = 1; // This cannot be 0. So 1 is a good candidate.

    // Motion detector thread and runnable.
    private Thread myThread;
    private MyServiceTask myTask;

    // Binder class.
    public class MyBinder extends Binder {
        MyService getService() {
            // Returns the underlying service.
            return MyService.this;
        }
    }

    // Binder given to clients
    private final IBinder myBinder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(LOG_TAG, "Service is being bound");
        // Returns the binder to this service.
        return myBinder;
    }

    public MyService() {
    }

    @Override
    public void onCreate() {
        Log.i(LOG_TAG, "Service is being created");
        // Display a notification about us starting.  We put an icon in the status bar.
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        showMyNotification();

        // Creates the thread running the service.
        myTask = new MyServiceTask(getApplicationContext());
        myThread = new Thread(myTask);
        myThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(LOG_TAG, "Received start id " + startId + ": " + intent);
        // We start the task thread.
        if (!myThread.isAlive()) {
            myThread.start();
        }
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        notificationManager.cancel(ONGOING_NOTIFICATION_ID);
        Log.i(LOG_TAG, "Stopping.");
        // Stops the motion detector.
        myTask.stopProcessing();
        Log.i(LOG_TAG, "Stopped.");
    }

    /**
     * Method that pretends to do something.
     */
    public void doSomething (int i, String s) {
        // We don't actually do anything, we need to ask the service thread to do it
        // for us.
        myTask.doSomething(i, s);
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
                .setContentText("You've received new messages!")
                .setSmallIcon(R.drawable.ic_stat_name).setChannelId(CHANNEL_ID);

        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(id, builder.build());

    }

    public void updateResultCallback(ResultCallback resultCallback) {
        myTask.updateResultCallback(resultCallback);
    }

}