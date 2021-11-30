package com.brain.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brain.R;
import com.brain.adapters.AnimeAdapter;
import com.brain.adapters.UserAdapter;
import com.brain.model.Anime;
import com.brain.holders.AnimeViewHolder;
import com.brain.model.User;
import com.brain.holders.UserViewHolder;

import java.util.ArrayList;
import java.util.List;

public class GenericFragment extends Fragment {

    protected RecyclerView recycler;
    protected RecyclerView.LayoutManager manager;

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
        View rootView = inflater.inflate(R.layout.container_recycler, container, false);

        recycler = (RecyclerView) rootView.findViewById(R.id.containerRecycler);
        recycler.setHasFixedSize(true);

        manager = new LinearLayoutManager(this.getContext());
        recycler.setLayoutManager(manager);

        setUpFragmentView(recycler);
        return rootView;
    }

    private void setUpFragmentView(final RecyclerView recyclerView) {
        assert getArguments() != null;
        int sectionFragmentNumber = getArguments().getInt(SECTION_FRAGMENT_NUMBER);

        switch (sectionFragmentNumber) {
            case 1:
                List<Anime> animeItems = new ArrayList<>();
                animeItems.add(new Anime(R.drawable.casa1, "Casa moderna EUA", 230));
                animeItems.add(new Anime(R.drawable.casa2, "Los coyotes Ver", 456));
                animeItems.add(new Anime(R.drawable.casa3, "Fate Stay Night", 342));
                animeItems.add(new Anime(R.drawable.casa4, "Casa de Campo Ver", 590));

                RecyclerView.Adapter<AnimeViewHolder> animeViewHolderAdapter = new AnimeAdapter(getActivity(), animeItems);
                recyclerView.setAdapter(animeViewHolderAdapter);
                break;
            case 2:
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
