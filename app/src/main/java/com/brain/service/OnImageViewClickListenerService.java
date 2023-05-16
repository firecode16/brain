package com.brain.service;

import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.brain.model.MediaDetail;
import com.brain.util.SharedData;
import com.brain.views.PosterOverlayView;

import java.util.List;

public class OnImageViewClickListenerService implements View.OnClickListener {
    private final List<MediaDetail> mediaDetailList;
    int position;

    public OnImageViewClickListenerService(List<MediaDetail> mediaDetailList, int position) {
        this.mediaDetailList = mediaDetailList;
        this.position = position;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        SharedData extras = new SharedData(v.getContext());
        extras.putBoolean("isSingle", mediaDetailList.size() == 1);
        extras.putMediaDetailList("mediaDetailList", mediaDetailList);
        extras.putInt("position", position);

        new PosterOverlayView(v.getContext(), null, mediaDetailList, extras);
    }
}
