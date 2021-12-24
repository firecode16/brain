package com.brain.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brain.R;
import com.brain.adapters.AnimeAdapter;
import com.brain.adapters.UserAdapter;
import com.brain.holders.AnimeViewHolder;
import com.brain.holders.UserViewHolder;
import com.brain.model.Anime;
import com.brain.model.User;

import java.util.ArrayList;
import java.util.List;

public class GenericFragment extends Fragment {
    protected RecyclerView recycler;
    protected RecyclerView.LayoutManager recyclerManager;

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
                recyclerManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(recyclerManager);

                final ArrayList<Anime> singleImage1 = new ArrayList<>();
                singleImage1.add(new Anime(15243L, "Jorge Sanchez", "jorge@brain.com", R.drawable.civil_war, "Civil War 2021", 2060));

                ArrayList<Anime> singleImage2 = new ArrayList<>();
                singleImage2.add(new Anime(25354L, "Jorge Sanchez", "jorge@brain.com", R.drawable.casa2, "Casa moderna EUA", 321));

                ArrayList<Anime> singleImage3 = new ArrayList<>();
                singleImage3.add(new Anime(36465L, "Ernesto Jr", "ernesto@brain.com", R.drawable.bvs, "Batman vs Superman 2022", 3080));

                ArrayList<Anime> multiImages = new ArrayList<>();
                multiImages.add(new Anime(47586L, "Ernesto Jr", "ernesto@brain.com", R.drawable.alcarbon, "Platanos a la Carbonera", 257));
                multiImages.add(new Anime(58697L, "Ernesto Jr", "ernesto@brain.com", R.drawable.casa4, "Casa de Campo, Ver", 587));
                multiImages.add(new Anime(69788L, "Ernesto Jr", "ernesto@brain.com", R.drawable.bourne, "Bourne 2016", 954));
                multiImages.add(new Anime(75342L, "Ernesto Jr", "ernesto@brain.com", R.drawable.doctor, "Doctor Strange 2021", 954));
                multiImages.add(new Anime(94531L, "Ernesto Jr", "ernesto@brain.com", R.drawable.matrix, "Matrix 2021", 954));

                final ArrayList<ArrayList<Anime>> objectMatrix = new ArrayList<>();
                objectMatrix.add(singleImage1);
                objectMatrix.add(singleImage2);
                objectMatrix.add(singleImage3);
                objectMatrix.add(multiImages);

                RecyclerView.Adapter<AnimeViewHolder> animeViewHolderAdapter = new AnimeAdapter(getContext(), objectMatrix);

                recyclerView.setHasFixedSize(true);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(animeViewHolderAdapter);
                break;
            case 2:
                recyclerManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(recyclerManager);

                List<User> userItems = new ArrayList<>();
                userItems.add(new User("Fredi Hdz", "codefire@github.com"));

                RecyclerView.Adapter<UserViewHolder> userViewHolderAdapter = new UserAdapter(getActivity(), userItems);
                recyclerView.setAdapter(userViewHolderAdapter);
                break;
            case 3:
                break;
        }
    }

}
