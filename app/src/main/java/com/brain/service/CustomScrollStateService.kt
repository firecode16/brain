package com.brain.service

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs


/**
 * @Author FLE
 * @Company Brain Inc.
 * @Email hfredi35@gmail.com
 */
abstract class CustomScrollStateService(
    private var linearLayoutManager: LinearLayoutManager
) : RecyclerView.OnScrollListener() {
    private val controlState: Int = RecyclerView.SCROLL_STATE_IDLE

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        if (newState == controlState) {
            val visibleItemCount = linearLayoutManager.childCount
            val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()

            val screenCenter: Int = recyclerView.context.resources.displayMetrics.heightPixels / 2
            var minCenterOffset = Int.MAX_VALUE
            var middleItemIndex = 0

            for (index in 0 until visibleItemCount) {
                val itemChild = linearLayoutManager.getChildAt(index) ?: return

                val topOffset = itemChild.top
                val bottomOffset = itemChild.bottom
                val centerOffset = abs(topOffset - screenCenter) + abs(bottomOffset - screenCenter)

                if (minCenterOffset > centerOffset) {
                    minCenterOffset = centerOffset
                    middleItemIndex = index + firstVisibleItemPosition
                }
            }
            visibleItemCenterPosition(middleItemIndex)
        }
    }

    abstract fun visibleItemCenterPosition(index: Int)
    abstract fun getTotalPageCount(): Int
}