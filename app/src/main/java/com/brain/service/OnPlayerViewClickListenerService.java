package com.brain.service;

import static com.brain.util.Util.URL;
import static com.brain.util.Util.URL_PART;

import android.view.View;

import com.brain.model.MediaContent;
import com.brain.multimediapuzzlesviewer.MultimediaPuzzlesViewer;
import com.brain.multimediapuzzlesviewer.model.Poster;

import java.util.ArrayList;
import java.util.List;

public class OnPlayerViewClickListenerService implements View.OnClickListener {
    private final List<MediaContent> mediaContents;
    private List<Poster> posterList;
    private Poster model;
    private final int itemPosition;
    private final int position;
    private final String container;

    public OnPlayerViewClickListenerService(List<MediaContent> mediaContents, int itemPosition, int position, String container) {
        this.mediaContents = mediaContents;
        this.itemPosition = itemPosition;
        this.position = position;
        this.container = container;
    }

    @Override
    public void onClick(View v) {
        posterList = new ArrayList<>();

        mediaContents.forEach(item -> {
            model = new Poster();
            model.setId(item.get_id());
            model.setUserName(item.getMultimediaName());
            model.setContentType(item.getContentType());
            posterList.add(model);
        });

        new MultimediaPuzzlesViewer.Builder(v.getContext(), posterList, itemPosition, position, URL + URL_PART, container).show();
    }
}
