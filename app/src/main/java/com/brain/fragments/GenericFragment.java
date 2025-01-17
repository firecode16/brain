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
import com.brain.model.MediaApiResponse;
import com.brain.model.MediaDetail;
import com.brain.model.Profile;
import com.brain.model.Result;
import com.brain.multimediaplayer.service.MediaPlayerService;
import com.brain.multimediaposts.model.User;
import com.brain.repository.PostHistoryRepository;
import com.brain.service.CustomScrollStateService;
import com.brain.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenericFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
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
    private static final int ITEMS_SIZE = 20; // items by pagination
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private PostHistoryRepository postRepository;
    private User user;

    public GenericFragment() { }

    public static GenericFragment newInstance(int sectionNumber, User user) {
        GenericFragment fragment = new GenericFragment();
        Bundle args = new Bundle();
        args.putInt(SECTION_FRAGMENT_NUMBER, sectionNumber);
        args.putSerializable("user", user);
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
        user = (User) requireArguments().getSerializable("user");
        setUpFragmentView(recycler);
        return rootView;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setUpFragmentView(final RecyclerView recyclerView) {
        util = new Util(getContext());
        assert getArguments() != null;
        int sectionFragmentNumber = getArguments().getInt(SECTION_FRAGMENT_NUMBER);

        switch (sectionFragmentNumber) {
            case 1:
                layoutManager = new LinearLayoutManager(getActivity());
                multimediaAdapter = new MultimediaAdapter();
                recyclerView.setAdapter(null);
                recyclerView.setLayoutManager(null);
                recyclerView.setHasFixedSize(true);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(multimediaAdapter);
                recyclerView.setLayoutManager(layoutManager);

                recyclerView.addOnScrollListener(new CustomScrollStateService(layoutManager) {
                    @Override
                    public void visibleItemCenterPosition(int itemPosition, int index) {
                        MediaPlayerService.Companion.playIndexWhenScrolledUpOrDownOrSliderAndPausePreviousPlayer(itemPosition, index);
                    }

                    @Override
                    public int getTotalPageCount() {
                        return TOTAL_PAGES;
                    }

                    @Override
                    protected void loadMoreItems() {
                        /*isLoading = true;
                        currentPage += 1;
                        loadNextPage();*/
                    }

                    @Override
                    public boolean isLoading() {
                        return isLoading;
                    }

                    @Override
                    public boolean isLastPage() {
                        return isLastPage;
                    }
                });

                // init service and load data
                postRepository = ApiRest.getClient(getContext()).create(PostHistoryRepository.class);
                loadFirstPage();

                btnRetry.setOnClickListener(v -> loadFirstPage());
                swipeRefresh.setOnRefreshListener(this::doRefresh);
                break;
            case 2:
                layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);

                swipeRefresh.setEnabled(false);
                progressBar.setVisibility(View.INVISIBLE);

                List<Profile> userItems = new ArrayList<>();
                userItems.add(new Profile(user.getUserId(), user.getUserName(), user.getFullName(), user.getEmail(), user.getBackdropImage(), true));

                RecyclerView.Adapter<UserViewHolder> userViewHolderAdapter = new UserAdapter(getActivity(), userItems);
                recyclerView.setAdapter(userViewHolderAdapter);
                break;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void doRefresh() {
        progressBar.setVisibility(View.VISIBLE);
        if (callPostRepository().isExecuted()) {
            callPostRepository().cancel();
        }

        // check if data is stale.
        // execute network request if cache is expired; otherwise do not update data.
        multimediaAdapter.getMediaDetailList().clear();
        multimediaAdapter.notifyDataSetChanged();
        MediaPlayerService.Companion.releasePlayer();
        loadFirstPage();
        isLastPage = false;
        swipeRefresh.setRefreshing(false);
    }

    private Result fetchResults(Response<MediaApiResponse> response) {
        MediaApiResponse topRatedMedia = response.body();
        return Objects.requireNonNull(topRatedMedia).getData();
    }

    private Call<MediaApiResponse> callPostRepository() {
        return postRepository.getPosts(Objects.requireNonNull(user).getUserId(), currentPage, ITEMS_SIZE);
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

    private void loadFirstPage() {
        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();
        currentPage = PAGE_START;

        callPostRepository().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MediaApiResponse> call, @NonNull Response<MediaApiResponse> response) {
                hideErrorView();

                // Got data. Send it to adapter
                final Result result = fetchResults(response);
                final Profile profile = new Profile(user.getUserId(), user.getUserName(), user.getFullName(), user.getEmail(), user.getBackdropImage(), user.getAuth());
                List<MediaDetail> mediaDetailList = result.getPost();

                progressBar.setVisibility(View.GONE);
                multimediaAdapter.addAll(mediaDetailList);
                multimediaAdapter.addProfile(profile);

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
        callPostRepository().enqueue(new Callback<>() {
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

    public LinearLayoutManager getLinearLayout() {
        return layoutManager;
    }

    public RecyclerView getRecyclerView() {
        return recycler;
    }

    @Override
    public void onRefresh() { }
}
