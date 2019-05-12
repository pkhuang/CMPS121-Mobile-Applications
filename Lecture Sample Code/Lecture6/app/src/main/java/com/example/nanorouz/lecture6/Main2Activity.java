package com.example.nanorouz.lecture6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Log.d(Main2Activity.class.getSimpleName(), "onCreate");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(Main2Activity.class.getSimpleName(), "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(Main2Activity.class.getSimpleName(), "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Main2Activity.class.getSimpleName(), "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(Main2Activity.class.getSimpleName(), "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(Main2Activity.class.getSimpleName(), "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(Main2Activity.class.getSimpleName(), "onDestroy");
    }
}
