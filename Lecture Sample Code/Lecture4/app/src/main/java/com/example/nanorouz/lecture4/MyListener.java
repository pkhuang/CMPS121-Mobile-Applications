package com.example.nanorouz.lecture4;

import android.util.Log;
import android.view.View;

/**
 * Created by nanorouz on 4/16/19.
 */

public class MyListener implements View.OnClickListener{
    @Override
    public void onClick(View v) {
        Log.d(MyListener.class.getSimpleName(), "Button 4 is pressed!");
    }
}
