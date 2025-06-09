package com.brain.holders;

import static com.brain.util.Util.AUDIO_MP3;
import static com.brain.util.Util.BASE_URL;
import static com.brain.util.Util.IMG_GIF;
import static com.brain.util.Util.IMG_JPEG;
import static com.brain.util.Util.IMG_JPG;
import static com.brain.util.Util.IMG_PNG;
import static com.brain.util.Util.MP3;
import static com.brain.util.Util.MP4;
import static com.brain.util.Util.URL_PART;
import static com.brain.util.Util.VIDEO_MP4;
import static com.brain.util.Util.loadImage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

/**
 * @author brain30316@gmail.com
 *
 */
public class MultimediaViewHolder extends RecyclerView.ViewHolder {
    PlayerView postMedia;
    ProgressBar progressBar;
    ImageView volumeControl;

    ImageView imagePost;
    public MultimediaSlider multimediaSlider;
    ImageView share;
    ImageView icLike;
    LinearLayout like;
    LinearLayout emojiContainer;
    Handler handler = new Handler(Looper.getMainLooper());
    private final Handler autoDismissHandler = new Handler();
    private final Runnable autoDismissRunnable = this::hideEmojiContainerAnimated;

    Boolean isLongPress = false;

    TextView userName;
    TextView descrPost;

    private static final int CURRENT_ITEM = 0;
    ItemPlayerView itemPlayerView;
    List<Multimedia> multimediaList;

    @SuppressLint("ClickableViewAccessibility")
    public MultimediaViewHolder(@NonNull View itemView) {
        super(itemView);
        postMedia = itemView.findViewById(R.id.postMedia);
        progressBar = itemView.findViewById(R.id.progressBar);
        volumeControl = itemView.findViewById(R.id.volumeControl);

        imagePost = itemView.findViewById(R.id.imagePost);
        multimediaSlider = itemView.findViewById(R.id.multimediaSlider);
        share = itemView.findViewById(R.id.share);
        like = itemView.findViewById(R.id.ctrlLike);
        icLike = itemView.findViewById(R.id.icLike);
        emojiContainer = itemView.findViewById(R.id.emojiContainer);

        userName = itemView.findViewById(R.id.userName);
        descrPost = itemView.findViewById(R.id.descrPost);

        share.setOnClickListener(this::shareClick);
        like.setOnTouchListener(this::likeClick);

        itemView.findViewById(R.id.thumbsUp).setOnClickListener(evt -> onEmojiSelected("\uD83D\uDC4D"));
        itemView.findViewById(R.id.thumbsDown).setOnClickListener(evt -> onEmojiSelected("\uD83D\uDC4E"));
        itemView.findViewById(R.id.rocket).setOnClickListener(evt -> onEmojiSelected("\uD83D\uDE80"));
    }

    private boolean likeClick(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN -> {
                isLongPress = false;
                handler.postDelayed(() -> {
                    isLongPress = true;
                    showEmojiContainer();
                }, 300);
                return true;
            }
            case MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                handler.removeCallbacksAndMessages(null);
                if (!isLongPress) {
                    Toast.makeText(v.getContext(), "Like enviado", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        }
        return false;
    }

    private void onEmojiSelected(String reaction) {
        autoDismissHandler.removeCallbacks(autoDismissRunnable); // Cancel auto-dismiss
        hideEmojiContainerAnimated();
        Toast.makeText(emojiContainer.getContext(), reaction, Toast.LENGTH_SHORT).show();
    }

    private void showEmojiContainer() {
        emojiContainer.setVisibility(View.VISIBLE);
        for (int i = 0; i < emojiContainer.getChildCount(); i++) {
            View emoji = emojiContainer.getChildAt(i);
            emoji.setScaleX(0f);
            emoji.setScaleY(0f);
            emoji.setAlpha(1f);
            emoji.setTranslationY(50f);

            // Scaled
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(emoji, View.SCALE_X, 1.2f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(emoji, View.SCALE_Y, 1.2f, 1f);
            // Floating emojis Up
            ObjectAnimator moveUp = ObjectAnimator.ofFloat(emoji, View.TRANSLATION_Y, 0f);

            AnimatorSet set = new AnimatorSet();
            set.setStartDelay(i * 100L); // chain effect
            set.setDuration(300);
            set.playTogether(scaleX, scaleY, moveUp);
            set.start();
        }

        // Auto-dismiss before 3 sec.
        autoDismissHandler.postDelayed(autoDismissRunnable, 3000);
    }

    private void hideEmojiContainerAnimated() {
        for (int i = 0; i < emojiContainer.getChildCount(); i++) {
            View emoji = emojiContainer.getChildAt(i);

            ObjectAnimator fadeOut = ObjectAnimator.ofFloat(emoji, View.ALPHA, 0f);
            fadeOut.setDuration(200);
            fadeOut.setStartDelay(i * 50L);

            if (i == emojiContainer.getChildCount() - 1) {
                fadeOut.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        emojiContainer.setVisibility(View.GONE);
                        for (int j = 0; j < emojiContainer.getChildCount(); j++) {
                            emojiContainer.getChildAt(j).setAlpha(1f);
                        }
                    }
                });
            }

            fadeOut.start();
        }
    }

    private void shareClick(View v) {
        Toast.makeText(v.getContext(), "click share", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint({"UnsafeOptInUsageError", "UseCompatLoadingForDrawables"})
    public void bind(MediaDetail mediaDetail, Profile profile, List<ItemPlayerView> playerViewList, int position) {
        userName.setText(profile.getFullName());
        descrPost.setText(mediaDetail.getOverview());

        List<MediaContent> contentList = mediaDetail.getContent();

        if (mediaDetail.getArray() == 1) {
            String contentType = mediaDetail.getContent().get(CURRENT_ITEM).getContentType();
            String id = mediaDetail.getContent().get(CURRENT_ITEM).get_id();
            String url = BASE_URL + URL_PART + id;
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
            mediaDetail.getContent().forEach(post -> multimediaList.add(new Multimedia(post.get_id(), post.getContentType(), BASE_URL + URL_PART + post.get_id(), mediaDetail.getOverview())));
            int itemPosition = getBindingAdapterPosition();
            imagePost.setImageResource(0);
            multimediaSlider.setVisibility(View.VISIBLE);

            multimediaSlider.setMediaList(multimediaList, itemPosition);
            multimediaSlider.setItemClickListener(new OpenDialogSliderClickListenerService(multimediaSlider.getContext(), multimediaList));
        }
    }
}
