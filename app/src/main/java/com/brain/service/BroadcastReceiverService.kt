package com.brain.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.RelativeLayout
import androidx.media3.ui.PlayerView
import com.brain.activities.HomeActivity
import com.brain.adapters.ViewFragmentPagerAdapter
import com.brain.fragments.GenericFragment
import com.brain.multimediaplayer.service.MediaPlayerService
import com.brain.multimediaslider.MultimediaSlider

class BroadcastReceiverService : BroadcastReceiver() {
    companion object {
        const val RELATIVE_LAYOUT_CHILD = 1
        const val MEDIA_SLIDER_CHILD = 2
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val itemPosition = intent!!.getIntExtra("itemPosition", 0)
        val position = intent.getIntExtra("position", 0)

        context as HomeActivity
        val viewPager = context.viewPager
        val fragmentPagerAdapter = viewPager.adapter
        val fragment: ViewFragmentPagerAdapter = fragmentPagerAdapter as ViewFragmentPagerAdapter
        val firstFragment = fragment.fragments.first()
        val genericFragment: GenericFragment = firstFragment as GenericFragment
        val viewChild = genericFragment.linearLayout.getChildAt(RELATIVE_LAYOUT_CHILD)
        val relativeLayout = viewChild as RelativeLayout

        val multimediaSlider: MultimediaSlider = relativeLayout.getChildAt(MEDIA_SLIDER_CHILD) as MultimediaSlider
        multimediaSlider.callAndExecuteSelectedItem(itemPosition, position)
    }
}