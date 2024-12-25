package com.brain.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.brain.R;
import com.brain.adapters.ViewImageSliderPagerAdapter;
import com.brain.model.Poster;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * DELETE THIS CLASS
 */
public class ImageSliderDialogFragment extends DialogFragment {
    ViewPager viewPager;
    ViewImageSliderPagerAdapter sliderPagerAdapter;
    ArrayList<Poster> posterArrayList;
    TextView lblCount, lblTitle, lblDate;
    int selectedPosition = 0;
    boolean isSingle;
    Poster model;
    Toolbar toolbar;

    public static ImageSliderDialogFragment newInstance() {
        return new ImageSliderDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_slider, container, false);
        viewPager = view.findViewById(R.id.viewPager);
        lblCount = view.findViewById(R.id.lbl_count);
        lblTitle = view.findViewById(R.id.myTitle);
        lblDate = view.findViewById(R.id.date);
        toolbar = view.findViewById(R.id.imgToolbar);

        assert getArguments() != null;
        //posterArrayList = getArguments().getParcelableArrayList("arrParcelableImages");
        selectedPosition = getArguments().getInt("position");
        isSingle = getArguments().getBoolean("isSingle");

        sliderPagerAdapter = new ViewImageSliderPagerAdapter(posterArrayList, view.getContext(), isSingle);
        viewPager.setAdapter(sliderPagerAdapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);

        setCurrentItem(selectedPosition);
        return view;
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition, isSingle);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void displayMetaInfo(int position, boolean isSingle) {
        Date currentTime = Calendar.getInstance().getTime();
        if (isSingle) {
            model = posterArrayList.get(0);
        } else {
            model = posterArrayList.get(position);
            lblCount.setText((position + 1) + " of " + posterArrayList.size());
        }

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        lblTitle.setText(model.getDescriptionFooter());
        lblDate.setText(currentTime.toString());
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position, isSingle);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };
}
