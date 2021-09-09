package com.example.newsshare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    private static List<CategoryPOJO> list ;
    private static OnItemClickListener listener;
    public CategoryAdapter(List<CategoryPOJO> category) {
        list = category;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        CategoryPOJO model = list.get(position);
        holder.cat.setText(model.getCategory());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView cat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cat = itemView.findViewById(R.id.catName);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(list.get(position));
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(CategoryPOJO categoryPOJO);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        CategoryAdapter.listener = listener;
    }
}
