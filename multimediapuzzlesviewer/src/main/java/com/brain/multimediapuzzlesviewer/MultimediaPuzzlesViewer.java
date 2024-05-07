package com.brain.multimediapuzzlesviewer;

import android.content.Context;
import android.util.Log;

import com.brain.multimediapuzzlesviewer.dialog.MediaViewerDialog;
import com.brain.multimediapuzzlesviewer.model.BuilderData;
import com.brain.multimediapuzzlesviewer.model.Poster;

import java.util.List;

public class MultimediaPuzzlesViewer<T> {
    private final Context context;
    private final BuilderData<Poster> builderData;
    private final MediaViewerDialog<T> dialog;

    protected MultimediaPuzzlesViewer(Context context, BuilderData<Poster> builderData, String url) {
        this.context = context;
        this.builderData = builderData;
        this.dialog = new MediaViewerDialog<>(context, builderData, url);
    }

    public void show() {
        if (!builderData.getMediaList().isEmpty()) {
            dialog.show();
        } else {
            Log.w(context.getString(R.string.app_name), "list cannot be empty! Viewer ignored.");
        }
    }

    public static class Builder<T> {
        private final Context context;
        private final BuilderData<Poster> data;
        private final String url;

        public Builder(Context context, List<Poster> dataList, String url) {
            this.context = context;
            this.data = new BuilderData<>(dataList);
            this.url = url;
        }

        public MultimediaPuzzlesViewer<T> build() {
            return new MultimediaPuzzlesViewer<>(context, data, url);
        }

        public void show() {
            MultimediaPuzzlesViewer<T> viewer = build();
            viewer.show();
        }
    }
}
