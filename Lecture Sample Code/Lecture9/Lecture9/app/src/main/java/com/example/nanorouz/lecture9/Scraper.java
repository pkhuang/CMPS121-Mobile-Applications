package com.example.nanorouz.lecture9;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by nanorouz on 5/2/19.
 */

class Scraper extends AsyncTask<String, Void, String>{

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(500);
            con.setReadTimeout(500);
            con.setRequestMethod("GET");
            con.connect();

            InputStream is = con.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            while((line = bufferedReader.readLine())!=null){
                stringBuilder.append(line);
            }
            bufferedReader.close();
            is.close();
            return stringBuilder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        MainActivity.textView.setText(s);
    }
}
