package com.brain.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.brain.R;
import com.brain.model.Anime;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class ViewImageSliderPagerAdapter extends PagerAdapter {
    LayoutInflater layoutInflater;
    ArrayList<Anime> animeArrayList;
    Context context;
    ImageView imageViewPreview;
    boolean isSingle;
    Anime model;

    public ViewImageSliderPagerAdapter(ArrayList<Anime> animeArrayList, Context context, boolean isSingle) {
        this.animeArrayList = animeArrayList;
        this.context = context;
        this.isSingle = isSingle;
    }

    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

        imageViewPreview = view.findViewById(R.id.imagePreview);
        if (isSingle) {
            model = animeArrayList.get(0);
        } else {
            model = animeArrayList.get(position);
        }
        Glide.with(context).load(model.getImage()).thumbnail(0.5f).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageViewPreview);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return animeArrayList.size();
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
