package com.example.nanorouz.lecture6;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements View.OnKeyListener{

    EditText editText;
    TextView textView;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(MainActivity.class.getSimpleName(), "onCreate");
        editText = findViewById(R.id.editText);
        editText.setOnKeyListener(this);
        textView = findViewById(R.id.textView);
        sh = getSharedPreferences("myPrefs", MODE_PRIVATE);
        String s = sh.getString("name", "NO NAME!");
        editText.setText(s);
        textView.setText(s);
        /*
        if(savedInstanceState != null && savedInstanceState.containsKey("name")){
            editText.setText(savedInstanceState.get("name").toString());
            textView.setText(savedInstanceState.get("name").toString());
        }
        */
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name", editText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null && savedInstanceState.containsKey("name")){
            editText.setText(savedInstanceState.get("name").toString());
            textView.setText(savedInstanceState.get("name").toString());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(MainActivity.class.getSimpleName(), "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(MainActivity.class.getSimpleName(), "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(MainActivity.class.getSimpleName(), "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(MainActivity.class.getSimpleName(), "onPause");
        SharedPreferences.Editor edit = sh.edit();
        edit.putString("name", editText.getText().toString());
        edit.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(MainActivity.class.getSimpleName(), "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(MainActivity.class.getSimpleName(), "onDestroy");
    }

    public void go(View view) {
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
            String s = editText.getText().toString();
            textView.setText(s);
        }
        return true;
    }
}
