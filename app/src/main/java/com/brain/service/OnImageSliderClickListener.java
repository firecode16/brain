package com.brain.service;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.brain.model.Poster;
import com.brain.util.SharedData;
import com.brain.views.PosterOverlayView;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

public class OnImageSliderClickListener implements ItemClickListener {
    Context context;
    private final ArrayList<SlideModel> slideModelList;
    List<Poster> posterList;
    Poster model;

    public OnImageSliderClickListener(Context context, ArrayList<SlideModel> slideModelList) {
        this.context = context;
        this.slideModelList = slideModelList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemSelected(int position) {
        SharedData extras = new SharedData(context);
        posterList = new ArrayList<>();

        for (int index = 0; index < slideModelList.size(); ++index) {
            model = new Poster();
            model.setId(slideModelList.get(index).getId());
            model.setUserName(slideModelList.get(index).getTitle());
            posterList.add(model);
        }

        extras.putBoolean("isSingle", posterList.size() == 1);
        extras.putPosterList("posterList", posterList);
        extras.putInt("position", position);

        new PosterOverlayView(context, null, posterList, extras);
    }
}
