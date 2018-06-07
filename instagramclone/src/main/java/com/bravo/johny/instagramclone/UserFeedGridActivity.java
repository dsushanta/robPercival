package com.bravo.johny.instagramclone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class UserFeedGridActivity extends AppCompatActivity {

    String selectedUser;
    ArrayList<Bitmap> photoList = new ArrayList<>();

    GridView photoGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed_grid);

        photoGridView = findViewById(R.id.photoGridView);

        PhotoAdapter photoAdapter = new PhotoAdapter(this, R.layout.grid_photo_icon, photoList);

        photoGridView.setAdapter(photoAdapter);

        Intent intent = getIntent();
        selectedUser = intent.getStringExtra("username");
        setTitle(selectedUser+"'s Feed");

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Image");
        query.whereEqualTo("username", selectedUser);
        query.whereNotEqualTo("userPhoto", 1);
        query.orderByDescending("createdAt");
        /*query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null) {
                    if(objects.size() > 0) {
                        for(ParseObject object : objects) {
                            ParseFile imageFile = (ParseFile) object.get("image");
                            imageFile.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if(e == null && data != null) {
                                        final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        photoList.add(bitmap);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });*/

        try {
            List<ParseObject> parseObjects = query.find();
            if(parseObjects.size() > 0) {
                for(ParseObject object : parseObjects) {
                    ParseFile imageFile = (ParseFile) object.get("image");
                    /*imageFile.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if(e == null && data != null) {
                                final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                photoList.add(bitmap);
                            }
                        }
                    });*/
                    byte[] data = imageFile.getData();
                    if(data != null) {
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        photoList.add(bitmap);
                    }
                }
                photoAdapter.notifyDataSetChanged();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        photoAdapter.notifyDataSetChanged();

        photoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
                ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                photoList.get(position).compress(Bitmap.CompressFormat.PNG, 100, bStream);
                byte[] byteArray = bStream.toByteArray();
                intent.putExtra("image", byteArray);
                startActivity(intent);
            }
        });
    }
}
