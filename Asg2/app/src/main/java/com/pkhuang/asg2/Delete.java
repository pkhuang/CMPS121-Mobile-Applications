package com.pkhuang.asg2;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Delete extends AppCompatActivity {

    ImageDB db;
    EditText deleteID;
    EditText deleteTitle;
    Button btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        db = ImageDB.getInstance(this);

        init();

        // attack click listener to OK button
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // read user input
                String id_string = deleteID.getText().toString().trim();
                String title_to_delete = deleteTitle.getText().toString().trim();

                int id_to_delete;
                if(!TextUtils.isEmpty(id_string)) {
                    id_to_delete = Integer.parseInt(id_string);
                    db.deleteDataById(id_to_delete);
                } else if(!TextUtils.isEmpty(title_to_delete)) {
                    db.deleteDataByTitle(title_to_delete);
                }

                // reset EditText fields
                deleteID.setText("");
                deleteTitle.setText("");

                // return to MainActivity
                Intent intent = new Intent(Delete.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }



    private void init() {
        deleteID = findViewById(R.id.deleteID);
        deleteTitle = findViewById(R.id.deleteTitle);
        btn_delete = findViewById(R.id.btn_delete);
    }
}
