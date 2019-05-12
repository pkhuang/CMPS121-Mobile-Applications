package com.pkhuang.asg2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Download extends AppCompatActivity {

    ImageDB db = ImageDB.getInstance(this);
    EditText editURL;
    EditText editTitle;
    Button btnDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        init();

        // attach click listener to Download button
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // download image from url
                urlToBitmap inputtedURL = new urlToBitmap();
                String img_URL = editURL.getText().toString().trim();
                Bitmap img_bitmap;

                // read user input and insert
                try {
                    img_bitmap = inputtedURL.execute(img_URL).get(); // get bitmap from URL

                    db.insertData(
                            editTitle.getText().toString().trim(), // insert inputted title
                            bitmapToByte(img_bitmap) // insert converted image
                    );

                    // reset EditText fields
                    editURL.setText("");
                    editTitle.setText("");

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // return to MainActivity
                Intent intent = new Intent(Download.this, MainActivity.class);
                startActivity(intent);

                // toasts for success / error in downloading
                if (checkConnection())
                {
                    Toast.makeText(Download.this, "Download successful!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Download.this, "Download unsuccessful. \n" +
                            " Check network connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // returns image bitmap from URL
    public class urlToBitmap extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {

            try {
                URL img_URL = new URL(strings[0]);

                HttpURLConnection connection = (HttpURLConnection) img_URL.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    // convert bitmap to byte array for insertion to db
    public static byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // checks for connection to internet
    public boolean checkConnection() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void init() {
        editURL = findViewById(R.id.editTextURL);
        editTitle = findViewById(R.id.editTextTitle);
        btnDownload = findViewById(R.id.btn_download);
    }
}