package com.example.newsshare;

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
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private final List<Model> newsList;
    public ItemClicker itemClicker;
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
        int width = holder.image.getWidth();
        int height = holder.image.getHeight();
        holder.title.setText(model.getContent());
        String s = model.getContent();
//        Glide.with(holder.image.getContext()).load(newsList.get(position).getImageUrl()).into(holder.image);
//        Picasso.get().load(model.getImageUrl())
//                .resize(width,height).into(holder.image);
        holder.singleNews.setOnClickListener(v -> {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(v.getContext(), Uri.parse("https://google"+s+".com"));
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView title;
        private final ImageView image;
        private final ConstraintLayout singleNews;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            singleNews = itemView.findViewById(R.id.singleNews);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(itemClicker!=null)itemClicker.clicked(v,getAdapterPosition());
        }
    }
    Model getItem(int id){
        return newsList.get(id);
    }
    void setOnClickListener(ItemClicker clickListener){
        this.itemClicker = clickListener;
    }

    public interface ItemClicker{
        void clicked(View v,int position);
    }
}
