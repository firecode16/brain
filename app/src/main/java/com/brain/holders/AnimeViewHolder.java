package com.brain.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brain.R;

public class AnimeViewHolder extends RecyclerView.ViewHolder {
    public ImageView imagePost;
    public TextView name;
    public TextView visits;

    public AnimeViewHolder(@NonNull View itemView) {
        super(itemView);
        imagePost = (ImageView) itemView.findViewById(R.id.imagePost);
        name = (TextView) itemView.findViewById(R.id.name);
        visits = (TextView) itemView.findViewById(R.id.visits);
    }

}
