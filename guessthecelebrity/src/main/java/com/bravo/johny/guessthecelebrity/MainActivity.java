package com.bravo.johny.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    List<String> celebURLS, celebNames;
    Random random;
    int chosenCelebrity;
    Bitmap celebImage;
    int locationOfCorrectAnswer=0;
    String[] answers = new String[4];

    ImageView celebImageView;
    Button button0, button1, button2, button3;
    TextView resultTextView;

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();

                while(data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {

            URL url;
            HttpURLConnection urlConnection;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void start(View view) {

    }

    public void chooseAnswer(View view) {

        if(view.getTag().toString().equalsIgnoreCase(Integer.toString(locationOfCorrectAnswer))) {
            resultTextView.setText("Correct");
            resultTextView.setBackgroundColor(Color.parseColor("#2aba1a"));
        } else {
            resultTextView.setText("Incorrect");
            resultTextView.setBackgroundColor(Color.parseColor("#fb570100"));
        }
        resultTextView.setVisibility(View.VISIBLE);
        createNewQuestion();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        celebImageView = findViewById(R.id.celebImageView);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        resultTextView = findViewById(R.id.resultTextView);

        celebURLS = new ArrayList<>();
        celebNames = new ArrayList<>();

        DownloadTask task = new DownloadTask();
        try {
            String result = task.execute("http://www.tv.com/shows/friends/cast/").get();
            String split1 = result.split("id=\"castListGoesHere\"")[1];
            String split2 = split1.split("Recurring Roles")[0];

            Pattern p = Pattern.compile("img src=\"(.*?)\"");
            Matcher m = p.matcher(split2);

            while(m.find())
                celebURLS.add(m.group(1));

            p = Pattern.compile("<div class=\"role\">(.*?)</div>");
            m = p.matcher(split2);

            while(m.find())
                celebNames.add(m.group(1));

            createNewQuestion();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createNewQuestion() {
        try {
            random = new Random();
            chosenCelebrity = random.nextInt(celebURLS.size());

            ImageDownloader imageTask = new ImageDownloader();

            celebImage = imageTask.execute(celebURLS.get(chosenCelebrity)).get();
            celebImageView.setImageBitmap(celebImage);

            locationOfCorrectAnswer = random.nextInt(4);

            int incorrectAnswerIndex;

            for(int i=0; i<4; i++) {
                if(i==locationOfCorrectAnswer)
                    answers[i] = celebNames.get(chosenCelebrity);
                else {
                    do {
                        incorrectAnswerIndex = random.nextInt(celebURLS.size());
                    }while (incorrectAnswerIndex == chosenCelebrity);
                    answers[i] = celebNames.get(incorrectAnswerIndex);
                }
            }

            button0.setText(answers[0]);
            button1.setText(answers[1]);
            button2.setText(answers[2]);
            button3.setText(answers[3]);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
