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
    List<ItemPlayerView> playerViewList = new ArrayList<>();

    Profile profile;

    public MultimediaAdapter() {
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
        RecyclerView.ViewHolder viewHolder;
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
            default -> null;
        };
        assert viewHolder != null;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_ITEM:
                MediaDetail mediaDetail = getMediaDetailList().get(position);
                ((MultimediaViewHolder) holder).bind(mediaDetail, getProfile(), playerViewList, position);
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
        return mediaDetailList.get(position).getCollectionId();
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
