package com.brain.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;

import com.brain.R;
import com.denzcoskun.imageslider.ImageSlider;

public class MultimediaViewHolder extends RecyclerView.ViewHolder {
    public PlayerView videoPost;
    public ProgressBar progressBar;
    public ImageView volumeControl;

    public ImageView imagePost;
    public ImageSlider imageSlider;

    public TextView userName;

    public MultimediaViewHolder(@NonNull View itemView) {
        super(itemView);
        videoPost = itemView.findViewById(R.id.videoPost);
        progressBar = itemView.findViewById(R.id.progressBar);
        volumeControl = itemView.findViewById(R.id.volumeControl);

        imagePost = itemView.findViewById(R.id.imagePost);
        imageSlider = itemView.findViewById(R.id.imageSlider);

        userName = itemView.findViewById(R.id.userName);
    }
}
