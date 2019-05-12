package com.pkhuang.asg2;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Delete extends AppCompatActivity {

    ImageDB db = ImageDB.getInstance(this);
    EditText deleteID;
    EditText deleteTitle;
    Button btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        init();

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // read user input
                String id_string = deleteID.getText().toString().trim();
                int id_to_delete = Integer.parseInt(id_string);
                String title_to_delete = deleteTitle.getText().toString().trim();

                // check if data exists. if so, delete. this is where you left off
//                Cursor cursor = db.getData("SELECT id, title FROM DOWNLOADS");
//                while(cursor.moveToNext()) {
//                    int id = cursor.getInt(0);
//                    String title = cursor.getString(1);

                    db.deleteData(id_to_delete, title_to_delete);

//                }

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
