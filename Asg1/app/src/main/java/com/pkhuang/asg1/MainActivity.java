package com.pkhuang.asg1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_ACTIVITY2 = 100;
    public static final int REQUEST_CODE_ACTIVITY3 = 101;
    public static final int REQUEST_CODE_ACTIVITY4 = 102;
    public static final String HIDE_BUTTON = "hideButton";

    private Button button1;
    private Button button2;
    private Button button3;
    private Button exit_button;

    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        exit_button = findViewById(R.id.exit_button);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        exit_button.setOnClickListener(this);

        sh = getSharedPreferences("visibility", MODE_PRIVATE);
        int vis = sh.getInt("hidden", 0);
        button1.setVisibility(vis);
        button2.setVisibility(vis);
        button3.setVisibility(vis);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("hidden", button1.getVisibility());
        outState.putInt("hidden", button2.getVisibility());
        outState.putInt("hidden", button3.getVisibility());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey("hidden")) {
            button1.setVisibility(savedInstanceState.getInt("hidden"));
            button2.setVisibility(savedInstanceState.getInt("hidden"));
            button3.setVisibility(savedInstanceState.getInt("hidden"));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor edit = sh.edit();
        edit.putInt("hidden", button1.getVisibility());
        edit.putInt("hidden", button2.getVisibility());
        edit.putInt("hidden", button3.getVisibility());
        edit.apply();
    }

    // handles button clicks
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                startActivityForResult(new Intent(this, Activity2.class), REQUEST_CODE_ACTIVITY2);
                break;
            case R.id.button2:
                startActivityForResult(new Intent(this, Activity3.class), REQUEST_CODE_ACTIVITY3);
                break;
            case R.id.button3:
                startActivityForResult(new Intent(this, Activity4.class), REQUEST_CODE_ACTIVITY4);
                break;
            case R.id.exit_button:
                finish();
                System.exit(0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ACTIVITY2 && resultCode == RESULT_OK) {
            // check if passed true from Activity2 to hide button / only hide if view is visible
            if (data.hasExtra(HIDE_BUTTON) && data.getBooleanExtra(HIDE_BUTTON, false) && button1.getVisibility() != View.GONE) {
                button1.setVisibility(View.GONE);
            }
        }
        if (requestCode == REQUEST_CODE_ACTIVITY3 && resultCode == RESULT_OK) {
            if (data.hasExtra(HIDE_BUTTON) && data.getBooleanExtra(HIDE_BUTTON, false) && button2.getVisibility() != View.GONE) {
                button2.setVisibility(View.GONE);
            }
        }
        if (requestCode == REQUEST_CODE_ACTIVITY4 && resultCode == RESULT_OK) {
            if (data.hasExtra(HIDE_BUTTON) && data.getBooleanExtra(HIDE_BUTTON, false) && button3.getVisibility() != View.GONE) {
                button3.setVisibility(View.GONE);
            }
        }
    }
}
