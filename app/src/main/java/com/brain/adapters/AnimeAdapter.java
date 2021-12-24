package com.brain.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brain.R;
import com.brain.holders.AnimeViewHolder;
import com.brain.model.Anime;
import com.brain.service.OnImageSliderClickListener;
import com.brain.service.OnImageViewClickListenerService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeViewHolder> {
    final Context context;
    ArrayList<ArrayList<Anime>> objectMatrix;
    ArrayList<Anime> items;
    Anime model;
    ArrayList<SlideModel> itemToSlideModel;

    public AnimeAdapter(Context context, ArrayList<ArrayList<Anime>> objectMatrix) {
        this.context = context;
        this.objectMatrix = objectMatrix;
    }

    @NonNull
    @Override
    public AnimeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.container_cards, viewGroup, false);
        itemToSlideModel = new ArrayList<>();
        return new AnimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeViewHolder holder, int position) {
        items = objectMatrix.get(position);
        if (items.size() == 1) {
            model = items.get(0);
            Glide.with(context).load(model.getImage()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imagePost);
            holder.userName.setText(model.getUserName());
            holder.visits.setText(String.valueOf(model.getVisits()));
            holder.imagePost.setOnClickListener(new OnImageViewClickListenerService(items, position));
        } else {
            for (int index = 0; index < items.size(); ++index) {
                Glide.with(context).load(items.get(index).getImage()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imagePost);
                holder.userName.setText(items.get(index).getUserName());
                holder.visits.setText(String.valueOf(items.get(index).getVisits()));

                itemToSlideModel.add(new SlideModel(items.get(index).getImage(), items.get(index).getDescriptionFooter(), ScaleTypes.CENTER_CROP));
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
