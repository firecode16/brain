package com.brain.service;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.brain.model.ImageBinary;
import com.brain.model.MediaDetail;
import com.brain.util.SharedData;
import com.brain.views.PosterOverlayView;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class OnImageSliderClickListener implements ItemClickListener {
    Context context;
    ArrayList<SlideModel> slideModelList;
    List<MediaDetail> mediaDetailList;
    MediaDetail model;

    public OnImageSliderClickListener(Context context, ArrayList<SlideModel> slideModelList) {
        this.context = context;
        this.slideModelList = slideModelList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemSelected(int position) {
        SharedData extras = new SharedData(context);
        mediaDetailList = new ArrayList<>();

        for (int index = 0; index < slideModelList.size(); ++index) {
            model = new MediaDetail();
            ImageBinary imageBinary = new ImageBinary();
            byte[] imageByte = slideModelList.get(index).getImageBytePath();
            imageBinary.setData(Base64.getEncoder().encodeToString(imageByte));
            model.setBinaryContent(imageBinary);
            model.setId(slideModelList.get(index).getId());

            /*model.setId(slideModelList.get(index).getId());
            model.setImage(Objects.requireNonNull(slideModelList.get(index).getImagePath()));
            model.setDescriptionFooter(slideModelList.get(index).getTitle());*/
            mediaDetailList.add(model);
        }

        extras.putBoolean("isSingle", mediaDetailList.size() == 1);
        extras.putMediaDetailList("mediaDetailList", mediaDetailList);
        extras.putInt("position", position);

        new PosterOverlayView(context, null, mediaDetailList, extras);
    }
}
