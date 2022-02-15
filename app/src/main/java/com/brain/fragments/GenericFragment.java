package com.brain.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brain.R;
import com.brain.adapters.MultimediaAdapter;
import com.brain.adapters.UserAdapter;
import com.brain.holders.MultimediaViewHolder;
import com.brain.holders.UserViewHolder;
import com.brain.model.Poster;
import com.brain.model.User;
import com.brain.model.Video;

import java.util.ArrayList;
import java.util.List;

public class GenericFragment extends Fragment {
    protected RecyclerView recycler;
    protected RecyclerView.LayoutManager layoutManager;

    private static final String SECTION_FRAGMENT_NUMBER = "section_fragment_number";

    public GenericFragment() {
    }

    public static GenericFragment newInstance(int sectionNumber) {
        GenericFragment fragment = new GenericFragment();
        Bundle args = new Bundle();
        args.putInt(SECTION_FRAGMENT_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view, container, false);
        recycler = rootView.findViewById(R.id.recyclerView);
        setUpFragmentView(recycler);
        return rootView;
    }

    private void setUpFragmentView(final RecyclerView recyclerView) {
        assert getArguments() != null;
        int sectionFragmentNumber = getArguments().getInt(SECTION_FRAGMENT_NUMBER);

        switch (sectionFragmentNumber) {
            case 1:
                layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);

                ArrayList<Poster> singleImage1 = new ArrayList<>();
                singleImage1.add(new Poster(15243L, "Jorge Sanchez", "jorge@brain.com", R.drawable.civil_war, "Civil War 2021", 2060));

                ArrayList<Poster> singleImage2 = new ArrayList<>();
                singleImage2.add(new Poster(25354L, "Jorge Sanchez", "jorge@brain.com", R.drawable.casa_campo, "Casa moderna EUA", 321));

                ArrayList<Poster> singleImage3 = new ArrayList<>();
                singleImage3.add(new Poster(36465L, "Ernesto Jr", "ernesto@brain.com", R.drawable.bvs, "Batman vs Superman 2022", 3080));

                ArrayList<Poster> multiImages = new ArrayList<>();
                multiImages.add(new Poster(47586L, "Ernesto Jr", "ernesto@brain.com", R.drawable.alcarbon, "Platanos a la Carbonera", 257));
                multiImages.add(new Poster(58697L, "Ernesto Jr", "ernesto@brain.com", R.drawable.casa_campo_2, "Casa de Campo, Ver", 587));
                multiImages.add(new Poster(69788L, "Ernesto Jr", "ernesto@brain.com", R.drawable.bourne, "Bourne 2016", 954));
                multiImages.add(new Poster(75342L, "Ernesto Jr", "ernesto@brain.com", R.drawable.divertido, "Meme Syntax Error", 954));
                multiImages.add(new Poster(94531L, "Ernesto Jr", "ernesto@brain.com", R.drawable.matrix, "Matrix 2021", 954));

                ArrayList<Poster> singleImage4 = new ArrayList<>();
                singleImage4.add(new Poster(54421L, "Eliza Cardenas", "eliza@brain.com", R.drawable.omero, "Meme Omero", 7020));

                ArrayList<Video> singleVideo1 = new ArrayList<>();
                singleVideo1.add(new Video(18372L, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", "Eithan Hdz", "eithan@brain.com", "Alan Walker - Darkside", 1234));

                final ArrayList<Object> objectMatrix = new ArrayList<>();
                objectMatrix.add(singleImage1);
                objectMatrix.add(singleImage2);
                objectMatrix.add(singleImage3);
                objectMatrix.add(multiImages);
                objectMatrix.add(singleImage4);
                objectMatrix.add(singleVideo1);

                RecyclerView.Adapter<MultimediaViewHolder> animeViewHolderAdapter = new MultimediaAdapter(getContext(), objectMatrix, recyclerView);

                recyclerView.setHasFixedSize(true);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(animeViewHolderAdapter);
                break;
            case 2:
                layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);

                List<User> userItems = new ArrayList<>();
                userItems.add(new User("Fredi Hdz", "codefire@github.com"));

                RecyclerView.Adapter<UserViewHolder> userViewHolderAdapter = new UserAdapter(getActivity(), userItems);
                recyclerView.setAdapter(userViewHolderAdapter);
                break;
            case 3:
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //VideoPlayerMethodService.releaseAllPlayers();
        Log.d("onPause()::: ", "pause...: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("onResume()::: ", "Reanudar...: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("onDestroy()::: ", "destroy...: ");
    }
}
