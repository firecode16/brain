package com.brain.service;

import android.os.Bundle;
import android.view.View;

import com.brain.model.Anime;
import com.brain.views.PosterOverlayView;

import java.util.ArrayList;

public class OnImageViewClickListenerService implements View.OnClickListener {
    ArrayList<Anime> items;
    int position;

    public OnImageViewClickListenerService(ArrayList<Anime> items, int position) {
        this.items = items;
        this.position = position;
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isSingle", items.size() == 1);
        bundle.putParcelableArrayList("arrParcelableImages", items);
        bundle.putInt("position", position);

        new PosterOverlayView(v.getContext(), null, items, bundle);
    }
}
