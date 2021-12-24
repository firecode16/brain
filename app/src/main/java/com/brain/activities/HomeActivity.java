package com.brain.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.brain.R;
import com.brain.adapters.ViewFragmentPagerAdapter;
import com.brain.fragments.GenericFragment;
import com.brain.fragments.ThinkDialogFragment;
import com.brain.util.Util;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager viewPager;
    ActionBar actionBar;
    TabLayout tabLayout;
    AppBarLayout.LayoutParams layoutParams;
    CoordinatorLayout coordinatorLayoutForFAB;

    FloatingActionButton fabOptionMenuTouch;
    FloatingActionButton fabTextPosting;
    FloatingActionButton fabUploadVideoClip;

    Animation fabOpen;
    Animation fabClose;
    Animation animationFabOptionMenuRotateForward;
    Animation animationFabOptionMenuRotateBackward;

    ImageView imageView;

    private boolean fabStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        viewPager = (ViewPager) findViewById(R.id.pager);
        fabTextPosting = findViewById(R.id.fabTextPosting);
        fabUploadVideoClip = findViewById(R.id.fabUploadVideoClip);
        imageView = findViewById(R.id.imagePost);

        // add the toolbar
        setToolbar();

        // push adapter into viewpager
        setupViewPager(viewPager);

        // setup Taps
        setupTabSelectedChangeListener();
        setupTabIcons();

        initFabAnimations();
    }

    private void initFabAnimations() {
        fabOpen = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_close);
        animationFabOptionMenuRotateForward = AnimationUtils.loadAnimation(getApplication(), R.anim.rotate_forward);
        animationFabOptionMenuRotateBackward = AnimationUtils.loadAnimation(getApplication(), R.anim.rotate_backward);
    }

    /**
     * set the toolbar action bar
     */
    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("brain");
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewFragmentPagerAdapter adapter = new ViewFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(GenericFragment.newInstance(1), getString(R.string.title_section1));
        adapter.addFragment(GenericFragment.newInstance(2), getString(R.string.title_section2));
        adapter.addFragment(GenericFragment.newInstance(3), getString(R.string.title_section3));
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(Util.getTabIcon[0]);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(Util.getTabIcon[1]);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(Util.getTabIcon[2]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView search = (SearchView) item.getActionView();

        if (search != null) {
            search.setSubmitButtonEnabled(false);
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            HomeActivity.this.onSearchRequested();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupTabSelectedChangeListener() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
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
                        fabOptionMenuTouch = (FloatingActionButton) findViewById(R.id.fabOptionMenu);
                        fabOptionMenuTouch.setVisibility(View.INVISIBLE);

                        coordinatorLayoutForFAB = findViewById(R.id.coordinatorForFAB);
                        coordinatorLayoutForFAB.setVisibility(CoordinatorLayout.GONE);

                        layoutParams = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
                        layoutParams.setScrollFlags(0);
                        toolbar.setLayoutParams(layoutParams);
                        toolbar.setVisibility(Toolbar.GONE);
                        break;
                    case 2:
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
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
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

        fabTextPosting.startAnimation(fabOpen);
        fabTextPosting.setClickable(true);

        fabUploadVideoClip.startAnimation(fabOpen);
        fabUploadVideoClip.setClickable(true);
    }

    private void hideFAB() {
        fabOptionMenuTouch.startAnimation(animationFabOptionMenuRotateBackward);

        fabTextPosting.startAnimation(fabClose);
        fabTextPosting.setClickable(false);

        fabUploadVideoClip.startAnimation(fabClose);
        fabUploadVideoClip.setClickable(false);
    }

    public void getFabTextPostingOnClick(View view) {
        FragmentManager fm = getSupportFragmentManager();
        DialogFragment newFragment = ThinkDialogFragment.newInstance();
        newFragment.show(fm, "Dialog");
    }

    public void getFabUploadVideoClipOnClick(View view) {
        Toast.makeText(getApplication(), "Floating Action Button 2", Toast.LENGTH_SHORT).show();
    }

}
