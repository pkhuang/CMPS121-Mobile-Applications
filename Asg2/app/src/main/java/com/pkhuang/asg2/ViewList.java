package com.pkhuang.asg2;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;

import static com.pkhuang.asg2.ImageDB.TABLE_NAME;

/**
 * Displays the data from db in a GridView
 */
public class ViewList extends AppCompatActivity {

    ImageDB db;
    GridView gridView;
    ArrayList<DownloadedImage> list;
    ImageListAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);

        init();

        // get data from SQLite
        Cursor cursor = db.getData("SELECT * FROM " + TABLE_NAME);
        list.clear();
        while(cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            byte[] image = cursor.getBlob(2);

            list.add(new DownloadedImage(id, title, image));
        }
        adapter.notifyDataSetChanged(); // update grid
    }

    private void init() {
        db = ImageDB.getInstance(this);
        gridView = findViewById(R.id.grid_view);
        list = new ArrayList<>();
        adapter = new ImageListAdapter(this, R.layout.image_grid_view, list);
        gridView.setAdapter(adapter);
    }
}
