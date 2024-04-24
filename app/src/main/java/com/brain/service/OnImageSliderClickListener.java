package com.brain.service;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.brain.model.Poster;
import com.brain.multimediaslider.impl.ItemClickListenerImpl;
import com.brain.multimediaslider.model.Multimedia;
import com.brain.util.SharedData;
import com.brain.views.PosterOverlayView;

import java.util.ArrayList;
import java.util.List;

public class OnImageSliderClickListener implements ItemClickListenerImpl {
    Context context;
    private final ArrayList<Multimedia> multimediaList;
    List<Poster> posterList;
    Poster model;

    public OnImageSliderClickListener(Context context, ArrayList<Multimedia> multimediaList) {
        this.context = context;
        this.multimediaList = multimediaList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemSelected(int position) {
        SharedData extras = new SharedData(context);
        posterList = new ArrayList<>();

        for (int index = 0; index < multimediaList.size(); ++index) {
            model = new Poster();
            model.setId(multimediaList.get(index).getId());
            model.setUserName(multimediaList.get(index).getTitle());
            posterList.add(model);
        }

        extras.putBoolean("isSingle", posterList.size() == 1);
        extras.putPosterList("posterList", posterList);
        extras.putInt("position", position);

        new PosterOverlayView(context, null, posterList, extras);
    }
}
