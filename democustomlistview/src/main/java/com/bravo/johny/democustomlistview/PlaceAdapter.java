package com.bravo.johny.democustomlistview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class PlaceAdapter extends ArrayAdapter<Place> implements View.OnClickListener{

    Context mContext;
    int mLayoutResourceId;
    Place[] mData = null;

    public PlaceAdapter(@NonNull Context context, int resource, @NonNull Place[] data) {
        super(context, resource, data);
        mContext = context;
        mLayoutResourceId = resource;
        mData = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        PlaceHolder placeHolder = null;

        // If we dont have a row view to reuse
        if(row == null) {

            // Create a row view
            LayoutInflater inflater = LayoutInflater.from(mContext);
            row = inflater.inflate(mLayoutResourceId, parent, false);

            placeHolder = new PlaceHolder();

            placeHolder.nameView = row.findViewById(R.id.nameTextView);
            placeHolder.zipView = row.findViewById(R.id.zipcodeTextView);
            placeHolder.imageView = row.findViewById(R.id.imageView);

            row.setTag(placeHolder);
        } else {
            // Otherwise use an existing one
            placeHolder = (PlaceHolder) row.getTag();
        }

        // Getting the data from data array

        Place place = mData[position];

        // setting the data into holder object

        placeHolder.imageView.setTag(position);
        placeHolder.imageView.setOnClickListener(this);

        placeHolder.nameView.setText(place.mNameOfPlace);
        placeHolder.zipView.setText(String.valueOf(place.mZipCode));

        int resId = mContext.getResources().getIdentifier(place.mNameOfImage, "drawable", mContext.getPackageName());
        placeHolder.imageView.setImageResource(resId);

        return row;
    }

    @Override
    public void onClick(View v) {
        Integer position = (Integer) v.getTag();
        Place p = mData[position];
        Toast.makeText(mContext, p.mPopup, Toast.LENGTH_SHORT).show();
    }


    private static class PlaceHolder {

        TextView nameView;
        TextView zipView;
        ImageView imageView;

    }

    /*public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        row = inflater.inflate(mLayoutResourceId, parent, false);

        TextView nameView = row.findViewById(R.id.nameTextView);
        TextView zipView = row.findViewById(R.id.zipcodeTextView);
        ImageView imageView = row.findViewById(R.id.imageView);

        Place place = mData[position];

        nameView.setText(place.mNameOfPlace);
        zipView.setText(String.valueOf(place.mZipCode));

        int resId = mContext.getResources().getIdentifier(place.mNameOfImage, "drawable", mContext.getPackageName());
        imageView.setImageResource(resId);

        return row;
    }*/
}
