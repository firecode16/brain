package com.brain.multimediapuzzlesviewer.view

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewpager.widget.ViewPager
import com.brain.multimediaplayer.service.MediaPlayerService
import com.brain.multimediapuzzlesviewer.R
import com.brain.multimediapuzzlesviewer.adapter.MediaPagerAdapter
import com.brain.multimediapuzzlesviewer.model.Poster
import com.google.android.material.tabs.TabLayout

internal class MediaViewerView (
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

    private var itemPosition: Int = 0
    private var position: Int = 0
    private var lastPosition: Int = 0
    private lateinit var container: String

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

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp)

        toolbar.setNavigationOnClickListener {
            getIntentAndSendBroadcast()
            MediaPlayerService.pauseCurrentPlayingVideo()
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

        setupViewPager(mediaViewPager)
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                lastPosition = position
                MediaPlayerService.playIndexWhenScrolledUpOrDownOrSliderAndPausePreviousPlayer(itemPosition, position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    internal fun setMultimedia(objList: List<Poster>, itemPosition: Int, position: Int, url: String, container: String) {
        this.itemPosition = itemPosition
        this.position = position
        this.container = container
        this.mediaPagerAdapter = MediaPagerAdapter(context, objList, itemPosition, url)
        this.mediaViewPager.adapter = mediaPagerAdapter
        this.mediaViewPager.setCurrentItem(position)
        getPageLimit(objList.size)
    }

    private fun getPageLimit(mediaSize: Int) {
        if (mediaSize > mediaViewPager.offscreenPageLimit) {
            mediaViewPager.offscreenPageLimit = mediaSize
        }
    }

    internal fun open() {
        MediaPlayerService.pauseCurrentPlayingVideo()
        MediaPlayerService.playIndexWhenScrolledUpOrDownOrSliderAndPausePreviousPlayer(this.itemPosition, this.position)
    }

    private fun getIntentAndSendBroadcast() {
        val intent = Intent()
        intent.action = "com.brain.Broadcast"
        intent.putExtra("itemPosition", itemPosition)
        intent.putExtra("position", lastPosition)
        intent.putExtra("container", container)
        intent.flags = Intent.FLAG_INCLUDE_STOPPED_PACKAGES
        context.sendBroadcast(intent)
    }

}