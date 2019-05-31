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
import android.widget.Button;
import android.widget.TextView;

import com.pkhuang.asg3.MotionDetectorService.MyBinder;

public class MainActivity extends AppCompatActivity
        implements com.pkhuang.asg3.MotionDetectorTask.ResultCallback {

    public static final int DISPLAY_NUMBER = 10;
    private Handler mUiHandler;

    private static final String LOG_TAG = "MainActivity";

    // Service connection variables
    private boolean serviceBound;
    private MotionDetectorService myService;

    public static TextView text_timer;
    public static TextView text_state;
    private Button btn_clear;
    private Button btn_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // starts the service, so that the service will only stop when explicitly stopped
        Intent intent = new Intent(this, MotionDetectorService.class);
        startService(intent);
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

    /**
     * Resets service.
     * 1) Activity calls the service
     * 2) Service sets init_time to the current time and first_accel_time to null
     */
    void clickClear(View v) {
        myService.doSomething(5, "hello");
    }

    /**
     * Exits app.
     * 1) Activity unbinds and stop from process, same as onPause()
     * 2) Remove the notification too
     */
    void clickExit(View v) {
        if (serviceBound) {
            unbindService(serviceConnection);
            serviceBound = false;

            if (true) {
                Intent intent = new Intent(this, MotionDetectorService.class);
                stopService(intent);
            }
        }
        finish();
    }

    @Override
    protected void onPause() {
        if (serviceBound) {
            Log.i("MotionDetectorService", "Unbinding");
            unbindService(serviceConnection);
            serviceBound = false;
            // if we like, stops the service.
            if (true) {
                Log.i(LOG_TAG, "Stopping.");
                Intent intent = new Intent(this, MotionDetectorService.class);
                stopService(intent);
                Log.i(LOG_TAG, "Stopped.");
            }
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
                // Displays it
                if (result != null) {
                    Log.i(LOG_TAG, "Displaying: " + result.intValue);
                    text_timer.setText(Integer.toString(result.intValue));
                    // tell the worker that the bitmap is ready to be reused
                } else {
                    Log.e(LOG_TAG, "Error: received empty message!");
                }
            }
//            // change state text after timer hits 0
//            if (Integer.parseInt((String) text_timer.getText()) == 0)
//            {
//                text_timer.setVisibility(View.INVISIBLE);
//                text_state.setText(getResources().getString(R.string.state_active));
//            }

            return true;
        }
    }

    private void init() {
        mUiHandler = new Handler(getMainLooper(), new UiCallback());
        serviceBound = false;
        text_timer = findViewById(R.id.text_timer);
        text_state = findViewById(R.id.text_state);
        btn_clear = findViewById(R.id.btn_clear);
        btn_exit = findViewById(R.id.btn_exit);

        // prevents the screen from dimming and going to sleep
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    }
}
