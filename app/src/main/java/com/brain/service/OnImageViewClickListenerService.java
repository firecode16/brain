package com.brain.service;

import android.view.View;

import com.brain.model.Poster;
import com.brain.util.SharedData;
import com.brain.views.PosterOverlayView;

import java.util.ArrayList;

public class OnImageViewClickListenerService implements View.OnClickListener {
    ArrayList<Poster> postersList;
    int position;

    public OnImageViewClickListenerService(ArrayList<Poster> postersList, int position) {
        this.postersList = postersList;
        this.position = position;
    }

    @Override
    public void onClick(View v) {
        SharedData extras = new SharedData(v.getContext());
        extras.putBoolean("isSingle", postersList.size() == 1);
        extras.putListPoster("listPoster", postersList);
        extras.putInt("position", position);

        new PosterOverlayView(v.getContext(), null, postersList, extras);
    }
}
