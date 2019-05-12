package com.pkhuang.asg2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A custom adapter for a grid. Grid is a bit wonky with my S7, test on emulator for other phones
 */
public class ImageListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<DownloadedImage> imageList;

    public ImageListAdapter(Context context, int layout, ArrayList<DownloadedImage> imageList) {
        this.context = context;
        this.layout = layout;
        this.imageList = imageList;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView downloads;
        TextView text_title;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if(row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.downloads = row.findViewById(R.id.img_downloaded);
            holder.text_title = row.findViewById(R.id.text_title);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        DownloadedImage img = imageList.get(position);

        byte[] downloaded_img = img.getImage();
        holder.text_title.setText(img.getTitle());

        Bitmap bitmap = BitmapFactory.decodeByteArray(downloaded_img, 0, downloaded_img.length);
        holder.downloads.setImageBitmap(bitmap);
        return row;
    }
}
