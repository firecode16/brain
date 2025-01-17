package com.brain.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.brain.R;
import com.brain.adapters.ViewFragmentPagerAdapter;
import com.brain.fragments.GenericFragment;
import com.brain.multimediaplayer.service.MediaPlayerService;
import com.brain.multimediaposts.fragments.AboutDialogFragment;
import com.brain.multimediaposts.fragments.PostsDialogFragment;
import com.brain.multimediaposts.model.User;
import com.brain.service.BroadcastReceiverService;
import com.brain.util.Util;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private AppBarLayout.LayoutParams layoutParams;
    private CoordinatorLayout coordinatorLayoutForFAB;
    private BroadcastReceiverService broadcastReceiverService = null;

    private FloatingActionButton fabOptionMenuTouch;
    private FloatingActionButton fabPosts;
    private FloatingActionButton fabAbout;

    private Animation fabOpen;
    private Animation fabClose;
    private Animation animationFabOptionMenuRotateForward;
    private Animation animationFabOptionMenuRotateBackward;

    ImageView imageView;

    private boolean fabStatus = false;
    private int tabSelected = -1;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        viewPager = findViewById(R.id.pager);
        fabPosts = findViewById(R.id.fabPosts);
        fabAbout = findViewById(R.id.fabAbout);
        imageView = findViewById(R.id.imagePost);

        // add the toolbar
        setToolbar();

        // push adapter into viewpager
        setupViewPager(viewPager);

        // setup Taps
        setupTabSelectedChangeListener();
        setupTabIcons();

        initFabAnimations();

        configureReceiver();

        fullScreenAndHideNavigationBar();
    }

    @SuppressLint("NewApi")
    private void configureReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.brain.Broadcast");
        broadcastReceiverService = new BroadcastReceiverService();
        registerReceiver(broadcastReceiverService, filter, Context.RECEIVER_EXPORTED);
    }

    private void fullScreenAndHideNavigationBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void initFabAnimations() {
        fabOpen = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_close);
        animationFabOptionMenuRotateForward = AnimationUtils.loadAnimation(getApplication(), R.anim.rotate_forward);
        animationFabOptionMenuRotateBackward = AnimationUtils.loadAnimation(getApplication(), R.anim.rotate_backward);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewFragmentPagerAdapter adapter = new ViewFragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        user = (User) getIntent().getSerializableExtra("user");
        adapter.addFragment(GenericFragment.newInstance(1, user), getString(R.string.title_section1));
        adapter.addFragment(GenericFragment.newInstance(2, user), getString(R.string.title_section2));
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(Util.getTabIcon[0]);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(Util.getTabIcon[1]);
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);

        ImageView actionAvatar = findViewById(R.id.actionAvatar);
        actionAvatar.setOnClickListener(v -> Toast.makeText(getApplication(), "Action Avatar.", Toast.LENGTH_SHORT).show());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem searchMenu = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            SearchView search = (SearchView) item.getActionView();
            assert search != null;
            search.setSubmitButtonEnabled(false);
            search.setQueryHint("Project, author...");
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupTabSelectedChangeListener() {
        tabLayout = findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        fabOptionMenuTouch = findViewById(R.id.fabOptionMenu);
                        fabOptionMenuTouch.setVisibility(View.VISIBLE);

                        coordinatorLayoutForFAB = findViewById(R.id.coordinatorForFAB);
                        coordinatorLayoutForFAB.setVisibility(CoordinatorLayout.VISIBLE);

                        layoutParams = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
                        layoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
                        toolbar.setLayoutParams(layoutParams);
                        toolbar.setVisibility(Toolbar.VISIBLE);

                        layoutParams = (AppBarLayout.LayoutParams) tabLayout.getLayoutParams();
                        layoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
                        tabLayout.setLayoutParams(layoutParams);
                        break;
                    case 1:
                        fabOptionMenuTouch = findViewById(R.id.fabOptionMenu);
                        fabOptionMenuTouch.setVisibility(View.INVISIBLE);

                        coordinatorLayoutForFAB = findViewById(R.id.coordinatorForFAB);
                        coordinatorLayoutForFAB.setVisibility(CoordinatorLayout.GONE);

                        layoutParams = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
                        layoutParams.setScrollFlags(0);
                        toolbar.setLayoutParams(layoutParams);
                        toolbar.setVisibility(Toolbar.GONE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    MediaPlayerService.Companion.pauseCurrentPlayingVideo();
                    tabSelected = tab.getPosition();
                    //close fab menu
                    hideFAB();
                    fabStatus = false;
                } else if (tab.getPosition() == 1) {
                    MediaPlayerService.Companion.resumePlayerIndexCurrent();
                    tabSelected = tab.getPosition();
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        tabLayout.setupWithViewPager(viewPager);
    }

    public void getFabOptionMenuOnClicked(View view) {
        if (!fabStatus) {
            // display fab menu
            expandFAB();
            fabStatus = true;
        } else {
            //close fab menu
            hideFAB();
            fabStatus = false;
        }
    }

    private void expandFAB() {
        fabOptionMenuTouch.startAnimation(animationFabOptionMenuRotateForward);

        fabPosts.startAnimation(fabOpen);
        fabPosts.setClickable(true);

        fabAbout.startAnimation(fabOpen);
        fabAbout.setClickable(true);
    }

    private void hideFAB() {
        fabOptionMenuTouch.startAnimation(animationFabOptionMenuRotateBackward);

        fabPosts.startAnimation(fabClose);
        fabPosts.setClickable(false);

        fabAbout.startAnimation(fabClose);
        fabAbout.setClickable(false);
    }

    public void getFabPostsOnClick(View view) {
        FragmentManager fm = getSupportFragmentManager();
        user = (User) getIntent().getSerializableExtra("user");
        DialogFragment newFragment = PostsDialogFragment.Companion.newInstance(Objects.requireNonNull(user));
        newFragment.show(fm, "Dialog");
    }

    public void getFabAboutOnClick(View view) {
        FragmentManager fm = getSupportFragmentManager();
        DialogFragment newFragment = AboutDialogFragment.Companion.newInstance();
        newFragment.show(fm, "Dialog");
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaPlayerService.Companion.pauseCurrentPlayingVideo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fullScreenAndHideNavigationBar();
        if (tabSelected == 0) {
            MediaPlayerService.Companion.pauseCurrentPlayingVideo();
        } else {
            MediaPlayerService.Companion.resumePlayerIndexCurrent();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiverService);
        MediaPlayerService.Companion.releasePlayer();
    }
}
