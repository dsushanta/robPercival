package com.bravo.johny.instagramclone;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

    private Context mContext;
    private int mLayoutResourceId;
    private List<User> data;

    public UserAdapter(@NonNull Context context, int resource, @NonNull List<User> objects) {
        super(context, resource, objects);
        mContext = context;
        mLayoutResourceId = resource;
        data = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        PlaceHolder holder;

        // If we dont have a row view to reuse
        if(row == null) {

            // Create a row view
            LayoutInflater inflater = LayoutInflater.from(mContext);
            row = inflater.inflate(mLayoutResourceId, parent, false);

            // Create a new holder object
            holder = new PlaceHolder();
            holder.nameView = row.findViewById(R.id.nameTextView);
            holder.cityView = row.findViewById(R.id.cityTextView);
            holder.imageView = row.findViewById(R.id.userImageView);

            row.setTag(holder);
        } else {
            // Use existing placeholder
            holder = (PlaceHolder) row.getTag();
        }

        User userData = data.get(position);

        // Setting data in Holder object
        holder.nameView.setText(userData.getName());
        holder.cityView.setText(userData.getCity());

        if(userData.getUserPhoto() == null) {
            holder.imageView.setImageResource(R.drawable.ic_face_black_24dp);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(250, 250);
            holder.imageView.setLayoutParams(layoutParams);
            Log.i("friends", "Not Present : "+userData.getName());
        }
        else {
            holder.imageView.setImageBitmap(userData.getUserPhoto());
            Log.i("friends", "Present : "+userData.getName());
        }
        return row;
    }

    public class PlaceHolder {
        public TextView nameView;
        public TextView cityView;
        public ImageView imageView;

    }
}
