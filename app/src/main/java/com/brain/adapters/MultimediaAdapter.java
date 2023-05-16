package com.brain.adapters;

import static com.brain.util.Util.getDecoded;

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
import com.brain.model.MediaDetail;
import com.brain.model.Result;
import com.brain.model.Video;
import com.brain.service.OnImageSliderClickListener;
import com.brain.service.OnImageViewClickListenerService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class MultimediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private static final AtomicLong autID = new AtomicLong();
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private String errorMsg;

    protected Context context;
    protected ArrayList<SlideModel> slideModelList;
    protected ArrayList<Video> videoList;
    protected List<Result> arrMultimediaResult;

    protected Video modelVideo;

    public MultimediaAdapter(Context context) {
        this.context = context;
        this.arrMultimediaResult = new ArrayList<>();
    }

    public List<Result> getMultimediaResult() {
        return this.arrMultimediaResult;
    }

    public void setMultimediaResult(List<Result> arrMultimediaResult) {
        this.arrMultimediaResult = arrMultimediaResult;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        slideModelList = new ArrayList<>();
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case VIEW_TYPE_ITEM:
                View viewItem = inflater.inflate(R.layout.container_cards, viewGroup, false);
                viewHolder = new MultimediaViewHolder(viewItem);
                break;
            case VIEW_TYPE_LOADING:
                View viewLoading = inflater.inflate(R.layout.footer_loading, viewGroup, false);
                viewHolder = new ProgressViewHolder(viewLoading);
                break;
        }
        assert viewHolder != null;
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Result result = getMultimediaResult().get(position);

        switch (getItemViewType(position)) {
            case VIEW_TYPE_ITEM:
                final MultimediaViewHolder multimediaViewHolder = (MultimediaViewHolder) holder;
                multimediaViewHolder.userName.setText(result.getUserName());
                multimediaViewHolder.visits.setText(String.valueOf(23));
                multimediaViewHolder.description.setText(result.getOverview());

                List<MediaDetail> arrMediaDetail = result.getPosterPath();
                if (arrMediaDetail.size() == 1) {
                    String data = arrMediaDetail.stream().map(model -> model.getBinaryContent().getData()).findFirst().orElse("Image not found");
                    loadImage(getDecoded(data)).into(multimediaViewHolder.imagePost);

                    multimediaViewHolder.imagePost.setOnClickListener(new OnImageViewClickListenerService(arrMediaDetail, position));
                } else {
                    arrMediaDetail.forEach(mediaDetail -> {
                        String data = mediaDetail.getBinaryContent().getData();
                        slideModelList.add(new SlideModel(autID.incrementAndGet(), getDecoded(data), mediaDetail.getImageName(), ScaleTypes.CENTER_CROP));

                        multimediaViewHolder.imageSlider.setImageList(slideModelList);
                        multimediaViewHolder.imageSlider.setItemClickListener(new OnImageSliderClickListener(context, slideModelList));
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
        return arrMultimediaResult == null ? 0 : arrMultimediaResult.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == arrMultimediaResult.size() - 1 && isLoadingAdded) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void add(Result result) {
        arrMultimediaResult.add(result);
        notifyItemInserted(arrMultimediaResult.size() - 1);
    }

    public void addAll(List<Result> moveResults) {
        for (Result result : moveResults) {
            add(result);
        }
    }

    public void remove(Result result) {
        int position = arrMultimediaResult.indexOf(result);
        if (position > -1) {
            arrMultimediaResult.remove(position);
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
        add(new Result());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = arrMultimediaResult.size() - 1;
        Result result = getItem(position);

        if (result != null) {
            arrMultimediaResult.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Result getItem(int position) {
        return arrMultimediaResult.get(position);
    }

    /**
     * displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(arrMultimediaResult.size() - 1);
        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    private RequestBuilder<Drawable> loadImage(@NonNull byte[] posterPath) {
        return Glide.with(context).load(posterPath).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).listener(new RequestListener<>() {
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
