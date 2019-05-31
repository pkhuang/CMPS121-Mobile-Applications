package com.example.nanorouz.lecture13test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void count(View view) {
        Intent i = new Intent(this, CountingService.class);
        startService(i);
    }

    public void stop(View view) {
        Intent i = new Intent(this, CountingService.class);
        stopService(i);
    }
}
