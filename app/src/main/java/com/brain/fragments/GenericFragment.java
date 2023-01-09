package com.brain.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.brain.R;
import com.brain.adapters.MultimediaAdapter;
import com.brain.adapters.UserAdapter;
import com.brain.api.ApiRest;
import com.brain.holders.UserViewHolder;
import com.brain.impl.ApiRestImpl;
import com.brain.impl.PaginationAdapterCallbackImpl;
import com.brain.model.Result;
import com.brain.model.TopRatedMovies;
import com.brain.model.User;
import com.brain.service.PaginationListenerService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenericFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, PaginationAdapterCallbackImpl {
    protected RecyclerView recycler;
    protected SwipeRefreshLayout swipeRefresh;
    protected LinearLayoutManager layoutManager;
    protected MultimediaAdapter multimediaAdapter;
    protected ProgressBar progressBar;
    protected LinearLayout errorLayout;
    protected Button btnRetry;
    protected TextView txtError;

    private static final String SECTION_FRAGMENT_NUMBER = "section_fragment_number";
    private static final int PAGE_START = 1;
    private static final int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;

    private ApiRestImpl apiRestImpl;

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
        progressBar = rootView.findViewById(R.id.beginProgress);
        errorLayout = rootView.findViewById(R.id.error_layout);
        btnRetry = rootView.findViewById(R.id.error_btn_retry);
        txtError = rootView.findViewById(R.id.error_txt_cause);
        swipeRefresh = rootView.findViewById(R.id.swipeRefresh);
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

                /*ArrayList<Poster> singleImage1 = new ArrayList<>();
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

                // https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4
                // http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4
                ArrayList<Video> singleVideo1 = new ArrayList<>();
                singleVideo1.add(new Video(18372L, "https://videos.pond5.com/clip-03mp4-footage-007745736_main_xxl.mp4", "Eithan Hdz", "eithan@brain.com", "demo 1", 1234));

                ArrayList<Video> singleVideo2 = new ArrayList<>();
                singleVideo2.add(new Video(29945L, "https://static.klliq.com/videos/QMWR5PxqxnnAILvO8iGB5ygvV47wxoDK_hd.mp4", "Jose Torres", "jose@brain.com", "demo 2", 1245));

                final ArrayList<Object> objectMatrix = new ArrayList<>();
                objectMatrix.add(singleVideo1);
                objectMatrix.add(singleImage1);
                objectMatrix.add(singleVideo2);
                objectMatrix.add(singleImage2);
                objectMatrix.add(singleImage3);
                objectMatrix.add(multiImages);
                objectMatrix.add(singleImage4);
                */

                //RecyclerView.Adapter<MultimediaViewHolder> animeViewHolderAdapter = new MultimediaAdapter(getContext(), objectMatrix, recyclerView);
                multimediaAdapter = new MultimediaAdapter(getContext());
                recyclerView.setHasFixedSize(false);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(multimediaAdapter);

                recyclerView.addOnScrollListener(new PaginationListenerService(layoutManager) {
                    @Override
                    protected void loadMoreItems() {
                        isLoading = true;
                        currentPage += 1;
                        loadNextPage();
                    }

                    @Override
                    public int getTotalPageCount() {
                        return TOTAL_PAGES;
                    }

                    @Override
                    public boolean isLastPage() {
                        return isLastPage;
                    }

                    @Override
                    public boolean isLoading() {
                        return isLoading;
                    }
                });

                // init service and load data
                apiRestImpl = ApiRest.getClient(getContext()).create(ApiRestImpl.class);
                loadFirstPage();

                btnRetry.setOnClickListener(v -> loadFirstPage());
                swipeRefresh.setOnRefreshListener(this::doRefresh);
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

    @SuppressLint("NotifyDataSetChanged")
    private void doRefresh() {
        progressBar.setVisibility(View.VISIBLE);
        if (callTopRatedMoviesApi().isExecuted()) {
            callTopRatedMoviesApi().cancel();
        }

        // check if data is stale.
        // execute network request if cache is expired; otherwise do not update data.
        multimediaAdapter.getMovies().clear();
        multimediaAdapter.notifyDataSetChanged();
        loadFirstPage();
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        currentPage = PAGE_START;
        isLastPage = false;
        multimediaAdapter.clear();
        loadFirstPage();
    }

    private List<Result> fetchResults(Response<TopRatedMovies> response) {
        TopRatedMovies topRatedMovies = response.body();
        assert topRatedMovies != null;
        return topRatedMovies.getResults();
    }

    private Call<TopRatedMovies> callTopRatedMoviesApi() {
        return apiRestImpl.getTopRatedMovies(getString(R.string.movies_api_key),"en_US", currentPage);
    }

    private void showErrorView(Throwable throwable) {
        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            txtError.setText(fetchErrorMessage(throwable));
        }
    }

    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }

    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    // Remember to add android.permission.ACCESS_NETWORK_STATE permission.
    private boolean isNetworkConnected() {
        @SuppressLint("ServiceCast") ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    @Override
    public void retryPageLoad() {
        loadNextPage();
    }

    private void loadFirstPage() {
        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();
        currentPage = PAGE_START;

        callTopRatedMoviesApi().enqueue(new Callback<TopRatedMovies>() {
            @Override
            public void onResponse(@NonNull Call<TopRatedMovies> call, @NonNull Response<TopRatedMovies> response) {
                hideErrorView();

                // Got data. Send it to adapter
                List<Result> results = fetchResults(response);
                progressBar.setVisibility(View.GONE);
                multimediaAdapter.addAll(results);

                if (currentPage <= TOTAL_PAGES) {
                    multimediaAdapter.addLoadingFooter();
                } else {
                    isLastPage = true;
                }
            }

            @Override
            public void onFailure(@NonNull Call<TopRatedMovies> call, @NonNull Throwable t) {
                t.getLocalizedMessage();
                showErrorView(t);
            }
        });
    }

    private void loadNextPage() {
        callTopRatedMoviesApi().enqueue(new Callback<TopRatedMovies>() {
            @Override
            public void onResponse(@NonNull Call<TopRatedMovies> call, @NonNull Response<TopRatedMovies> response) {
                multimediaAdapter.removeLoadingFooter();
                isLoading = false;

                List<Result> results = fetchResults(response);
                multimediaAdapter.addAll(results);

                if (currentPage != TOTAL_PAGES) {
                    multimediaAdapter.addLoadingFooter();
                } else {
                    isLastPage = true;
                }
            }

            @Override
            public void onFailure(@NonNull Call<TopRatedMovies> call, @NonNull Throwable t) {
                t.getLocalizedMessage();
                multimediaAdapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }
}
