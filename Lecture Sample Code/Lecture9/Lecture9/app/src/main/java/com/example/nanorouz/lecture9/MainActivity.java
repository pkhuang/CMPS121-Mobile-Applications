package com.example.nanorouz.lecture9;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    static TextView textView;
    static ImageView imageView;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
    }

    public void count(View view) {
        Counter counter = new Counter(MainActivity.this);
        counter.execute();
    }
    //bit.ly/interimpresentation121
    public void downloadImage(View view) {
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            ImageDownloader imageDownloader = new ImageDownloader();
            imageDownloader.execute("https://coarchitects.com/wp-content/uploads/2015/05/UCSC-Engineering-09.jpg");
        }
        else{
            Toast.makeText(MainActivity.this, "NO INTERNET CONNECTION", Toast.LENGTH_LONG).show();
        }
    }
    public void web(View view) {
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Scraper scraper = new Scraper();
            scraper.execute("http://google.com");
        }
        else{
            Toast.makeText(MainActivity.this, "NO INTERNET CONNECTION", Toast.LENGTH_LONG).show();
        }
    }
}
