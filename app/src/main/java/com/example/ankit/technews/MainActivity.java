package com.example.ankit.technews;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.lang.Integer.valueOf;

public class MainActivity extends AppCompatActivity {

    Map<Integer, String> articleTitles = new HashMap<Integer, String>();
    Map<Integer, String> articleURLs = new HashMap<Integer, String>();
    ArrayList<Integer> articleIds = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask task = new DownloadTask();

        try {

            String result = task.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty").get();

            JSONArray jsonArray = new JSONArray(result);

            for(int i=0; i<12; i++){

                String articleId = jsonArray.getString(i);

                DownloadTask getArticle = new DownloadTask();

                String articleInfo = getArticle.execute("https://hacker-news.firebaseio.com/v0/item/" + articleId + ".json?print=pretty").get();

                JSONObject jsonObject = new JSONObject(articleInfo);

                String articleTitle = jsonObject.getString("title");

                String articleURL = jsonObject.getString("url");

                articleIds.add(Integer.valueOf(articleId));
                articleTitles.put(Integer.valueOf(articleId),articleTitle);
                articleURLs.put(Integer.valueOf(articleId),articleURL);

            }

            Log.i("article titles", articleTitles.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {

            String result = "";

            URL url;

            HttpURLConnection urlConnection = null;

            try{

                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data != -1){

                    char current = (char) data;

                    result += current;

                    data = reader.read();

                }


            }catch (Exception e){

                e.printStackTrace();

            }

            return  result;
        }
    }
}
