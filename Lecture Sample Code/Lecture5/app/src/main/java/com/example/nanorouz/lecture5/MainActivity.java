package com.example.nanorouz.lecture5;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //Setting request codes
    static int secondCode = 2;
    static int thirdCode = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Implicit intent to open a web page
    public void openWeb(View view) {
        Uri uri = Uri.parse("http://google.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }

    //Implicit intent to call a number
    public void call(View view) {
        Uri uri = Uri.parse("tel:1234567890");
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(intent);

    }

    public void goToSecond(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        String first = ((TextView)findViewById(R.id.edit1)).getText().toString();
        String last = ((TextView)findViewById(R.id.edit2)).getText().toString();
        //Sending data using a Bundle object
        Bundle b = new Bundle();
        b.putString("first", first);
        b.putString("last", last);

        //intent.putExtra("first", name);
        //intent.putExtra("last", last);

        intent.putExtras(b);

        //startActivity(intent);
        startActivityForResult(intent, MainActivity.secondCode);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Displaying request code - 2 or 3
        TextView t1 = findViewById(R.id.text1);
        t1.setText(String.valueOf(requestCode));

        //Displaying result code
        TextView t2 = findViewById(R.id.text2);
        t2.setText(String.valueOf(resultCode));

        TextView t3 = findViewById(R.id.text3);

        if(requestCode == 2) {
            t3.setText("View 2");
        } else{
            t3.setText("View 3");
        }
    }

    public void goToThird(View view) {
        Intent intent = new Intent(this, ThirdActivity.class);
        startActivityForResult(intent, MainActivity.thirdCode);
    }
}
