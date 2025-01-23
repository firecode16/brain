package com.brain.holders;

import static com.brain.util.Util.AUDIO_MP3;
import static com.brain.util.Util.IMG_GIF;
import static com.brain.util.Util.IMG_JPEG;
import static com.brain.util.Util.IMG_JPG;
import static com.brain.util.Util.IMG_PNG;
import static com.brain.util.Util.MP3;
import static com.brain.util.Util.MP4;
import static com.brain.util.Util.URL;
import static com.brain.util.Util.URL_PART;
import static com.brain.util.Util.VIDEO_MP4;
import static com.brain.util.Util.loadImage;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;

import com.brain.R;
import com.brain.model.MediaContent;
import com.brain.model.MediaDetail;
import com.brain.model.Profile;
import com.brain.multimediaplayer.service.MediaPlayerService;
import com.brain.multimediaslider.MultimediaSlider;
import com.brain.multimediaslider.model.ItemPlayerView;
import com.brain.multimediaslider.model.Multimedia;
import com.brain.service.OnImageViewClickListenerService;
import com.brain.service.OnPlayerViewClickListenerService;
import com.brain.service.OpenDialogSliderClickListenerService;
import com.bumptech.glide.load.resource.gif.GifDrawable;

import java.util.ArrayList;
import java.util.List;

public class MultimediaViewHolder extends RecyclerView.ViewHolder {
    PlayerView postMedia;
    ProgressBar progressBar;
    ImageView volumeControl;

    ImageView imagePost;
    public MultimediaSlider multimediaSlider;
    ImageView share;

    TextView userName;
    TextView descrProject;

    private static final int CURRENT_ITEM = 0;
    ItemPlayerView itemPlayerView;
    List<Multimedia> multimediaList;

    public MultimediaViewHolder(@NonNull View itemView) {
        super(itemView);
        postMedia = itemView.findViewById(R.id.postMedia);
        progressBar = itemView.findViewById(R.id.progressBar);
        volumeControl = itemView.findViewById(R.id.volumeControl);

        imagePost = itemView.findViewById(R.id.imagePost);
        multimediaSlider = itemView.findViewById(R.id.multimediaSlider);
        share = itemView.findViewById(R.id.share);

        userName = itemView.findViewById(R.id.userName);
        descrProject = itemView.findViewById(R.id.descrProject);

        share.setOnClickListener(this::shareClick);
    }

    private void shareClick(View v) {
        Toast.makeText(v.getContext(), "click share", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint({"UnsafeOptInUsageError", "UseCompatLoadingForDrawables"})
    public void bind(MediaDetail mediaDetail, Profile profile, List<ItemPlayerView> playerViewList, int position) {
        userName.setText(profile.getUserName());
        descrProject.setText(mediaDetail.getOverview());

        List<MediaContent> contentList = mediaDetail.getContent();

        if (mediaDetail.getArray() == 1) {
            String contentType = mediaDetail.getContent().get(CURRENT_ITEM).getContentType();
            String id = mediaDetail.getContent().get(CURRENT_ITEM).get_id();
            String url = URL + URL_PART + id;
            int itemPosition = getBindingAdapterPosition();

            switch (contentType) {
                case VIDEO_MP4 -> {
                    imagePost.setVisibility(View.INVISIBLE);
                    multimediaSlider.setVisibility(View.INVISIBLE);
                    var player = postMedia.getPlayer();

                    if (player == null) {
                        MediaPlayerService.Companion.initPlayer(postMedia.getContext(), url, CURRENT_ITEM, itemPosition, false, postMedia, progressBar);
                        postMedia.setOnClickListener(new OnPlayerViewClickListenerService(contentList, itemPosition, CURRENT_ITEM, MP4));

                        itemPlayerView = new ItemPlayerView(itemPosition, CURRENT_ITEM, postMedia);
                        playerViewList.add(itemPlayerView);
                    }
                }
                case AUDIO_MP3 -> {
                    imagePost.setVisibility(View.INVISIBLE);
                    multimediaSlider.setVisibility(View.INVISIBLE);
                    postMedia.setArtworkDisplayMode(PlayerView.ARTWORK_DISPLAY_MODE_FIT);
                    postMedia.setDefaultArtwork(postMedia.getContext().getDrawable(R.drawable.ic_audio_96));
                    var player = postMedia.getPlayer();

                    if (player == null) {
                        MediaPlayerService.Companion.initPlayer(postMedia.getContext(), url, CURRENT_ITEM, itemPosition, false, postMedia, progressBar);
                        postMedia.setOnClickListener(new OnPlayerViewClickListenerService(contentList, itemPosition, CURRENT_ITEM, MP3));

                        itemPlayerView = new ItemPlayerView(itemPosition, CURRENT_ITEM, postMedia);
                        playerViewList.add(itemPlayerView);
                    }
                }
                case IMG_JPG, IMG_JPEG, IMG_PNG -> {
                    imagePost.setVisibility(View.VISIBLE);
                    Drawable drawable = imagePost.getDrawable();

                    if (drawable instanceof GifDrawable) {
                        imagePost.setImageDrawable(null);
                        loadImage("BITMAP", url, imagePost.getContext(), imagePost);
                    } else if (drawable == null) {
                        loadImage("BITMAP", url, imagePost.getContext(), imagePost);
                    }
                    imagePost.setOnClickListener(new OnImageViewClickListenerService(contentList, position));
                }
                case IMG_GIF -> {
                    imagePost.setVisibility(View.VISIBLE);

                    Drawable drawable = imagePost.getDrawable();
                    if (drawable instanceof BitmapDrawable) {
                        imagePost.setImageBitmap(null);
                        loadImage("GIF", url, imagePost.getContext(), imagePost);
                    } else if (drawable == null) {
                        loadImage("GIF", url, imagePost.getContext(), imagePost);
                    }
                    imagePost.setOnClickListener(new OnImageViewClickListenerService(contentList, position));
                }
            }
        } else {
            multimediaList = new ArrayList<>();
            mediaDetail.getContent().forEach(post -> multimediaList.add(new Multimedia(post.get_id(), post.getContentType(), URL + URL_PART + post.get_id(), mediaDetail.getOverview())));
            int itemPosition = getBindingAdapterPosition();
            imagePost.setImageResource(0);
            multimediaSlider.setVisibility(View.VISIBLE);

            multimediaSlider.setMediaList(multimediaList, itemPosition);
            multimediaSlider.setItemClickListener(new OpenDialogSliderClickListenerService(multimediaSlider.getContext(), multimediaList));
        }
    }
}
