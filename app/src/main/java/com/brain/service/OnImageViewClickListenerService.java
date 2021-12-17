package com.brain.service;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.brain.fragments.SlideshowDialogFragment;
import com.brain.model.Anime;

import java.util.ArrayList;

public class OnImageViewClickListenerService implements View.OnClickListener {
    private final ArrayList<Anime> items;
    private final int position;
    AppCompatActivity appActivity;

    public OnImageViewClickListenerService(ArrayList<Anime> items, int position) {
        this.items = items;
        this.position = position;
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("arrParcelableImages", items);
        bundle.putInt("position", position);

        appActivity = (AppCompatActivity) v.getContext();
        SlideshowDialogFragment slideshowDialogFragment = SlideshowDialogFragment.newInstance();
        slideshowDialogFragment.setArguments(bundle);
        slideshowDialogFragment.show(appActivity.getSupportFragmentManager().beginTransaction(), "slideshow");
    }

}
