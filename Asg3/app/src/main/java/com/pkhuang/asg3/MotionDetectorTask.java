package com.pkhuang.asg3;

import android.content.Context;
import android.util.Log;
import java.util.Date;
import java.util.Random;

/**
 * A task that determines if the phone has been moved or not.
 * Implements Runnable to run this on a background thread.
 */
public class MotionDetectorTask implements Runnable {
    public static final String LOG_TAG = "MotionDetectorService";
    private boolean running;
    private Context context;

    ResultCallback resultCallback;

    // vars for calculating time
//    private Date init_time; // start time of the app
//    private Date first_accel_time; // the time of the first acceleration event

    // Fake variables to pretend we do something.
    private String s = "";
    private int i = 0;

    final Object m = new Object();

    public MotionDetectorTask(Context context) {
        this.context = context;
        // Put here what to do at creation.
    }

    /**
     * Calculates change in time after motion is detected.
     * replace starter code
     */
    @Override
    public void run() {
        running = true;
        Random rand = new Random();
        while (running) {
            // Sleep a tiny bit.
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.getLocalizedMessage();
            }
            // Generate a random number.
            int r = rand.nextInt(100);
            // sends it to the UI thread in MainActivity (if MainActivity is running).
            Log.i(LOG_TAG, "Sending random number: " + r);
            notifyResultCallback(r);
            if (i > 0) {
                Log.d("Service Counting", i + " ");
                i--;
                if (i < 0) {
                    // Do I ever get here?
                }
            }

        }
    }

    public void stopProcessing() {
        // no need to bother with a synchronized statement; booleans are atomically updated.
        running = false;
    }

    /**
     *
     */
    public void doSomething(int ii, String ss) {
        // An integer can be always changed atomically.
        i = ii;
        // For a string, we need to synchronize.
        synchronized (m) {
            s = ss;
        }
    }

    /**
     * Call this function to return the integer i to the activity.
     * @param i
     */
    private void notifyResultCallback(int i) {
        ServiceResult result = new ServiceResult();
        // if null result, there's no more space in the buffer, so drop the integer rather than
        // sending it back
        if (result != null) {
            result.intValue = i;
            Log.i(LOG_TAG, "calling resultCallback for " + result.intValue);
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
}
