package com.brain.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.brain.R;
import com.brain.holders.MultimediaViewHolder;
import com.brain.holders.ProgressViewHolder;
import com.brain.model.Poster;
import com.brain.model.Result;
import com.brain.model.Video;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

public class MultimediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w200";

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private String errorMsg;

    protected Context context;
    protected ArrayList<SlideModel> slideModelList;
    protected ArrayList<Object> objectMatrix;
    protected ArrayList<Poster> postersList;
    protected ArrayList<Video> videoList;
    protected List<Result> movieResults;

    protected Poster modelPoster;
    protected Video modelVideo;

    public MultimediaAdapter(Context context) {
        this.context = context;
        this.movieResults = new ArrayList<>();
    }

    public List<Result> getMovies() {
        return this.movieResults;
    }

    public void setMovies(List<Result> movieResults) {
        this.movieResults = movieResults;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
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

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Result result = getMovies().get(position);

        switch (getItemViewType(position)) {
            case VIEW_TYPE_ITEM:
                final MultimediaViewHolder multimediaViewHolder = (MultimediaViewHolder) holder;
                multimediaViewHolder.userName.setText(result.getTitle());
                multimediaViewHolder.visits.setText(String.valueOf(result.getVoteCount()));
                multimediaViewHolder.description.setText(result.getOverview());

                loadImage(result.getPosterPath()).into(multimediaViewHolder.imagePost);
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

        /*if (objectMatrix.size() > 0) {
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
            }
        }*/
    }

    @Override
    public int getItemCount() {
        return movieResults == null ? 0 : movieResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == movieResults.size() - 1 && isLoadingAdded) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void add(Result result) {
        movieResults.add(result);
        notifyItemInserted(movieResults.size() - 1);
    }

    public void addAll(List<Result> moveResults) {
        for (Result result : moveResults) {
            add(result);
        }
    }

    public void remove(Result result) {
        int position = movieResults.indexOf(result);
        if (position > -1) {
            movieResults.remove(position);
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

        int position = movieResults.size() - 1;
        Result result = getItem(position);

        if (result != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Result getItem(int position) {
        return movieResults.get(position);
    }

    /**
     * displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(movieResults.size() - 1);
        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    private RequestBuilder<Drawable> loadImage(@NonNull String posterPath) {
        return Glide.with(context).load(BASE_URL_IMG + posterPath).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).listener(new RequestListener<Drawable>() {
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
