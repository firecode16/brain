package com.brain.multimediapuzzlesviewer.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.brain.multimediapuzzlesviewer.R
import com.brain.multimediapuzzlesviewer.adapter.MediaPagerAdapter
import com.brain.multimediapuzzlesviewer.adapter.ViewFragmentPagerAdapter
import com.brain.multimediapuzzlesviewer.fragment.ActionsFragment
import com.brain.multimediapuzzlesviewer.model.Poster
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout

internal class MediaViewerView<T> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : RelativeLayout(context, attrs, defStyleAttr) {
    internal var onDismiss: (() -> Unit)? = null
    internal var overlayView: View? = null
        set(value) {
            field = value
            value?.let { rootContainer.addView(it) }
        }

    private var rootContainer: ViewGroup
    private var toolbar: Toolbar
    private var mediaViewPager: ViewPager
    //private var actionViewPager: ViewPager
    private var tabLayout: TabLayout
    private var imageView: ImageView
    private var mediaPagerAdapter: MediaPagerAdapter? = null

    private var objList: List<Poster> = listOf()
    private var startPosition: Int = 0

    init {
        View.inflate(context, R.layout.multimedia_puzzles_viewer, this)

        rootContainer = findViewById(R.id.nestedScrollView)
        toolbar = findViewById(R.id.toolbarPuzzles)
        mediaViewPager = findViewById(R.id.mediaViewPager)
        tabLayout = findViewById(R.id.tabLayout)
        imageView = findViewById(R.id.imageView)
        //actionViewPager = findViewById(R.id.actionViewPager)

        (context as AppCompatActivity).setSupportActionBar(toolbar)
        context.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        context.supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            onDismiss?.invoke()
        }

        mediaViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    internal fun setMultimedia(objList: List<Poster>, startPosition: Int, url: String) {
        this.objList = objList
        this.startPosition = startPosition
        this.mediaPagerAdapter = MediaPagerAdapter(context, objList, url)
        this.mediaViewPager.adapter = mediaPagerAdapter

        // setup Taps
        setupTabSelectedChangeListener()
        setupActionsTabs()
    }

    internal fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewFragmentPagerAdapter((context as FragmentActivity).supportFragmentManager)
        adapter.addFragment(ActionsFragment.newInstance(1), "Tab 1")
        adapter.addFragment(ActionsFragment.newInstance(2), "Tab 2")
        viewPager.adapter = adapter
    }

    internal fun setupActionsTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("comments"))
        tabLayout.addTab(tabLayout.newTab().setText("likes"))
    }

    internal fun setupTabSelectedChangeListener() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        println("**** 1")
                    }
                    1 -> {
                        println("**** 2")
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        //tabLayout.setupWithViewPager(actionViewPager)
    }

    internal fun open(url: String) {
        context?.let {
            Glide.with(it).load(url + objList[startPosition].id).into(this.imageView)
        }
    }
}