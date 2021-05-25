package com.example.newsshare;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NewsAdapter.ItemClicker {

        public class BG extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection conn;
            try {
                url = new URL(urls[0]);
                conn = (HttpURLConnection) url.openConnection();
                InputStream in = conn.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
            }
            catch (Exception e){
                e.printStackTrace();
                return e.toString();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("apidata",s);
        }
    }
    List<Model> newsList;
    NewsAdapter adapter;
    RecyclerView news;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BG bg = new BG();
        bg.execute("https://jsonplaceholder.typicode.com/todos/");
        news = findViewById(R.id.news);
        newsList = new ArrayList<>();
        loadRecyclerViewData();
        news.addItemDecoration(new DividerItemDecoration(news.getContext(),DividerItemDecoration.VERTICAL));
        news.setHasFixedSize(true);
        news.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsAdapter(newsList);
        news.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnClickListener(this);
    }

    public void loadRecyclerViewData(){
        RequestQueue rq = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                "https://jsonplaceholder.typicode.com/todos/", null,
                response -> {
            try {
                for(int i=0;i<response.length();i++){
                    JSONObject obj = response.getJSONObject(i);
                    Log.d("em ro unnava",obj.getString("title"));
//                    Log.d("imageURL",obj.getString("thumbnailUrl"));
                    newsList.add(new Model(obj.getString("title"),""));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.d("Aipaye...",error.toString());
            Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        });
        rq.add(jsonArrayRequest);
    }

    @Override
    public void clicked(View v, int position) {
        Toast.makeText(this, "clicked"+adapter.getItem(position), Toast.LENGTH_SHORT).show();
//        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//        CustomTabsIntent customTabsIntent = builder.build();
//        customTabsIntent.launchUrl(this, Uri.parse(""));

    }
}