package com.bravo.johny.instagramclone;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

public class PhotoAdapter extends ArrayAdapter<Bitmap> {

    private Context mContext;
    private int mLayoutResourceId;
    private List<Bitmap> data;

    LayoutInflater inflater;

    public PhotoAdapter(Context mContext, int resource, List<Bitmap> data) {
        super(mContext, resource, data);
        this.mContext = mContext;
        this.data = data;
        this.mLayoutResourceId = resource;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public List<Bitmap> getData() {
        return data;
    }

    /**
     * Updates grid data and refresh grid items.
     * @param data
     */
    public void setData(List<Bitmap> data) {

        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Bitmap getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        PhotoHolder photoHolder;

        if(view == null) {
            inflater = LayoutInflater.from(this.mContext);
            view = inflater.inflate(mLayoutResourceId, parent, false);
            photoHolder = new PhotoHolder();
            photoHolder.imageView = view.findViewById(R.id.gridImageView);

            view.setTag(photoHolder);
        } else {
            photoHolder = (PhotoHolder) view.getTag();
        }

        Bitmap bitmap = data.get(position);
        photoHolder.imageView.setImageBitmap(bitmap);

        /*LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(250, 250);
        photoHolder.imageView.setLayoutParams(layoutParams);*/

        return view;
    }

    public class PhotoHolder {
        public ImageView imageView;

    }
}
