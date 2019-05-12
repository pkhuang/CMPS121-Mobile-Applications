package com.pkhuang.asg2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // set request codes
    static int code_download = 2;
    static int code_delete = 3;
    static int code_list = 4;
    static int code_range = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_download = findViewById(R.id.btn_download);
        Button btn_delete = findViewById(R.id.btn_delete);
        Button btn_view = findViewById(R.id.btn_view);
        Button btn_range = findViewById(R.id.btn_range);

        btn_download.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_view.setOnClickListener(this);
        btn_range.setOnClickListener(this);
    }

    // handles button clicks
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download:
                startActivityForResult(new Intent(this, Download.class), code_download);
                break;
            case R.id.btn_delete:
                startActivityForResult(new Intent(this, Delete.class), code_delete);
                break;
            case R.id.btn_view:
                startActivityForResult(new Intent(this, ViewList.class), code_list);
                break;
            case R.id.btn_range:
                startActivityForResult(new Intent(this, ViewRange.class), code_range);
                break;
        }
    }
}
