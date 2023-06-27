package com.brain.views;

import static com.brain.util.Util.URL;
import static com.brain.util.Util.URL_PART;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.brain.R;
import com.brain.model.Poster;
import com.brain.util.SharedData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.stfalcon.imageviewer.StfalconImageViewer;

import java.util.List;

@SuppressLint("ViewConstructor")
public class PosterOverlayView extends ConstraintLayout {
    List<Poster> posterList;
    int selectedPosition = 0;
    boolean isSingle;
    Poster model;
    TextView txtView;
    Toolbar toolbar;
    ImageView imageVerticalDots;
    SharedData extras;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public PosterOverlayView(@NonNull Context context, AttributeSet attrs, List<Poster> posterList, SharedData extras) {
        super(context, attrs);
        this.posterList = posterList;
        this.extras = extras;
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_poster_overlay, this, true);
        setBackgroundColor(Color.TRANSPARENT);

        toolbar = view.findViewById(R.id.toolbarForImageFullScreen);
        txtView = view.findViewById(R.id.footer);

        posterList = extras.getPosterList("posterList", Poster.class);
        selectedPosition = extras.getInt("position");
        isSingle = extras.getBoolean("isSingle");

        StfalconImageViewer.Builder<Poster> builder = new StfalconImageViewer.Builder<>(context, posterList, (imageView, image) -> {
            Glide.with(context).load(URL + URL_PART + image.getId()).thumbnail(0.5F).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        });

        if (isSingle) {
            model = posterList.get(0);
            txtView.setText(model.getUserName());
        } else {
            model = posterList.get(selectedPosition);
            txtView.setText(model.getUserName());

            builder.withStartPosition(selectedPosition);
            builder.withImageChangeListener(position -> {
                model = posterList.get(position);
                txtView.setText(model.getUserName());
            });
        }

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        imageVerticalDots = view.findViewById(R.id.menuVerticalDots);
        imageVerticalDots.setOnClickListener(v -> Toast.makeText(context, "Click download Image...", Toast.LENGTH_SHORT).show());

        builder.withOverlayView(view);
        builder.show();
    }
}