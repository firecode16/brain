package com.brain.multimediapuzzlesviewer;

import android.content.Context;
import android.util.Log;

import com.brain.multimediapuzzlesviewer.dialog.MediaViewerDialog;
import com.brain.multimediapuzzlesviewer.model.BuilderData;
import com.brain.multimediapuzzlesviewer.model.Poster;

import java.util.List;

public class MultimediaPuzzlesViewer {
    private final Context context;
    private final BuilderData builderData;
    private final MediaViewerDialog dialog;

    protected MultimediaPuzzlesViewer(Context context, BuilderData builderData, Integer itemPosition, Integer position, String url) {
        this.context = context;
        this.builderData = builderData;
        this.dialog = new MediaViewerDialog(context, builderData, itemPosition, position, url);
    }

    public void show() {
        if (!builderData.getMediaList().isEmpty()) {
            dialog.show();
        } else {
            Log.w(context.getString(R.string.app_name), "list cannot be empty! Viewer ignored.");
        }
    }

    public static class Builder {
        private final Context context;
        private final BuilderData data;
        private final String url;
        private final Integer itemPosition;
        private final Integer position;

        public Builder(Context context, List<Poster> dataList, Integer itemPosition, Integer position, String url) {
            this.context = context;
            this.data = new BuilderData(dataList);
            this.itemPosition = itemPosition;
            this.position = position;
            this.url = url;
        }

        public MultimediaPuzzlesViewer build() {
            return new MultimediaPuzzlesViewer(context, data, itemPosition, position, url);
        }

        public void show() {
            MultimediaPuzzlesViewer viewer = build();
            viewer.show();
        }
    }
}
