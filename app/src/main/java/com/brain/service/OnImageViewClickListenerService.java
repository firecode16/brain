package com.brain.service;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.brain.fragments.ImageSliderDialogFragment;
import com.brain.model.Anime;

import java.util.ArrayList;

public class OnImageViewClickListenerService implements View.OnClickListener {
    ArrayList<Anime> items;
    int position;
    AppCompatActivity appActivity;

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

        appActivity = (AppCompatActivity) v.getContext();
        ImageSliderDialogFragment imageSlider = ImageSliderDialogFragment.newInstance();
        imageSlider.setArguments(bundle);
        imageSlider.show(appActivity.getSupportFragmentManager().beginTransaction(), "slideshow");
    }

}
