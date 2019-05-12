package com.pkhuang.asg2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ViewRange extends AppCompatActivity {

    ImageDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_range);
        db = ImageDB.getInstance(this);
    }
}
