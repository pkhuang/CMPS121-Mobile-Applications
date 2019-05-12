package com.pkhuang.asg2;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import java.util.ArrayList;

import static com.pkhuang.asg2.ImageDB.TABLE_NAME;

/**
 * Displays a user-specified range of data from db in a GridView
 */
public class ViewRange extends AppCompatActivity {

    ImageDB db;
    EditText editMin;
    EditText editMax;
    Button btn_display;
    GridView gridView;
    ArrayList<DownloadedImage> list;
    ImageListAdapter adapter = null;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_range);

        init();

        btn_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get range of data from SQLite
                String id_min_string = editMin.getText().toString().trim();
                int id_min = Integer.parseInt(id_min_string);
                String id_max_string = editMax.getText().toString().trim();
                int id_max = Integer.parseInt(id_max_string);

                // move through specified range of data and fill GridView
                Cursor cursor = db.getData("SELECT * FROM " + TABLE_NAME + " WHERE _id BETWEEN " +
                        id_min + " AND " + id_max);

                list.clear();

                // set up a counter or something for the toast in case of out of range
                while(cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String title = cursor.getString(1);
                    byte[] image = cursor.getBlob(2);

                    list.add(new DownloadedImage(id, title, image));
                }

                // handle out of range
                adapter.notifyDataSetChanged(); // update grid
            }
        });
    }

    private void init() {
        db = ImageDB.getInstance(this);
        editMin = findViewById(R.id.editMin);
        editMax = findViewById(R.id.editMax);
        btn_display = findViewById(R.id.btn_display);
        gridView = findViewById(R.id.grid_view2);
        list = new ArrayList<>();
        adapter = new ImageListAdapter(this, R.layout.image_grid_view, list);
        gridView.setAdapter(adapter);
    }
}
