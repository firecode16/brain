package com.brain.multimediapuzzlesviewer.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewpager.widget.ViewPager
import com.brain.multimediapuzzlesviewer.R
import com.brain.multimediapuzzlesviewer.adapter.MediaPagerAdapter
import com.brain.multimediapuzzlesviewer.model.Poster
import com.google.android.material.tabs.TabLayout


internal class MediaViewerView<T>(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : CoordinatorLayout(context, attrs, defStyleAttr) {
    internal var onDismiss: (() -> Unit)? = null

    private var toolbar: Toolbar
    private var mediaViewPager: ViewPager
    private var tabLayout: TabLayout
    private var fragmentComments: TextView
    private var fragmentSource: TextView
    private var fragmentFinancing: TextView
    private var fragmentJoin: TextView
    private var imageView: ImageView
    private var mediaPagerAdapter: MediaPagerAdapter? = null

    private var objList: List<Poster> = listOf()
    private var startPosition: Int = 0

    init {
        View.inflate(context, R.layout.multimedia_puzzles_viewer, this)

        toolbar = findViewById(R.id.toolbarPuzzles)
        mediaViewPager = findViewById(R.id.mediaViewPager)
        imageView = findViewById(R.id.imageView)
        tabLayout = findViewById(R.id.tabLayout)
        fragmentComments = findViewById(R.id.fragmentComments)
        fragmentSource = findViewById(R.id.fragmentSource)
        fragmentFinancing = findViewById(R.id.fragmentFinancing)
        fragmentJoin = findViewById(R.id.fragmentJoin)

        (context as AppCompatActivity).setSupportActionBar(toolbar)
        context.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        context.supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            onDismiss?.invoke()
        }

        //create tabs title
        tabLayout.addTab(tabLayout.newTab().setText("Comments").setIcon(R.drawable.ic_comment_50), true)
        tabLayout.addTab(tabLayout.newTab().setText("Source").setIcon(R.drawable.ic_storage_50))
        tabLayout.addTab(tabLayout.newTab().setText("Financing").setIcon(R.drawable.ic_add_dollar_50))
        tabLayout.addTab(tabLayout.newTab().setText("Join").setIcon(R.drawable.ic_add_user_group_50))

        //handling tab click event
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        fragmentComments.visibility = VISIBLE
                        fragmentSource.visibility = GONE
                        fragmentFinancing.visibility = GONE
                        fragmentJoin.visibility = GONE
                    }

                    1 -> {
                        fragmentSource.visibility = VISIBLE
                        fragmentComments.visibility = GONE
                        fragmentFinancing.visibility = GONE
                        fragmentJoin.visibility = GONE
                    }

                    2 -> {
                        fragmentFinancing.visibility = VISIBLE
                        fragmentComments.visibility = GONE
                        fragmentSource.visibility = GONE
                        fragmentJoin.visibility = GONE
                    }

                    3 -> {
                        fragmentJoin.visibility = VISIBLE
                        fragmentFinancing.visibility = GONE
                        fragmentComments.visibility = GONE
                        fragmentSource.visibility = GONE
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    internal fun setMultimedia(objList: List<Poster>, startPosition: Int, url: String) {
        this.objList = objList
        this.startPosition = startPosition
        this.mediaPagerAdapter = MediaPagerAdapter(context, objList, url)
        this.mediaViewPager.adapter = mediaPagerAdapter
    }

    internal fun open(url: String) {}
}