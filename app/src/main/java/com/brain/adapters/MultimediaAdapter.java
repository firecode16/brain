package com.brain.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.brain.R;
import com.brain.holders.MultimediaViewHolder;
import com.brain.holders.ProgressViewHolder;
import com.brain.model.MediaDetail;
import com.brain.model.Profile;
import com.brain.multimediaplayer.service.MediaPlayerService;
import com.brain.multimediaslider.model.ItemPlayerView;

import java.util.ArrayList;
import java.util.List;

public class MultimediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private String errorMsg;

    List<MediaDetail> mediaDetailList;
    List<ItemPlayerView> playerViewList;

    Profile profile;

    private MultimediaViewHolder multimediaHolder;

    public MultimediaAdapter() {
        this.mediaDetailList = new ArrayList<>();
        this.playerViewList = new ArrayList<>();
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

    public List<ItemPlayerView> getPlayerList() {
        return playerViewList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.container_cards, viewGroup, false);
            multimediaHolder = new MultimediaViewHolder(view);
            viewHolder = multimediaHolder;
        } else if (viewType == VIEW_TYPE_LOADING) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.footer_loading, viewGroup, false);
            viewHolder = new ProgressViewHolder(view);
        }

        assert viewHolder != null;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_ITEM:
                MediaDetail mediaDetail = getMediaDetailList().get(position);
                multimediaHolder = (MultimediaViewHolder) holder;
                multimediaHolder.bind(mediaDetail, getProfile(), playerViewList, position);
                break;
            case VIEW_TYPE_LOADING:
                ((ProgressViewHolder) holder).bind(retryPageLoad, errorMsg);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return (mediaDetailList == null || mediaDetailList.isEmpty()) ? 0 : mediaDetailList.size();
    }

    @Override
    public long getItemId(int position) {
        Long collectionId = mediaDetailList.get(position).getCollectionId();
        if (collectionId != null) {
            return collectionId;
        }
        return 0L;
    }

    @Override
    public int getItemViewType(int position) {
        int dataSize = mediaDetailList.size() - 1;
        if (position == dataSize && isLoadingAdded) {
            return VIEW_TYPE_LOADING;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    private void add(MediaDetail mediaDetail) {
        mediaDetailList.add(mediaDetail);
        int position = mediaDetailList.size() - 1;
        notifyItemInserted(position);
    }

    public void addAll(List<MediaDetail> mediaDetail) {
        for (MediaDetail post : mediaDetail) {
            add(post);
        }
    }

    public void addProfile(Profile objProfile) {
        setProfile(objProfile);
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

    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(mediaDetailList.size() - 1);
        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    public void callAndExecuteSelectedItem(int itemPosition, int position) {
        MediaPlayerService.Companion.pauseCurrentPlayingVideo();

        playerViewList.forEach(item -> {
            if (item.getItemPosition() == itemPosition) {
                if (item.getPosition() != null && item.getPosition() == position) {
                    MediaPlayerService.Companion.prepareIndexesOfMultimediaWhenOpenDialog(item.getItemPosition(), item.getPosition(), item.getPlayerView());
                    item.getPlayerView().setUseController(false);
                    MediaPlayerService.Companion.resumePlayerIndexCurrent();
                }
            }
        });
    }
}
