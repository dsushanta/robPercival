package com.bravo.johny.instagramclone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class UserPhotoActivity extends AppCompatActivity {

    ImageView imageView;
    Button browseButton;
    Button skipButton;

    ParseObject userPhotoObject;

    public void navigateToUserList(View view) {
        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
        startActivity(intent);
    }

    public Bitmap getBitmapImage(ParseObject object) {

        final Bitmap[] bitmap = {null};

        ParseFile imageFile = (ParseFile) object.get("image");
        /*imageFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if(e == null && data != null) {
                    bitmap[0] = BitmapFactory.decodeByteArray(data, 0, data.length);
                }
            }
        });*/
        try {
            byte[] data = imageFile.getData();
            bitmap[0] = BitmapFactory.decodeByteArray(data, 0, data.length);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return bitmap[0];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_photo);

        imageView = findViewById(R.id.imageView);
        browseButton = findViewById(R.id.browseButton);
        skipButton = findViewById(R.id.skipButton);

        userPhotoObject = getCurrentUserPhoto();

        if(userPhotoObject == null) {
            Log.i("friends","a");
            imageView.setImageResource(R.drawable.user);
        } else {
            Log.i("friends","b");
            Bitmap bitmapImage = getBitmapImage(userPhotoObject);
            imageView.setImageBitmap(bitmapImage);
        }

        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                else
                    getPhoto();
            }
        });
    }

    private void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getPhoto();
        }
    }

    public ParseObject getCurrentUserPhoto() {

        final ParseObject[] userPhotoObject = new ParseObject[1];

        final String[] currentUser = {ParseUser.getCurrentUser().getUsername()};

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Image");
        query.whereEqualTo("username", currentUser[0]);
        query.whereEqualTo("userPhoto", 1);

        Log.i("friends", currentUser[0]);

        try {
            userPhotoObject[0] = query.find().get(0);
        } catch (Exception e) {
            userPhotoObject[0] = null;
        }

        /*query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null) {
                    if(objects.size() > 0) {
                        Log.i("friends", "aaa");
                        userPhotoObject[0] = objects.get(0);
                    }
                } else {
                    Log.i("friends", e.getMessage());
                }
            }
        });*/

        return userPhotoObject[0];
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null ) {
            Uri selectedImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 20, outputStream);
                byte[] bytes = outputStream.toByteArray();
                ParseFile parseFile = new ParseFile("trim"+ParseUser.getCurrentUser().getUsername()+".PNG", bytes);

                if(userPhotoObject == null) {
                    ParseObject parseObject = new ParseObject("Image");
                    parseObject.put("image", parseFile);
                    parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                    parseObject.put("userPhoto", 1);

                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null)
                                Toast.makeText(UserPhotoActivity.this, "User Photo Selected", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(UserPhotoActivity.this, "User Photo Could not be selected.. Please try again !!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    userPhotoObject.put("image", parseFile);
                    userPhotoObject.saveInBackground();
                }

                skipButton.setText("Next");
                skipButton.setBackgroundColor(Color.parseColor("#6efc77"));
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
