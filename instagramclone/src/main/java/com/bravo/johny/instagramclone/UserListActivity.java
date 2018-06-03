package com.bravo.johny.instagramclone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserListActivity extends AppCompatActivity {

    ArrayList<User> userList = new ArrayList<>();
    UserAdapter userAdapter;
    Map<String, Bitmap> imageMap = new HashMap<>();

    ListView userListView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.share_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.share) {
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            else
                getPhoto();
        } else if(item.getItemId() == R.id.logout) {
            ParseUser.logOut();
            Intent intent = new Intent(UserListActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getPhoto() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        setTitle("User List");

        userListView = findViewById(R.id.userListView);

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), UserFeedActivity.class);
//                Intent intent = new Intent(getApplicationContext(), UserFeedGridActivity.class);
                intent.putExtra("username", userList.get(position).getName());
                startActivity(intent);
            }
        });

        userAdapter = new UserAdapter(this, R.layout.row, userList);

        // Get user profile pictures and store it in a map

        ParseQuery<ParseObject> imageQuery = new ParseQuery<>("Image");
        imageQuery.whereEqualTo("userPhoto", 1);

        try {
            List<ParseObject> parseImages = imageQuery.find();
            if (parseImages.size() > 0) {
                for (ParseObject parseImage : parseImages) {
                    String userName = parseImage.getString("username");
                    Log.i("friends", "this is from image class : "+userName);
                    Bitmap imageBitmap = getBitmapImage(parseImage);
                    imageBitmap = scale(imageBitmap, 300, true);
                    imageMap.put(userName, imageBitmap);
                }
            } else {
                Log.i("friends", "No images returned");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        userQuery.addAscendingOrder("username");

        try {
            List<ParseUser> parseUsers = userQuery.find();
            if(parseUsers.size() > 0) {
                for(ParseUser parseUser : parseUsers) {
                    String userName = parseUser.getUsername();
                    String city = parseUser.getString("city");
                    Bitmap image = null;
                    image = imageMap.get(userName);
                    User user = new User(userName, city, image);
                    userList.add(user);
                }
                userListView.setAdapter(userAdapter);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        userAdapter.notifyDataSetChanged();

        // ----------------------------------------------------------------------------------------

        /*arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, userList);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.addAscendingOrder("username");

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null) {
                    if(objects.size() > 0 ) {
                        for(ParseUser user : objects) {
                            userList.add(user.getUsername());
                        }
                        userListView.setAdapter(arrayAdapter);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });

        arrayAdapter.notifyDataSetChanged();*/

        // ----------------------------------------------------------------------------------------
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                Log.i("friends", "Photo Recieved");

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bytes = stream.toByteArray();
                ParseFile parseFile = new ParseFile("image.png", bytes);
                ParseObject object = new ParseObject("Image");
                object.put("image", parseFile);
                object.put("username", ParseUser.getCurrentUser().getUsername());
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null) {
                            Toast.makeText(UserListActivity.this, "Image has been shared !!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserListActivity.this, "Image could not be shared.. Please try again later ..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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

    public static Bitmap scale(Bitmap realImage, float maxImageSize,boolean filter) {
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        int avg = (width + height) / 2;

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, avg, avg, filter);
        return newBitmap;
    }
}
