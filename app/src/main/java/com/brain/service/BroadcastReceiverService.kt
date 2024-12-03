package com.brain.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.brain.activities.HomeActivity
import com.brain.adapters.ViewFragmentPagerAdapter
import com.brain.fragments.GenericFragment
import com.brain.holders.MultimediaViewHolder

class BroadcastReceiverService : BroadcastReceiver() {
    companion object {
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

        val linearLayoutManager = genericFragment.linearLayout
        val visibleItemCount = linearLayoutManager.childCount
        val recycler: RecyclerView = genericFragment.recyclerView

        for (index in 0 until visibleItemCount) {
            val itemChild = linearLayoutManager.getChildAt(index)
            val viewHolder = itemChild?.let { recycler.getChildViewHolder(itemChild) }
            val multimediaHolder = viewHolder as MultimediaViewHolder
            val multimediaSlider = multimediaHolder.multimediaSlider
            val currentItemPosition = multimediaSlider.getItemPosition()

            if (currentItemPosition == itemPosition) {
                multimediaSlider.callAndExecuteSelectedItem(itemPosition, position)
                break
            }
        }
    }
}