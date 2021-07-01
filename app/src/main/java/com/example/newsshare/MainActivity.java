package com.example.newsshare;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
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
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    List<Model> newsList;
    NewsAdapter adapter;
    RecyclerView news;
    int page = 0;
    int limit = 10;
    NestedScrollView nest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        nest = findViewById(R.id.nested);

        ImageView saved = findViewById(R.id.saved);
        saved.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,Saved.class)));

        loadNews( page, limit);
        nest.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                page++;
                loadNews(page,limit);
            }
        });
        news = findViewById(R.id.news);
        newsList = new ArrayList<>();
        news.addItemDecoration(new DividerItemDecoration(news.getContext(),DividerItemDecoration.VERTICAL));
        news.setHasFixedSize(true);
        news.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsAdapter(newsList);
        adapter.notifyDataSetChanged();
    }

    private void loadNews(int page,int limit) {
        if (page > limit) {
            Toast.makeText(this, "You're all caught up", Toast.LENGTH_SHORT).show();
            return;
        }
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();
        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://api.currentsapi.services/v1/latest-news?language=en&apiKey=35lSJkzqiVJ1mx6HphYIy2cKhsTALMe1YsAeeYuM_SL-pGXr", null,
                response -> {
                    try {
                        pd.dismiss();
                        JSONArray jsonArray = response.getJSONArray("news");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject obj = jsonArray.getJSONObject(i);
                            newsList.add(new Model(obj.getString("description"),
                                                   obj.getString("image"),
                                                   obj.getString("url"),
                                                   obj.getString("published")));
                        }
                        news.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            pd.dismiss();
            Log.d("Rale",error.toString());
            if(error instanceof NetworkError){
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_baseline_error_24)
                        .setTitle("No Internet Connection")
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, which) -> onBackPressed())
                        .setMessage("please check your internet connection or make your wifi toggle switch on.")
                        .show();
            }
        });
        rq.add(jsonObjectRequest);
    }
}