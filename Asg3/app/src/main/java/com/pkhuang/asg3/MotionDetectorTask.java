package com.pkhuang.asg3;

import android.content.Context;
import android.util.Log;
import android.view.View;

/**
 * A task that determines if the phone has been moved or not.
 * Implements Runnable to run this on a background thread.
 */
public class MotionDetectorTask implements Runnable {
    public static final String LOG_TAG = "MotionDetectorService";
    private boolean running;
    private Context context;

    ResultCallback resultCallback;

    final Object myLock = new Object();

    public MotionDetectorTask(Context context) {
        this.context = context;
        // Put here what to do at creation.
    }

    /**
     * Ticks down the visual timer.
     */
    @Override
    public void run() {
        running = true;
        int countdown = 30;
        while (running) {
            // sleep one second
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
            notifyResultCallback(countdown);

            // change state text and stop processing after timer hits 0
            if (countdown == 0)
            {
                MainActivity.text_timer.setVisibility(View.INVISIBLE);
                MainActivity.text_state.setText(context.getResources().getString(R.string.state_active));
                stopProcessing();
            }
        }
    }

    public void stopProcessing() {
        // no need to bother with a synchronized statement; booleans are atomically updated.
        running = false;
    }

    /**
     * Call this function to return the integer i to the activity.
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
