package com.brain.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.brain.R;
import com.brain.model.Anime;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SlideshowDialogFragment extends DialogFragment {
    ViewPager viewPager;
    ArrayList<Anime> animeArrayList;
    TextView lblCount, lblTitle, lblDate;
    int selectedPosition = 0;
    SetupViewPagerAdapter setupViewPagerAdapter;

    public static SlideshowDialogFragment newInstance() {
        return new SlideshowDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_slider, container, false);
        viewPager = view.findViewById(R.id.viewpager);
        lblCount = view.findViewById(R.id.lbl_count);
        lblTitle = view.findViewById(R.id.myTitle);
        lblDate = view.findViewById(R.id.date);

        assert getArguments() != null;
        animeArrayList = getArguments().getParcelableArrayList("arrParcelableImages");
        selectedPosition = getArguments().getInt("position");

        setupViewPagerAdapter = new SetupViewPagerAdapter();
        viewPager.setAdapter(setupViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);
        return view;
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    private void displayMetaInfo(int position) {
        Date currentTime = Calendar.getInstance().getTime();
        Anime model = animeArrayList.get(position);
        lblCount.setText((position + 1) + " of " + animeArrayList.size());
        lblTitle.setText(model.getName());
        lblDate.setText(currentTime.toString());
    }

    //page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    public class SetupViewPagerAdapter extends PagerAdapter {
        LayoutInflater layoutInflater;

        public SetupViewPagerAdapter() {
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            ImageView imageViewPreview = view.findViewById(R.id.imagePreview);
            Anime model = animeArrayList.get(position);

            Glide.with(requireActivity()).load(model.getImage()).thumbnail(0.5f).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageViewPreview);
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
}
