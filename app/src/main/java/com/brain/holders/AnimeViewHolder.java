package com.brain.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brain.R;
import com.denzcoskun.imageslider.ImageSlider;

public class AnimeViewHolder extends RecyclerView.ViewHolder {
    public TextView userName;
    public ImageView imagePost;
    public TextView description;
    public TextView visits;
    public ImageSlider imageSlider;

    public AnimeViewHolder(@NonNull View itemView) {
        super(itemView);
        imagePost = itemView.findViewById(R.id.imagePost);
        userName = itemView.findViewById(R.id.userName);
        description = itemView.findViewById(R.id.descriptionPost);
        visits = itemView.findViewById(R.id.visits);
        imageSlider = itemView.findViewById(R.id.imageSlider);
    }

}
