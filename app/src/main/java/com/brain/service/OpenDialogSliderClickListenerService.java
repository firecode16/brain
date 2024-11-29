package com.brain.service;

import static com.brain.util.Util.URL;
import static com.brain.util.Util.URL_PART;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.brain.multimediapuzzlesviewer.MultimediaPuzzlesViewer;
import com.brain.multimediapuzzlesviewer.model.Poster;
import com.brain.multimediaslider.impl.ItemClickListenerImpl;
import com.brain.multimediaslider.model.Multimedia;

import java.util.ArrayList;
import java.util.List;

public class OpenDialogSliderClickListenerService implements ItemClickListenerImpl {
    Context context;
    private final ArrayList<Multimedia> multimediaList;
    List<Poster> posterList;
    Poster model;

    public OpenDialogSliderClickListenerService(Context context, ArrayList<Multimedia> multimediaList) {
        this.context = context;
        this.multimediaList = multimediaList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemSelected(int itemPosition, int position) {
        posterList = new ArrayList<>();

        multimediaList.forEach(x -> {
            model = new Poster();
            model.setId(x.getId());
            model.setContentType(x.getContentType());
            model.setUserName(x.getTitle());
            posterList.add(model);
        });

        new MultimediaPuzzlesViewer.Builder<>(context, posterList, itemPosition, position,URL + URL_PART).show();
    }
}
