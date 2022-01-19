package com.brain.adapters;

import static com.brain.util.Util.containsInstance;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brain.R;
import com.brain.holders.MultimediaViewHolder;
import com.brain.model.Poster;
import com.brain.model.Video;
import com.brain.service.OnImageSliderClickListener;
import com.brain.service.OnImageViewClickListenerService;
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

    Poster modelPoster;
    Video modelVideo;

    public MultimediaAdapter(Context context, ArrayList<Object> objectMatrix) {
        this.context = context;
        this.objectMatrix = objectMatrix;
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
                holder.imagePlay.setVisibility(View.VISIBLE);
                holder.videoPost.setVisibility(View.VISIBLE);

                modelVideo = (Video) objectList.get(0);
                String videoPath = "android.resource://" + context.getPackageName() + "/" + modelVideo.getVideoPath();
                Uri uri = Uri.parse(videoPath);
                MediaController controller = new MediaController(context);

                Glide.with(context).load(modelVideo.getImagePlay()).into(holder.imagePlay);

                controller.setAnchorView(holder.videoPost);
                holder.videoPost.setMediaController(controller);
                holder.videoPost.setVideoURI(uri);

                holder.imagePlay.setOnClickListener(v -> {
                    holder.imagePlay.setVisibility(View.GONE);
                    holder.videoPost.start();
                });

                holder.videoPost.setOnCompletionListener(mp -> holder.imagePlay.setVisibility(View.VISIBLE));
            }
        }
    }

    @Override
    public int getItemCount() {
        return objectMatrix.size();
    }
}
