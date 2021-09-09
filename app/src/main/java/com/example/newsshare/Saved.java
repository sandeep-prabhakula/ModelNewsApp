package com.example.newsshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import java.util.List;

public class Saved extends AppCompatActivity {
    RecyclerView savedNews;
    List<SavedModel>list;
    SavedAdapter adapter;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        savedNews = findViewById(R.id.savedNews);
        MyDbHandler dbHandler = new MyDbHandler(this);
        list = dbHandler.getAllTodos();
        savedNews.addItemDecoration(new DividerItemDecoration(savedNews.getContext(),DividerItemDecoration.VERTICAL));
        savedNews.setHasFixedSize(true);
        savedNews.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SavedAdapter(list);
        savedNews.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
    }
}