package com.example.newsshare;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private final List<Model> newsList;
    public NewsAdapter(List<Model> newsList) {
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Model model = newsList.get(position);
        holder.title.setText(model.getContent());
        holder.date.setText(model.getDate());
        Glide.with(holder.image.getContext()).load(newsList.get(position).getImageUrl()).into(holder.image);
        holder.singleNews.setOnClickListener(v -> {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(v.getContext(), Uri.parse(model.getContentUrl()));
        });
        holder.share.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");i.putExtra(Intent.EXTRA_TEXT,"Checkout the news\n"+model.getContentUrl());
            v.getContext().startActivity(Intent.createChooser(i,"choose an app"));
        });
        holder.save.setOnClickListener(v -> {
            MyDbHandler dbHandler = new MyDbHandler(v.getContext());
            SavedModel news = new SavedModel();
            news.setImageUrl(model.getImageUrl());
            news.setDescription(model.getContent());
            news.setDate(model.getDate());
            news.setNewsUrl(model.getContentUrl());
            dbHandler.addNews(news);
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final ImageView image;
        private final ConstraintLayout singleNews;
        private final TextView date;
        private final ImageView share;
        private final ImageView save;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.publishedOn);
            singleNews = itemView.findViewById(R.id.singleNews);
            share = itemView.findViewById(R.id.share);
            save = itemView.findViewById(R.id.save);
        }
    }
}
