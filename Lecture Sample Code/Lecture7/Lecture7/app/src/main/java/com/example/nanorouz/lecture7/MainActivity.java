package com.example.nanorouz.lecture7;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    MyDB db;
    EditText editText1;
    EditText editText2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new MyDB(this, "NAME_DATABASE", null, 1);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
    }

    public void insert(View view) {
        String firstname = editText1.getText().toString();
        String lastname = editText2.getText().toString();
        db.insert(firstname, lastname);
    }

    public void view(View view) {
        db.view();
    }

    public void delete(View view) {
        db.delete(editText1.getText().toString(), editText2.getText().toString());
    }

    public void update(View view) {
        db.update(editText1.getText().toString(), editText2.getText().toString());
    }

    public void count(View view) {
        db.count();
    }

    public void erase(View view) {
        db.erase();
    }
}
