package com.brain.service;

import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.brain.model.MediaContent;
import com.brain.model.Poster;
import com.brain.util.SharedData;
import com.brain.views.PosterOverlayView;

import java.util.ArrayList;
import java.util.List;

public class OnImageViewClickListenerService implements View.OnClickListener {
    private final List<MediaContent> mediaContents;
    private List<Poster> posterList;
    private Poster model;
    int position;

    public OnImageViewClickListenerService(List<MediaContent> mediaContents, int position) {
        this.mediaContents = mediaContents;
        this.position = position;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        posterList = new ArrayList<>();

        mediaContents.forEach(item -> {
            model = new Poster();
            model.setId(item.get_id());
            model.setUserName(item.getMultimediaName());
            posterList.add(model);
        });

        SharedData extras = new SharedData(v.getContext());
        extras.putBoolean("isSingle", mediaContents.size() == 1);
        extras.putPosterList("posterList", posterList);
        extras.putInt("position", position);

        new PosterOverlayView(v.getContext(), null, posterList, extras);
    }
}
