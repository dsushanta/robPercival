package com.bravo.johny.practiceparsedserver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");
        query.whereContains("username", "R");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects != null && e == null) {
                    for (ParseObject o : objects) {
                        o.put("score", o.getInt("score") + 100);
                        o.saveInBackground();
                    }
                }
            }
        });






        /*String[] friends_chars = {"Chandler", "Monica", "Phoebe", "Joey", "Rachel", "Ross"};

        Random random = new Random();

        for(int i=0; i<6; i++) {
            ParseObject score = new ParseObject("Score");

            score.put("username",friends_chars[i]);
            score.put("score", random.nextInt(80));

            score.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null) {
                        Log.i("friends", "Successfully saved");
                    } else {
                        Log.i("friends", "problem in saving");
                    }
                }
            });
        }*/





        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}
