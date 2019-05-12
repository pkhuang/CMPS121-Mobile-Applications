package com.example.nanorouz.lecture8test;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    TextView textView;
    Button button;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView2);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Counter c = new Counter();
                c.execute(Integer.parseInt(editText.getText().toString()));
            }
        });
    }
    class Counter extends AsyncTask<Integer, Integer, String>{
        int max;
        ProgressDialog pd;
        String response = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setTitle("Counting ...");
            pd.setMessage("Please Wait ...");
            pd.setButton(ProgressDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    pd.dismiss();
                    textView.setText("Cancelled");
                    response = "Cancelled";
                }
            });
            pd.show();
        }

        @Override
        protected String doInBackground(Integer... integers) {
            max = integers[0];
            for(int i = 1; i <= max; i++){
                if(response.equals("Cancelled"))
                    break;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);
                if(i == max)
                    return "success";
            }
            return "Cancelled";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pd.setProgress((int)(values[0] * 100.0/max));

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textView.setText(s);
            pd.dismiss();
        }
    }

}