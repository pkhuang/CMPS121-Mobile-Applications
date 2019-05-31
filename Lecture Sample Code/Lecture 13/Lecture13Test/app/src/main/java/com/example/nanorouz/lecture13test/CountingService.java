package com.example.nanorouz.lecture13test;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class CountingService extends Service {
    private Thread myThread;
    private CountingTask myTask;


    public CountingService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Counting Service", "Created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myTask = new CountingTask(getApplicationContext());
        myThread = new Thread(myTask);
        myThread.start();
        if(!myThread.isAlive())
            myThread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myTask.stopProcessing();
        Log.d("Counting Service", "Destroyed");
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }
}
