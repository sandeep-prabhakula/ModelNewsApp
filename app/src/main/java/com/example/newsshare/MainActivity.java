package com.example.newsshare;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Model> newsList;
    NewsAdapter adapter;
    RecyclerView news;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        news = findViewById(R.id.news);
        newsList = new ArrayList<>();
        loadNews();
        news.addItemDecoration(new DividerItemDecoration(news.getContext(),DividerItemDecoration.VERTICAL));
        news.setHasFixedSize(true);
        news.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsAdapter(newsList);
        adapter.notifyDataSetChanged();
    }

    private void loadNews() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();
        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://saurav.tech/NewsAPI/everything/cnn.json", null, response -> {
                    try {
                        pd.dismiss();
                        JSONArray jsonArray = response.getJSONArray("articles");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject obj = jsonArray.getJSONObject(i);
                            newsList.add(new Model(obj.getString("description"),
                                                   obj.getString("urlToImage"),
                                                   obj.getString("url"),
                                                   obj.getString("publishedAt")));
                        }
                        news.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            pd.dismiss();
            Log.d("Rale",error.toString());
            if(error instanceof NetworkError) Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        });
        rq.add(jsonObjectRequest);
    }
}