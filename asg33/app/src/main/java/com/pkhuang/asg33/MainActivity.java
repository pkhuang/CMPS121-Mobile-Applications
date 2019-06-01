package com.pkhuang.asg33;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pkhuang.asg33.MyServices.MyBinder;
import org.w3c.dom.Text;

import java.time.Instant;
import java.util.Locale;

public class  MainActivity extends AppCompatActivity
        implements com.pkhuang.asg33.BackgroundTask.ResultCallback{

    public static final int DISPLAY_NUMBER = 10;
    private Handler mUiHandler;
    private static final String LOG_TAG = "MainActivity";



    TextView textView;
    Button btnExit;
    Button btnClear;

    private boolean serviceBound;
    private MyServices myServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myServices = new MyServices();


        mUiHandler = new Handler(getMainLooper(), new UiCallback());
        serviceBound = false;


        btnExit = findViewById(R.id.btnExit);
        btnClear = findViewById(R.id.btnClear);
        textView = findViewById(R.id.textView);
        Intent intent = new Intent(MainActivity.this, MyServices.class);
        startService(intent);
        bindMyService();
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//reset first_accel_time and restart service
                //Intent i = new Intent(MainActivity.this, MyServices.class);
                //stopService(i);

                String empty = "";
                textView.setText(empty);
                unbindService(serviceConnection);
                Intent intent = new Intent(MainActivity.this, MyServices.class);
                stopService(intent);


                Intent intentt = new Intent(MainActivity.this, MyServices.class);
                startService(intentt);
                bindMyService();

                //myServices.onDestroy();
                //myServices.onDestroy();


                //bindMyService();
            }
        });
        //startService(new Intent(getBaseContext(), MyServices.class));

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //stop service when exit clicked

                Log.i(LOG_TAG, "Stopping.");
                Intent intent = new Intent(MainActivity.this, MyServices.class);
                stopService(intent);
                Log.i(LOG_TAG, "Stopped.");

                finish();
                //stopService(new Intent(getBaseContext(), MyServices.class));
            }
        });
    }

    public boolean didItMove(){
        return myServices.didItMove();
    }
    @Override
    protected void onResume() {
        super.onResume();

        Log.i("MainActivity", "onResume");

        Intent intent = new Intent(this, MyServices.class);
        startService(intent);
        bindMyService();
        String trueStr = "Phone has been moved!";
        String falseStr = "Everything was quiet.";
        /*if (didItMove()){
            textView.setText(trueStr);
        }
        else textView.setText(falseStr);
        */

    }

    private void bindMyService(){
        Log.i(LOG_TAG, "Starting the service");
        Intent intent = new Intent(this, MyServices.class);
        Log.i(LOG_TAG, "Trying to bind");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder serviceBinder) {
            MyBinder binder = (MyBinder) serviceBinder;
            myServices = binder.getService();
            serviceBound = true;

            Log.i("MyService", "Bound succeeded, adding the callback");
            myServices.updateResultCallback(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            serviceBound = false;
        }
    };


    @Override
    protected void onPause() {
        if (serviceBound){
            Log.i("MyServices", "Unbinding");
            unbindService(serviceConnection);
            serviceBound = false;
        }
        super.onPause();
    }

    @Override
    public void onResultReady(ServiceResult result){
        if (result != null){
            Log.i(LOG_TAG, "Preparing a message for " + result.boolValue);
        }
        else {
            Log.e(LOG_TAG, "Received an empty result!");
        }
        mUiHandler.obtainMessage(DISPLAY_NUMBER, result).sendToTarget();
    }

    private class UiCallback implements Handler.Callback{
        @Override
        public boolean handleMessage(Message message){
            if (message.what == DISPLAY_NUMBER){
                //Gets the result.
                ServiceResult result = (ServiceResult) message.obj;
                //Displays it.
                if (result != null){
                    Log.i(LOG_TAG, "Displaying: " + result.boolValue + " message");
                    TextView textView = (TextView) findViewById(R.id.textView);
                    String trueStr = "Phone has been moved!";
                    String falseStr = "Phone has not been moved.";
                    if (result.boolValue){

                        textView.setText(trueStr);
                    }
                    else textView.setText(falseStr);
                }
                else Log.e(LOG_TAG, "Error: received empty message!");
            }
            return true;
        }
    }
}