package com.pkhuang.asg3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.pkhuang.asg3.MotionDetectorService.MyBinder;

public class MainActivity extends AppCompatActivity
        implements com.pkhuang.asg3.MotionDetectorTask.ResultCallback {

    private static final String LOG_TAG = "MainActivity";

    public static final int DISPLAY_NUMBER = 10;
    private Handler mUiHandler;

    // Service connection variables
    private boolean serviceBound;
    private MotionDetectorService myService;

    // private if nothing else uses it outside
    private TextView text_timer;
    private TextView text_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    /**
     * Resets service on button click.
     * 1) Activity unbinds and stops the service.
     * 2) Activity starts and binds the service.
     * 3) Activity resets state text fields.
     */
    void clickClear(View v) {
        if (serviceBound) {
            // unbind and stop service
            unbindService(serviceConnection);
            serviceBound = false;
            stopService(new Intent(this, MotionDetectorService.class));

            // start new service and bind again
            startService(new Intent(this, MotionDetectorService.class));
            bindMyService();
            serviceBound = true;

            // reset the UI
            text_timer.setText(getResources().getString(R.string.start_time));
            text_timer.setVisibility(View.VISIBLE);
            text_state.setText(getResources().getString(R.string.state_arming));
        }
    }

    /**
     * Exits app on button click.
     * 1) Activity unbinds and stop the service
     * 2) Remove the notification
     */
    void clickExit(View v) {
        if (serviceBound) {
            unbindService(serviceConnection);
            serviceBound = false;
            stopService(new Intent(this, MotionDetectorService.class));
        }
        finish();
    }

    /**
     * Call service method.
     */
    public boolean didItMove(){
        return myService.didItMove();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // starts the service, so that the service will only stop when explicitly stopped
        startService(new Intent(this, MotionDetectorService.class));
        bindMyService();
    }

    private void bindMyService() {
        // binds to the service
        Log.i(LOG_TAG, "Starting the service");
        Intent intent = new Intent(this, MotionDetectorService.class);
        Log.i("LOG_TAG", "Trying to bind");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    // service connection code
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder serviceBinder) {
            // bound to the camera service
            MyBinder binder = (MyBinder) serviceBinder;
            myService = binder.getService();
            serviceBound = true;
            // connect the callbacks
            Log.i("MotionDetectorService", "Bound succeeded, adding the callback");
            myService.updateResultCallback(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            serviceBound = false;
        }
    };



    @Override
    protected void onPause() {
        if (serviceBound) {
            Log.i("MotionDetectorService", "Unbinding");
            unbindService(serviceConnection);
            serviceBound = false;
        }
        super.onPause();
    }

    /**
     * This function is called from the service thread. To process this, create a message for a
     * handler in the UI thread.
     */
    @Override
    public void onResultReady(ServiceResult result) {
        if (result != null) {
            Log.i(LOG_TAG, "Preparing a message for " + result.intValue);
        } else {
            Log.e(LOG_TAG, "Received an empty result!");
        }
        mUiHandler.obtainMessage(DISPLAY_NUMBER, result).sendToTarget();
    }

    /**
     * This Handler callback gets the message generated above. Used to display the integer on the
     * screen.
     */
    private class UiCallback implements Handler.Callback {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == DISPLAY_NUMBER) {

                // gets the result
                ServiceResult result = (ServiceResult) message.obj;

                // display the result in the timer text field
                if (result != null) {
                    Log.i(LOG_TAG, "Displaying: " + result.intValue);
                    text_timer.setText(Integer.toString(result.intValue));

                    // display the correct state text fields
                    if (result.boolValue) {
                        text_state.setText(getResources().getString(R.string.state_triggered));
                    }
                    else if (result.intValue != 0) {
                        text_state.setText(getResources().getString(R.string.state_arming));
                    }
                    else {
                        text_timer.setVisibility(View.INVISIBLE);
                        text_state.setText(getResources().getString(R.string.state_active));
                    }
                } else {
                    Log.e(LOG_TAG, "Error: received empty message!");
                }
            }
            return true;
        }
    }

    private void init() {
        myService = new MotionDetectorService();
        mUiHandler = new Handler(getMainLooper(), new UiCallback());
        serviceBound = false;
        text_timer = findViewById(R.id.text_timer);
        text_state = findViewById(R.id.text_state);

        // prevents the screen from dimming and going to sleep
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        // start and bind new service
        Intent intent = new Intent(MainActivity.this, MotionDetectorService.class);
        startService(intent);
        bindMyService();
    }
}
