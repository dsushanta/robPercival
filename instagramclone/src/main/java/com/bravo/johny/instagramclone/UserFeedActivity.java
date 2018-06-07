package com.bravo.johny.instagramclone;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class UserFeedActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    ImageView imageView;

    String selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        linearLayout = findViewById(R.id.linearLayout);

        Intent intent = getIntent();
        selectedUser = intent.getStringExtra("username");
        setTitle(selectedUser+"'s Feed");

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Image");
        query.whereEqualTo("username", selectedUser);
        query.whereNotEqualTo("userPhoto",1);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
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
                                        imageView = new ImageView(getApplicationContext());
                                        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT
                                        ));
                                        imageView.setImageBitmap(bitmap);
                                        linearLayout.addView(imageView);
                                        imageView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
                                                ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
                                                byte[] byteArray = bStream.toByteArray();
                                                intent.putExtra("image", byteArray);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }
}
