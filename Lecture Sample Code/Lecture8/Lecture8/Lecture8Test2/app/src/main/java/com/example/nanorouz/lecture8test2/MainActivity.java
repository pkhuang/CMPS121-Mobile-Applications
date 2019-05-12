package com.example.nanorouz.lecture8test2;


import android.app.Activity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<String>{

    TextView textView;
    Button button;
    EditText editText;
    static int id = 1;
    static int time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView2);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time = Integer.valueOf(editText.getText().toString());
                Bundle bundle = new Bundle();
                bundle.putInt("id", MainActivity.id);
                bundle.putInt("time", MainActivity.time);
                getLoaderManager().initLoader(MainActivity.id, bundle, MainActivity.this).forceLoad();
            }
        });
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        MainActivity.id++;
        return new Counter(MainActivity.this, args);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        textView.setText(textView.getText().toString() + data);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
class Counter extends AsyncTaskLoader<String>{
    ProgressDialog pd;
    int id;
    int max;
    Context context;
    String response = "";

    public Counter(Context context, Bundle bundle) {
        super(context);
        this.context = context;
        id = bundle.getInt("id");
        max = bundle.getInt("time");
    }

    @Override
    public String loadInBackground() {
        response = "Success" + id;
        int i = 1;
        while(!response.contains("Cancelled") && i <= max){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            onProgressUpdate(i);
            i++;
        }
        //Log.d("response", response);
        pd.dismiss();
        return response;
    }

    private void onProgressUpdate(int i) {
        pd.setProgress((int)(i * 100.0/max));
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        pd = new ProgressDialog(context);
        pd.setTitle("Counting ...");
        pd.setMessage("Please wait ...");
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setButton(ProgressDialog.BUTTON_NEGATIVE, "cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd.dismiss();
                response = "Cancelled" + id;
            }
        });
        pd.show();
    }

}