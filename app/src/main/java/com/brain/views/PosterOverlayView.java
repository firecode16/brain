package com.brain.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.brain.R;
import com.brain.model.Poster;
import com.brain.util.SharedData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.stfalcon.imageviewer.StfalconImageViewer;

import java.util.ArrayList;

@SuppressLint("ViewConstructor")
public class PosterOverlayView extends ConstraintLayout {
    ArrayList<Poster> postersList;
    int selectedPosition = 0;
    boolean isSingle;
    Poster model;
    TextView txtView;
    Toolbar toolbar;
    ImageView imageVerticalDots;
    StfalconImageViewer<Poster> imageViewer;
    SharedData extras;

    public PosterOverlayView(@NonNull Context context, AttributeSet attrs, ArrayList<Poster> postersList, SharedData extras) {
        super(context, attrs);
        this.postersList = postersList;
        this.extras = extras;
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_poster_overlay, this, true);
        setBackgroundColor(Color.TRANSPARENT);

        toolbar = view.findViewById(R.id.toolbarForImageFullScreen);
        txtView = view.findViewById(R.id.footer);

        postersList = extras.getListPoster("listPoster", Poster.class);
        selectedPosition = extras.getInt("position");
        isSingle = extras.getBoolean("isSingle");

        StfalconImageViewer.Builder<Poster> builder = new StfalconImageViewer.Builder<>(context, postersList, (imageView, image) -> Glide.with(context).load(image.getImage()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView));

        if (isSingle) {
            model = postersList.get(0);
            txtView.setText(model.getDescriptionFooter());
        } else {
            model = postersList.get(selectedPosition);
            txtView.setText(model.getDescriptionFooter());

            builder.withStartPosition(selectedPosition);
            builder.withImageChangeListener(position -> {
                model = postersList.get(position);
                txtView.setText(model.getDescriptionFooter());
            });
        }

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(v -> imageViewer.dismiss());

        imageVerticalDots = view.findViewById(R.id.menuVerticalDots);
        imageVerticalDots.setOnClickListener(v -> Toast.makeText(context, "Click download Image...", Toast.LENGTH_SHORT).show());

        builder.withOverlayView(view);
        imageViewer = builder.show();
    }
}