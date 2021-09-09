package com.example.newsshare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

public class MainActivity extends AppCompatActivity {
    List<Model> newsList;
    NewsAdapter adapter;
    List<CategoryPOJO>category;
    List<Model>filterList = new ArrayList<>();
    RecyclerView news,categories;
    int page = 0;
    int limit = 10;
    ProgressBar pb3;
    NestedScrollView nest;
    CategoryAdapter categoryAdapter;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nest = findViewById(R.id.nested);
        categories = findViewById(R.id.categories);
        ImageView saved = findViewById(R.id.saved);
        pb3 = findViewById(R.id.actvityMainProgressBar);
        saved.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,Saved.class)));
        pb3.setVisibility(View.GONE);
        loadNews( page, limit);
        nest.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                page++;
                loadNews(page,limit);
            }
        });
        news = findViewById(R.id.news);
        newsList = new ArrayList<>();
        category = new ArrayList<>();
        category.add(new CategoryPOJO("sports"));
        category.add(new CategoryPOJO("business"));
        category.add(new CategoryPOJO("health"));
        news.addItemDecoration(new DividerItemDecoration(news.getContext(),DividerItemDecoration.VERTICAL));
        news.setHasFixedSize(true);
        news.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsAdapter(filterList);
        adapter.notifyDataSetChanged();
        categories.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        categories.setHasFixedSize(true);
        categoryAdapter = new CategoryAdapter(category);
        categoryAdapter.notifyDataSetChanged();
        categories.setAdapter(categoryAdapter);
        categoryAdapter.setOnItemClickListener(categoryPOJO -> loadNewsData(categoryPOJO.getCategory()));
    }

    private void loadNewsData(String category) {
        newsList.clear();
        filterList.clear();
        pb3.setVisibility(View.VISIBLE);
        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://api.currentsapi.services/v1/search?language=en&category="+category+"&apiKey=35lSJkzqiVJ1mx6HphYIy2cKhsTALMe1YsAeeYuM_SL-pGXr", null,
                response -> {
                    try {
                        pb3.setVisibility(View.GONE);
                        JSONArray jsonArray = response.getJSONArray("news");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject obj = jsonArray.getJSONObject(i);
                            newsList.add(new Model(obj.getString("description"),
                                    obj.getString("image"),
                                    obj.getString("url"),
                                    obj.getString("published")));
                        }
                        filterList.addAll(newsList);
                        news.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            pb3.setVisibility(View.GONE);
            Log.d("Rale",error.toString());
            if(error instanceof NetworkError){
                Intent intent = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                startActivity(intent);
            }
        });
        rq.add(jsonObjectRequest);
    }

    private void loadNews(int page,int limit) {
        pb3.setVisibility(View.VISIBLE);
        if (page > limit) {
            Toast.makeText(this, "You're all caught up", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://api.currentsapi.services/v1/latest-news?language=en&apiKey=35lSJkzqiVJ1mx6HphYIy2cKhsTALMe1YsAeeYuM_SL-pGXr", null,
                response -> {
                    try {
                        pb3.setVisibility(View.GONE);
                        JSONArray jsonArray = response.getJSONArray("news");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject obj = jsonArray.getJSONObject(i);
                            filterList.add(new Model(obj.getString("description"),
                                                   obj.getString("image"),
                                                   obj.getString("url"),
                                                   obj.getString("published")));
                        }
                        news.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            pb3.setVisibility(View.GONE);
            Log.d("Rale",error.toString());
            if(error instanceof NetworkError){
                Intent intent = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                startActivity(intent);
            }
        });
        rq.add(jsonObjectRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search,menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNews(page,limit);
    }
}