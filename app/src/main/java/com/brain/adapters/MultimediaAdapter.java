package com.brain.adapters;

import static com.brain.util.Util.containsInstance;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brain.R;
import com.brain.holders.MultimediaViewHolder;
import com.brain.model.Poster;
import com.brain.model.Video;
import com.brain.service.OnImageSliderClickListener;
import com.brain.service.OnImageViewClickListenerService;
import com.brain.service.VideoPlayService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class MultimediaAdapter extends RecyclerView.Adapter<MultimediaViewHolder> {
    Context context;
    ArrayList<SlideModel> slideModelList;
    ArrayList<Object> objectMatrix;
    ArrayList<Poster> postersList;
    ArrayList<Video> videoList;
    RecyclerView recyclerView;

    Poster modelPoster;
    Video modelVideo;

    public MultimediaAdapter(Context context, ArrayList<Object> objectMatrix, RecyclerView recyclerView) {
        this.context = context;
        this.objectMatrix = objectMatrix;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public MultimediaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.container_cards, viewGroup, false);
        slideModelList = new ArrayList<>();
        return new MultimediaViewHolder(view);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(@NonNull MultimediaViewHolder holder, int position) {
        if (objectMatrix.size() > 0) {
            final ArrayList<Object> objectList = (ArrayList<Object>) objectMatrix.get(position);

            if (containsInstance(objectList, Poster.class)) {
                if (objectList.size() == 1) {
                    modelPoster = (Poster) objectList.get(0);
                    Glide.with(context).load(modelPoster.getImage()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imagePost);
                    holder.userName.setText(modelPoster.getUserName());
                    holder.visits.setText(String.valueOf(modelPoster.getVisits()));

                    postersList = new ArrayList(objectList);
                    holder.imagePost.setOnClickListener(new OnImageViewClickListenerService(postersList, position));
                } else {
                    for (int index = 0; index < objectList.size(); ++index) {
                        modelPoster = (Poster) objectList.get(index);
                        holder.userName.setText(modelPoster.getUserName());
                        holder.visits.setText(String.valueOf(modelPoster.getVisits()));

                        slideModelList.add(new SlideModel(modelPoster.getId(), modelPoster.getImage(), modelPoster.getDescriptionFooter(), ScaleTypes.CENTER_CROP));
                        holder.imageSlider.setImageList(slideModelList);
                        holder.imageSlider.setItemClickListener(new OnImageSliderClickListener(context, slideModelList));
                    }
                }
            } else if (containsInstance(objectList, Video.class)) {
                modelVideo = (Video) objectList.get(0);
                holder.userName.setText(modelVideo.getUserName());
                holder.visits.setText(String.valueOf(modelVideo.getVisits()));

                holder.progressBar.setVisibility(View.VISIBLE);
                holder.volumeControl.setVisibility(View.VISIBLE);
                holder.videoPost.setVisibility(View.VISIBLE);
                int itemIndex = holder.getBindingAdapterPosition();

                VideoPlayService.Companion.initPlayer(context, modelVideo.getUrlVideo(), itemIndex, false, holder);

                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        assert layoutManager != null;
                        int index = layoutManager.findFirstCompletelyVisibleItemPosition();

                        // play just visible item
                        if (index != -1) {
                            VideoPlayService.Companion.playIndexThenPausePreviousPlayer(index);
                        }
                    }
                });

                holder.volumeControl.setOnClickListener(v -> VideoPlayService.Companion.getToggleVolume(context, holder));
            }
        }
    }

    @Override
    public int getItemCount() {
        return objectMatrix.size();
    }

    @Override
    public void onViewRecycled(@NonNull MultimediaViewHolder holder) {
        super.onViewRecycled(holder);
        Log.e("onViewRecycled >> ", "recycled...");
    }
}
