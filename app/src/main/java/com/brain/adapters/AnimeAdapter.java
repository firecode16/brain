package com.brain.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brain.R;
import com.brain.holders.AnimeViewHolder;
import com.brain.model.Anime;

import java.util.List;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeViewHolder> {
    final Context context;
    private final List<Anime> items;

    public AnimeAdapter(Context context, List<Anime> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public AnimeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.container_cards, viewGroup, false);
        return new AnimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeViewHolder holder, int position) {
        holder.imagePost.setImageResource(items.get(position).getImage());
        holder.name.setText(items.get(position).getName());
        holder.visits.setText("Visits: " + String.valueOf(items.get(position).getVisits()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
