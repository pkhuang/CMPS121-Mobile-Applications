package com.pkhuang.asg2;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Displays the data from db in a GridView
 */
public class ViewList extends AppCompatActivity {

    ImageDB db = ImageDB.getInstance(this);
    GridView gridView;
    ArrayList<DownloadedImage> list;
    ImageListAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);

        gridView = findViewById(R.id.grid_view);
        list = new ArrayList<>();
        adapter = new ImageListAdapter(this, R.layout.image_grid_view, list);
        gridView.setAdapter(adapter);

        // get data from SQLite
        Cursor cursor = db.getData("SELECT * FROM DOWNLOADS"); // doesn't work if clicked without downloading an image first. works fine after that initial download
        list.clear();
        while(cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            byte[] image = cursor.getBlob(2);

            list.add(new DownloadedImage(id, title, image));
        }
        adapter.notifyDataSetChanged();
    }
}
