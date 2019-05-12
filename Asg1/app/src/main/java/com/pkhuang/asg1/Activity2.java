package com.pkhuang.asg1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activity2 extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        Button OK_button1 = findViewById(R.id.OK_button1);
        Button delete_button1 = findViewById(R.id.delete_button1);

        OK_button1.setOnClickListener(this);
        delete_button1.setOnClickListener(this);
    }

    // handles button clicks
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // go back to main menu
            case R.id.OK_button1:
                Intent intent1 = new Intent(Activity2.this, MainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                break;
            // go back to MainActivity but also remove this hobby from the list
            case R.id.delete_button1:
                Intent intent2 = new Intent();
                intent2.putExtra(MainActivity.HIDE_BUTTON, true);
                setResult(RESULT_OK, intent2);
                finish();
        }
    }
}
