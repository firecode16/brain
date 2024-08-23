package com.brain.fragments;

import android.annotation.SuppressLint;
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
import com.brain.model.MediaApiResponse;
import com.brain.model.MediaDetail;
import com.brain.model.Profile;
import com.brain.model.Result;
import com.brain.service.CustomScrollStateService;
import com.brain.service.MediaPlayerService;
import com.brain.util.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenericFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, PaginationAdapterCallbackImpl {
    protected RecyclerView recycler;
    protected SwipeRefreshLayout swipeRefresh;
    protected LinearLayoutManager layoutManager;
    protected MultimediaAdapter multimediaAdapter;
    protected Util util;
    protected ProgressBar progressBar;
    protected LinearLayout errorLayout;
    protected Button btnRetry;
    protected TextView txtError;

    private static final String SECTION_FRAGMENT_NUMBER = "section_fragment_number";
    private static final int PAGE_START = 0;
    private static final int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    private static final int ITEMS_SIZE = 10; // items by pagination
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
        util = new Util(getContext());
        assert getArguments() != null;
        int sectionFragmentNumber = getArguments().getInt(SECTION_FRAGMENT_NUMBER);

        switch (sectionFragmentNumber) {
            case 1:
                layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                multimediaAdapter = new MultimediaAdapter(getContext());
                recyclerView.setHasFixedSize(false);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(multimediaAdapter);

                recyclerView.addOnScrollListener(new CustomScrollStateService(layoutManager) {
                    @Override
                    public void visibleItemCenterPosition(int index) {
                        if (index != -1) {
                            MediaPlayerService.Companion.playIndexAndPausePreviousPlayer(index);
                        }
                    }

                    @Override
                    public int getTotalPageCount() {
                        return TOTAL_PAGES;
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

                List<Profile> userItems = new ArrayList<>();
                userItems.add(new Profile(111101L, "fredi303", "Fredi Hdz", "fredi303@brain.com", null, 15, true));

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
        if (callTopRatedMultimediaApi().isExecuted()) {
            callTopRatedMultimediaApi().cancel();
        }

        // check if data is stale.
        // execute network request if cache is expired; otherwise do not update data.
        multimediaAdapter.getMediaDetailList().clear();
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

    private Result fetchResults(Response<MediaApiResponse> response) {
        MediaApiResponse topRatedMedia = response.body();
        assert topRatedMedia != null;
        return topRatedMedia.getData();
    }

    private Call<MediaApiResponse> callTopRatedMultimediaApi() {
        return apiRestImpl.getTopRatedMultimedia(190881L, currentPage, ITEMS_SIZE);
    }

    private void showErrorView(Throwable throwable) {
        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            txtError.setText(util.fetchErrorMessage(throwable));
        }
    }

    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void retryPageLoad() {
        loadNextPage();
    }

    @Override
    public void onPause() {
        super.onPause();
        MediaPlayerService.Companion.releaseAllPlayers();
    }

    private void loadFirstPage() {
        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();
        currentPage = PAGE_START;

        callTopRatedMultimediaApi().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MediaApiResponse> call, @NonNull Response<MediaApiResponse> response) {
                hideErrorView();

                // Got data. Send it to adapter
                final Result result = fetchResults(response);
                final Profile profile = new Profile(111101L, result.getUserName(), result.getFullName(), result.getEmail(), result.getBackdropName(), result.getCountContacts(), result.isAuth());
                List<MediaDetail> mediaDetailList = result.getPost();

                progressBar.setVisibility(View.GONE);
                multimediaAdapter.addProfile(profile);
                multimediaAdapter.addAll(mediaDetailList);

                if (currentPage <= TOTAL_PAGES) {
                    multimediaAdapter.addLoadingFooter();
                } else {
                    isLastPage = true;
                }
            }

            @Override
            public void onFailure(@NonNull Call<MediaApiResponse> call, @NonNull Throwable t) {
                t.getLocalizedMessage();
                showErrorView(t);
            }
        });
    }

    private void loadNextPage() {
        callTopRatedMultimediaApi().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MediaApiResponse> call, @NonNull Response<MediaApiResponse> response) {
                multimediaAdapter.removeLoadingFooter();
                isLoading = false;

                // Got data. Send it to adapter
                final Result result = fetchResults(response);
                List<MediaDetail> mediaDetailList = result.getPost();
                multimediaAdapter.addAll(mediaDetailList);

                if (currentPage != TOTAL_PAGES) {
                    multimediaAdapter.addLoadingFooter();
                } else {
                    isLastPage = true;
                }
            }

            @Override
            public void onFailure(@NonNull Call<MediaApiResponse> call, @NonNull Throwable t) {
                t.getLocalizedMessage();
                multimediaAdapter.showRetry(true, util.fetchErrorMessage(t));
            }
        });
    }
}
