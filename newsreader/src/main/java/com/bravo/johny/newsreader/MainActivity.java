package com.bravo.johny.newsreader;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> contents = new ArrayList<>();

    ArrayAdapter arrayAdapter;

    SQLiteDatabase articleDatabase;
    SQLiteStatement statement;
    String queryCreateTable;


    private final String APIKEY = "4602628cdffb4c4c994c28673d22bebf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        queryCreateTable = "CREATE TABLE IF NOT EXISTS articles(id INTEGER PRIMARY KEY, title VARCHAR, content VARCHAR)";
        articleDatabase = this.openOrCreateDatabase("Articles", MODE_PRIVATE, null);
        articleDatabase.execSQL(queryCreateTable);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, titles) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                if(position %2 == 0) {
                    view.setBackgroundColor(Color.parseColor("#CCE1FD"));
                } else {
                    view.setBackgroundColor(Color.parseColor("#DEFDCC"));
                }
                return view;
            }
        };
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                intent.putExtra("content", contents.get(position));
                startActivity(intent);
            }
        });

        updateListView();

        DownloadTask task = new DownloadTask();
        String parentUrlString = "https://newsapi.org/v2/top-headlines?sources=espn-cric-info&apiKey="+APIKEY;
        task.execute(parentUrlString);
    }

    public void updateListView() {

        String querySelectAll = "SELECT * FROM articles";

        Cursor cursor = articleDatabase.rawQuery(querySelectAll, null);

        int titleIndex = cursor.getColumnIndex("title");
        int contentIndex = cursor.getColumnIndex("content");

        if(cursor.moveToFirst()) {
            titles.clear();
            contents.clear();

            do {
                titles.add(cursor.getString(titleIndex));
                contents.add(cursor.getString(contentIndex));
            } while(cursor.moveToNext());

            arrayAdapter.notifyDataSetChanged();
        }
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;

        private String download(String urlString) {

            String resultString = "";

            try {

                url = new URL(urlString);
                urlConnection = (HttpURLConnection)url.openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while(data != -1) {
                    char current = (char)data;
                    resultString += current;
                    data = reader.read();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return resultString;
        }

        @Override
        protected String doInBackground(String... strings) {

            result = download(strings[0]);

            String queryArticleInsert="";
            String queryArticleDelete="";

            try {
                JSONObject jsonDownload = new JSONObject(result);
                JSONArray articleArray = jsonDownload.getJSONArray("articles");
                int numberOfItems = articleArray.length();

                String articleTitle="", articleURLString="", articleContent="";

                queryArticleDelete = "DELETE FROM articles";

                articleDatabase.execSQL(queryArticleDelete);

                for(int i=0; i<numberOfItems; i++) {

                    JSONObject articleJSON = articleArray.getJSONObject(i);

                    if( !articleJSON.isNull("title") && ( !articleJSON.isNull("url"))) {
                        articleTitle = articleJSON.getString("title");
                        articleURLString = articleJSON.getString("url");
                        articleContent = download(articleURLString);
                    }

                    queryArticleInsert = "INSERT INTO articles (title,content) VALUES(? , ?)";
                    statement = articleDatabase.compileStatement(queryArticleInsert);

                    statement.bindString(1, articleTitle);
                    statement.bindString(2,articleContent);

                    statement.execute();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            updateListView();
        }
    }
}
