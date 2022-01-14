package com.brain.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.brain.R;
import com.brain.model.Anime;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.listeners.OnImageChangeListener;

import java.util.ArrayList;

@SuppressLint("ViewConstructor")
public class PosterOverlayView extends ConstraintLayout {
    ArrayList<Anime> items;
    int selectedPosition = 0;
    boolean isSingle;
    Anime model;
    TextView txtView;
    Bundle bundle;
    Toolbar toolbar;
    ImageView imageVerticalDots;
    StfalconImageViewer<Anime> imageViewer;

    public PosterOverlayView(@NonNull Context context, AttributeSet attrs, ArrayList<Anime> items, Bundle bundle) {
        super(context, attrs);
        this.items = items;
        this.bundle = bundle;
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_poster_overlay, this, true);
        setBackgroundColor(Color.TRANSPARENT);

        toolbar = view.findViewById(R.id.toolbarForImageFullScreen);
        txtView = view.findViewById(R.id.footer);

        items = bundle.getParcelableArrayList("arrParcelableImages");
        selectedPosition = bundle.getInt("position");
        isSingle = bundle.getBoolean("isSingle");

        StfalconImageViewer.Builder<Anime> builder = new StfalconImageViewer.Builder<>(context, items, (imageView, image) -> {
            Glide.with(context).load(image.getImage()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        });

        if (isSingle) {
            model = items.get(0);
            txtView.setText(model.getDescriptionFooter());
        } else {
            model = items.get(selectedPosition);
            txtView.setText(model.getDescriptionFooter());

            builder.withStartPosition(selectedPosition);
            builder.withImageChangeListener(position -> {
                model = items.get(position);
                txtView.setText(model.getDescriptionFooter());
            });
        }

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewer.dismiss();
            }
        });

        imageVerticalDots = view.findViewById(R.id.menuVerticalDots);
        imageVerticalDots.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Click download Image...", Toast.LENGTH_SHORT).show();
            }
        });

        builder.withOverlayView(view);
        imageViewer = builder.show();
    }
}