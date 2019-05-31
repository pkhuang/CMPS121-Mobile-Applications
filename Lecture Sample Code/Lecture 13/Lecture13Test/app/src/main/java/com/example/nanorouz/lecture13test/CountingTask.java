package com.example.nanorouz.lecture13test;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by nanorouz on 5/23/19.
 */

public class CountingTask implements Runnable{
    private Context context;
    boolean alive;

    CountingTask(Context context){
        this.context = context;
    }
    @Override
    public void run() {
        alive = true;
        int i = 1;
        while (alive) {
            if(i == 6) {
                stopProcessing();
                break;
            }
            try {
                Thread.sleep(1000);
                Log.d("Counting Service", i + " ");
                i++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d("Counting Service", "Finished");
    }

    public void stopProcessing(){
        alive = false;
    }
}
