package com.example.nanorouz.lecture9;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

/**
 * Created by nanorouz on 5/2/19.
 */

class Counter extends AsyncTask<Void, Integer, String>{
    Context context;
    ProgressDialog pd;
    String result;

    Counter(Context context){
        this.context  = context;
        result = "Success";
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setTitle("Please Wait");
        pd.setButton(ProgressDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd.dismiss();
                result = "Cancelled";
            }
        });
        pd.setMax(10);
        pd.show();
    }

    @Override
    protected String doInBackground(Void... voids) {
        for(int i = 1; i <= 10; i++){
            if(result.equals("Cancelled"))
                break;
            try {
                Thread.sleep(1000);
                publishProgress(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        pd.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        MainActivity.textView.setText(s);
        pd.dismiss();
    }
}
