package com.brain.service;

import android.content.Context;
import android.os.Bundle;

import com.brain.model.Poster;
import com.brain.views.PosterOverlayView;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.Objects;

public class OnImageSliderClickListener implements ItemClickListener {
    Context context;
    ArrayList<SlideModel> listSlideModel;
    ArrayList<Poster> items;
    Poster model;

    public OnImageSliderClickListener(Context context, ArrayList<SlideModel> listSlideModel) {
        this.context = context;
        this.listSlideModel = listSlideModel;
    }

    @Override
    public void onItemSelected(int position) {
        Bundle bundle = new Bundle();
        items = new ArrayList<>();

        for (int index = 0; index < listSlideModel.size(); ++index) {
            model = new Poster();
            model.setId(listSlideModel.get(index).getId());
            model.setImage(Objects.requireNonNull(listSlideModel.get(index).getImagePath()));
            model.setDescriptionFooter(listSlideModel.get(index).getTitle());
            items.add(model);
        }

        bundle.putBoolean("isSingle", items.size() == 1);
        bundle.putParcelableArrayList("arrParcelableImages", items);
        bundle.putInt("position", position);

        new PosterOverlayView(context, null, items, bundle);
    }
}
