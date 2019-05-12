package com.example.nanorouz.lecture4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //LinearLayout reference
    LinearLayout layout;
    //4 buttons for adding different listeners
    Button button1, button2, button3, button4;
    //TextView references for displaying messages
    TextView textView1, textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Instantiating all views
        layout = new LinearLayout(this);

        button1 = new Button(this);
        button2 = new Button(this);
        button3 = new Button(this);
        button4 = new Button(this);

        textView1 = new TextView(this);
        textView2 = new TextView(this);

        //Setting view properties
        layout.setOrientation(LinearLayout.VERTICAL);
        button1.setText("Button 1");
        button2.setText("Button 2");
        button3.setText("RESET");
        button4.setText("Debug Message");
        textView1.setHint("Flag for button 1");
        textView2.setHint("Flag for button 2");
        textView1.setTextSize(24);
        textView2.setTextSize(24);

        //Adding all views to the layout object
        layout.addView(textView1);
        layout.addView(textView2);
        layout.addView(button1);
        layout.addView(button2);
        layout.addView(button3);
        layout.addView(button4);

        //Setting the ContentView to the layout object or activity_main.xml
        setContentView(layout);
        //setContentView(R.layout.activity_main);

        //Adding listener using anonymous innerclass
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView1.setText("Button 1 is pressed!");
            }
        });

        //Same as above
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.setText("Button 2 is pressed!");
            }
        });

        //Making the activity implement the listener interface and assigning activity to be the listener on the view
        button3.setOnClickListener(this);

        //Creating a separate concrete class that implements the listener interface and instantiating a listener object from this user-defined concrete class
        button4.setOnClickListener(new MyListener());



    }

    //Implemented method from the View.OnClickListener interface
    @Override
    public void onClick(View v) {
        textView1.setText("");
        textView2.setText("");
        textView1.setHint("Flag for button 1");
        textView2.setHint("Flag for button 2");
    }

    //Defining and assigning a method as the onClick property of the view. It has been set in the xml file.
    public void showMessage(View view) {

        Toast.makeText(MainActivity.this, "My Button is pressed", Toast.LENGTH_LONG).show();
    }
}
