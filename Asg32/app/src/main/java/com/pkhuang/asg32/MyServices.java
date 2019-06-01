package com.pkhuang.asg32;

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
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.time.Instant;

import com.pkhuang.asg32.BackgroundTask.ResultCallback;

public class MyServices extends Service{
    private static final String LOG_TAG = "MyService";



    private Thread myThread;
    private BackgroundTask myTask;

    private NotificationManager notificationManager;
    private int ONGOING_NOTIFICATION_ID = 1;




    public class MyBinder extends Binder {
        MyServices getService(){
            return MyServices.this;
        }
    }

    private final IBinder myBinder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent){
        Log.i("MyServices", "Service is being bound");
        return myBinder;
    }

    public MyServices(){
    }

    @Override
    public void onCreate() {
        Log.i(LOG_TAG, "Service is being created");
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //showMyNotification();

        myTask = new BackgroundTask(getApplicationContext());
        myThread = new Thread(myTask);
        myThread.start();
        /*
        try{
            Thread.sleep(30000);
            Log.i(LOG_TAG, "30 seconds have passed.");
        } catch (Exception e){
            e.printStackTrace();
        }
        */
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOG_TAG, "Received start id " + startId + ": " + intent);
        if (!myThread.isAlive()){
            myTask = new BackgroundTask(getApplicationContext());
            myThread = new Thread(myTask);
            myThread.start();
            /*
            try{
                Thread.sleep(30000);
                Log.i(LOG_TAG, "30 seconds have passed.");
            } catch (Exception e){
                e.printStackTrace();
            }*/
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        notificationManager.cancel(ONGOING_NOTIFICATION_ID);
        Log.i(LOG_TAG, "Stopping.");
        myTask.stopProcessing();
        Log.i(LOG_TAG, "Stopped.");
    }

    public boolean didItMove(){
        return myTask.didItMove();
    }


    /*private void showMyNotification(){
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
                .setContentText("You've received new messages!");

        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(id, builder.build());
    }
    */

    public void updateResultCallback(BackgroundTask.ResultCallback resultCallback){
        myTask.updateResultCallback(resultCallback);
    }

}