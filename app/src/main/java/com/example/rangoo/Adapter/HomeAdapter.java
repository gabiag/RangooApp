package com.example.rangoo.Adapter;

import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rangoo.Model.Food;
import com.example.rangoo.R;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder> {

    private final int[] colors = {R.color.baron, R.color.pumpkin, R.color.vegan};
    private final ArrayList<Food> list;

    public HomeAdapter(ArrayList<Food> list){
        this.list = list;
    }

    public ArrayList<Food> getList() { return  list; }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_home, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Food food = list.get(position);
        holder.bind(food);
        holder.itemView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.getContext(), colors[position % colors.length])));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CLIQUE", "DETECTADO");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
