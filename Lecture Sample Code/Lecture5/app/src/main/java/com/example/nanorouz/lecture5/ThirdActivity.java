package com.example.nanorouz.lecture5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
    }

    public void goBack(View view) {
        Intent intent = new Intent();
        intent.putExtra("message", "We visited third page");
        setResult(RESULT_OK, intent);
        finish();
    }
}
