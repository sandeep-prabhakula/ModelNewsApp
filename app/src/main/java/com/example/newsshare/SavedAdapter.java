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

public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.ViewHolder> {
    private final List<SavedModel>list;
    public SavedAdapter(List<SavedModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SavedModel model = list.get(position);
        holder.title.setText(model.getNewsUrl());
        holder.date.setText(model.getDate());
        Glide.with(holder.image.getContext()).load(list.get(position).getImageUrl()).into(holder.image);
        holder.singleNews.setOnClickListener(v -> {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(v.getContext(), Uri.parse(model.getDescription()));
        });
        holder.share.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");i.putExtra(Intent.EXTRA_TEXT,"Checkout the news\n"+model.getDescription());
            v.getContext().startActivity(Intent.createChooser(i,"choose an app"));
        });
        holder.save.setOnClickListener(v -> {
            MyDbHandler dbHandler = new MyDbHandler(v.getContext());
            dbHandler.deleteTodo(model.getId());
            v.getContext().startActivity(new Intent(v.getContext(),Saved.class));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
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
            save = itemView.findViewById(R.id.unsave);
        }
    }
}
