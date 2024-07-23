package com.brain.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;

import com.brain.R;
import com.brain.multimediaslider.MultimediaSlider;

public class MultimediaViewHolder extends RecyclerView.ViewHolder {
    public PlayerView postMedia;
    public ProgressBar progressBar;
    public ImageView volumeControl;

    public ImageView imagePost;
    public MultimediaSlider multimediaSlider;

    public TextView userName;
    public TextView descrProject;

    public MultimediaViewHolder(@NonNull View itemView) {
        super(itemView);
        postMedia = itemView.findViewById(R.id.postMedia);
        progressBar = itemView.findViewById(R.id.progressBar);
        volumeControl = itemView.findViewById(R.id.volumeControl);

        imagePost = itemView.findViewById(R.id.imagePost);
        multimediaSlider = itemView.findViewById(R.id.multimediaSlider);

        userName = itemView.findViewById(R.id.userName);
        descrProject = itemView.findViewById(R.id.descrProject);
    }
}
