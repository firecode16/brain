package com.brain.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.brain.R;
import com.brain.adapters.ViewPagerAdapter;
import com.brain.fragments.DialogThinkFragment;
import com.brain.fragments.GenericFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    ActionBar actionBar;
    TabLayout tabLayout;
    FloatingActionButton floatingButton;
    LinearLayout linearLayout;
    AppBarLayout.LayoutParams layoutParams;

    private final int[] tabIcon = {
            R.drawable.ic_face,
            R.drawable.ic_chat,
            R.drawable.ic_profile
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        viewPager = (ViewPager) findViewById(R.id.pager);

        // add the toolbar
        setToolbar();

        // push adapter into viewpager
        setupViewPager(viewPager);

        // setup Taps
        setupTabSelectedChangeListener();
        setupTabIcons();
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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(GenericFragment.newInstance(1), getString(R.string.title_section1));
        adapter.addFragment(GenericFragment.newInstance(2), getString(R.string.title_section2));
        adapter.addFragment(GenericFragment.newInstance(3), getString(R.string.title_section3));
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(tabIcon[0]);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(tabIcon[1]);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(tabIcon[2]);
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
                        floatingButton = findViewById(R.id.btnThink);
                        floatingButton.setVisibility(View.VISIBLE);

                        linearLayout = findViewById(R.id.layoutForBtnThink);
                        linearLayout.setVisibility(View.VISIBLE);

                        layoutParams = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
                        layoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
                        toolbar.setLayoutParams(layoutParams);
                        toolbar.setVisibility(Toolbar.VISIBLE);

                        layoutParams = (AppBarLayout.LayoutParams) tabLayout.getLayoutParams();
                        layoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
                        tabLayout.setLayoutParams(layoutParams);
                        break;
                    case 1:
                        floatingButton = (FloatingActionButton) findViewById(R.id.btnThink);
                        floatingButton.setVisibility(View.INVISIBLE);

                        linearLayout = findViewById(R.id.layoutForBtnThink);
                        linearLayout.setVisibility(LinearLayout.GONE);

                        layoutParams = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
                        layoutParams.setScrollFlags(0);
                        toolbar.setLayoutParams(layoutParams);
                        toolbar.setVisibility(Toolbar.GONE);
                        break;
                    case 2:
                        floatingButton = findViewById(R.id.btnThink);
                        floatingButton.setVisibility(View.INVISIBLE);

                        linearLayout = findViewById(R.id.layoutForBtnThink);
                        linearLayout.setVisibility(LinearLayout.GONE);

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

    public void btnThinkOnClicked(View view) {
        FragmentManager fm = getSupportFragmentManager();
        DialogFragment newFragment = DialogThinkFragment.newInstance();
        newFragment.show(fm, "Dialog");
    }

}
