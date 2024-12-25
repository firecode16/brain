package com.brain.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.brain.R;
import com.brain.model.Poster;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * DELETE THIS CLASS
 */
public class ViewImageSliderPagerAdapter extends PagerAdapter {
    LayoutInflater layoutInflater;
    List<Poster> posterList;
    Context context;
    ImageView imageViewPreview;
    boolean isSingle;
    Poster model;

    public ViewImageSliderPagerAdapter(List<Poster> posterList, Context context, boolean isSingle) {
        this.posterList = posterList;
        this.context = context;
        this.isSingle = isSingle;
    }

    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null; //layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

        imageViewPreview = null; //view.findViewById(R.id.imagePreview);
        if (isSingle) {
            model = posterList.get(0);
        } else {
            model = posterList.get(position);
        }
        Glide.with(context).load(model.getId()).thumbnail(0.5f).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageViewPreview);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return posterList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
