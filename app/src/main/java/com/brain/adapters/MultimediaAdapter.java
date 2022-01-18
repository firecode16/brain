package com.brain.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brain.R;
import com.brain.holders.MultimediaViewHolder;
import com.brain.model.Poster;
import com.brain.service.OnImageSliderClickListener;
import com.brain.service.OnImageViewClickListenerService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class MultimediaAdapter extends RecyclerView.Adapter<MultimediaViewHolder> {
    final Context context;
    ArrayList<ArrayList<Poster>> objectMatrix;
    ArrayList<Poster> items;
    Poster model;
    ArrayList<SlideModel> itemToSlideModel;

    public MultimediaAdapter(Context context, ArrayList<ArrayList<Poster>> objectMatrix) {
        this.context = context;
        this.objectMatrix = objectMatrix;
    }

    @NonNull
    @Override
    public MultimediaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.container_cards, viewGroup, false);
        itemToSlideModel = new ArrayList<>();
        return new MultimediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MultimediaViewHolder holder, int position) {
        items = objectMatrix.get(position);
        if (items.size() == 1) {
            model = items.get(0);
            Glide.with(context).load(model.getImage()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imagePost);
            holder.userName.setText(model.getUserName());
            holder.visits.setText(String.valueOf(model.getVisits()));
            holder.imagePost.setOnClickListener(new OnImageViewClickListenerService(items, position));
        } else {
            for (int index = 0; index < items.size(); ++index) {
                holder.userName.setText(items.get(index).getUserName());
                holder.visits.setText(String.valueOf(items.get(index).getVisits()));

                itemToSlideModel.add(new SlideModel(items.get(index).getId(), items.get(index).getImage(), items.get(index).getDescriptionFooter(), ScaleTypes.CENTER_CROP));
                holder.imageSlider.setImageList(itemToSlideModel);
                holder.imageSlider.setItemClickListener(new OnImageSliderClickListener(context, itemToSlideModel));
            }
        }
    }

    @Override
    public int getItemCount() {
        return objectMatrix.size();
    }

}
