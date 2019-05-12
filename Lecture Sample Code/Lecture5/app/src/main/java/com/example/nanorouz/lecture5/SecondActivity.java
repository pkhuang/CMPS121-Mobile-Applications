package com.example.nanorouz.lecture5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        String first = b.get("first").toString();
        String last = b.get("last").toString();
        //String name = i.getStringExtra("first");
       // String last = i.getStringExtra("last");
        TextView textView = findViewById(R.id.textView);
        textView.setText("Hello " + first + " " + last);
    }

    public void goBack(View view) {
        Intent intent = new Intent();
        intent.putExtra("message", "We visited second page");
        setResult(RESULT_OK, intent);
        finish();
    }
}
