package com.brain.adapters;

import static com.brain.util.Util.URL;
import static com.brain.util.Util.URL_PART;
import static com.brain.util.Util.VIDEO_MP4;
import static com.brain.util.Util.AUDIO_MP3;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.brain.R;
import com.brain.holders.MultimediaViewHolder;
import com.brain.holders.ProgressViewHolder;
import com.brain.model.MediaContent;
import com.brain.model.MediaDetail;
import com.brain.model.Profile;
import com.brain.model.Video;
import com.brain.multimediaslider.model.Multimedia;
import com.brain.service.OnImageSliderClickListener;
import com.brain.service.OnImageViewClickListenerService;
import com.brain.service.VideoPlayService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class MultimediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private static final AtomicLong autID = new AtomicLong();
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private String errorMsg;

    protected Context context;
    protected ArrayList<Multimedia> multimediaList;
    protected ArrayList<Video> videoList;
    protected List<MediaDetail> mediaDetailList;

    protected Profile profile;
    protected Video modelVideo;

    public MultimediaAdapter(Context context) {
        this.context = context;
        this.mediaDetailList = new ArrayList<>();
        this.profile = new Profile();
    }

    public List<MediaDetail> getMediaDetailList() {
        return mediaDetailList;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        multimediaList = new ArrayList<>();
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        viewHolder = switch (viewType) {
            case VIEW_TYPE_ITEM -> {
                View viewItem = inflater.inflate(R.layout.container_cards, viewGroup, false);
                yield new MultimediaViewHolder(viewItem);
            }
            case VIEW_TYPE_LOADING -> {
                View viewLoading = inflater.inflate(R.layout.footer_loading, viewGroup, false);
                yield new ProgressViewHolder(viewLoading);
            }
            default -> viewHolder;
        };
        assert viewHolder != null;
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MediaDetail mediaDetail = getMediaDetailList().get(position);

        switch (getItemViewType(position)) {
            case VIEW_TYPE_ITEM:
                final MultimediaViewHolder multimediaViewHolder = (MultimediaViewHolder) holder;
                multimediaViewHolder.userName.setText(getProfile().getUserName());

                List<MediaContent> contentList = mediaDetail.getContent();

                if (contentList.size() == 1) {
                    String contentType = Objects.requireNonNull(mediaDetail.getContent().stream().findFirst().orElse(null)).getContentType();
                    String id = Objects.requireNonNull(mediaDetail.getContent().stream().findFirst().orElse(null)).get_id();

                    if (contentType.equals(VIDEO_MP4) || contentType.equals(AUDIO_MP3)) {
                        multimediaViewHolder.progressBar.setVisibility(View.VISIBLE);
                        multimediaViewHolder.volumeControl.setVisibility(View.VISIBLE);
                        multimediaViewHolder.postMedia.setVisibility(View.VISIBLE);
                        int itemIndex = multimediaViewHolder.getBindingAdapterPosition();

                        VideoPlayService.Companion.initPlayer(context, URL + URL_PART + id, itemIndex, false, multimediaViewHolder);
                    } else {
                        loadImage(URL + URL_PART + id).into(multimediaViewHolder.imagePost);
                        multimediaViewHolder.imagePost.setOnClickListener(new OnImageViewClickListenerService(contentList, position));
                    }
                } else {
                    mediaDetail.getContent().forEach(post -> {
                        multimediaList.add(new Multimedia(post.get_id(), post.getContentType(), URL + URL_PART + post.get_id(), mediaDetail.getOverview()));
                        multimediaViewHolder.multimediaSlider.setMediaList(multimediaList);
                        multimediaViewHolder.multimediaSlider.setItemClickListener(new OnImageSliderClickListener(context, multimediaList));
                    });
                }
                break;
            case VIEW_TYPE_LOADING:
                final ProgressViewHolder progressViewHolder = (ProgressViewHolder) holder;

                if (retryPageLoad) {
                    progressViewHolder.errorLayout.setVisibility(View.VISIBLE);
                    progressViewHolder.progressBar.setVisibility(View.GONE);
                    progressViewHolder.errorTxt.setText(errorMsg != null ? errorMsg : context.getString(R.string.error_msg_unknown));
                } else {
                    progressViewHolder.errorLayout.setVisibility(View.GONE);
                    progressViewHolder.progressBar.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return (mediaDetailList == null || mediaDetailList.isEmpty()) ? 0 : mediaDetailList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mediaDetailList.size() - 1 && isLoadingAdded) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private void add(final MediaDetail mediaDetail) {
        mediaDetailList.add(mediaDetail);
        notifyItemInserted(mediaDetailList.size() - 1);
    }

    public void addAll(List<MediaDetail> mediaDetail) {
        for (MediaDetail post : mediaDetail) {
            add(post);
        }
    }

    public void addProfile(Profile objProfile) {
        setProfile(objProfile);
    }

    public void remove(MediaDetail mediaDetail) {
        int position = mediaDetailList.indexOf(mediaDetail);
        if (position > -1) {
            mediaDetailList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new MediaDetail());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = mediaDetailList.size() - 1;
        MediaDetail post = getItem(position);

        if (post != null) {
            mediaDetailList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public MediaDetail getItem(int position) {
        return mediaDetailList.get(position);
    }

    /**
     * displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(mediaDetailList.size() - 1);
        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    private RequestBuilder<Drawable> loadImage(@NonNull String url) {
        return Glide.with(context).load(url).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).listener(new RequestListener<>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        });
    }
}
