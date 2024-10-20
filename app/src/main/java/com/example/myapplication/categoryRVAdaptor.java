package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class categoryRVAdaptor extends RecyclerView.Adapter<categoryRVAdaptor.ViewHolder> {
    private ArrayList<categoryRVmodel> categoryRVmodels;
    private Context context;
    private CategoryClickInterface categoryClickInterface;

    public categoryRVAdaptor(ArrayList<categoryRVmodel> categoryRVmodels, Context context, CategoryClickInterface categoryClickInterface) {
        this.categoryRVmodels = categoryRVmodels;
        this.context = context;
        this.categoryClickInterface = categoryClickInterface;
    }

    @NonNull
    @Override
    public categoryRVAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_rv_item,parent,false);
        return new categoryRVAdaptor.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull categoryRVAdaptor.ViewHolder holder, int position) {
        categoryRVmodel categoryRVModel = categoryRVmodels.get(position);
        holder.categoryTV.setText(categoryRVModel.getCategory());
        Picasso.get().load(categoryRVModel.getCategoryImageUrl()).into(holder.categoryIV);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryClickInterface.onCategoryClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryRVmodels.size();
    }
    public interface CategoryClickInterface{
        void onCategoryClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryTV;
        private ImageView categoryIV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTV = itemView.findViewById(R.id.idTVcategories);
            categoryIV = itemView.findViewById(R.id.idIVcategories);
        }
    }
}
