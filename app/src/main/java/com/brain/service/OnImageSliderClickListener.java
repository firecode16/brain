package com.brain.service;

import android.content.Context;

import com.brain.model.Poster;
import com.brain.util.SharedData;
import com.brain.views.PosterOverlayView;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.Objects;

public class OnImageSliderClickListener implements ItemClickListener {
    Context context;
    ArrayList<SlideModel> slideModelList;
    ArrayList<Poster> postersList;
    Poster model;

    public OnImageSliderClickListener(Context context, ArrayList<SlideModel> slideModelList) {
        this.context = context;
        this.slideModelList = slideModelList;
    }

    @Override
    public void onItemSelected(int position) {
        SharedData extras = new SharedData(context);
        postersList = new ArrayList<>();

        for (int index = 0; index < slideModelList.size(); ++index) {
            model = new Poster();
            model.setId(slideModelList.get(index).getId());
            model.setImage(Objects.requireNonNull(slideModelList.get(index).getImagePath()));
            model.setDescriptionFooter(slideModelList.get(index).getTitle());
            postersList.add(model);
        }

        extras.putBoolean("isSingle", postersList.size() == 1);
        extras.putListPoster("listPoster", postersList);
        extras.putInt("position", position);

        new PosterOverlayView(context, null, postersList, extras);
    }
}
