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

            val displayMetricsOfHeightInPixels: Int = recyclerView.context.resources.displayMetrics.heightPixels / 2
            var maxCenterOffset = Int.MAX_VALUE
            var middleItemIndex = 0

            for (index in 0 until visibleItemCount) {
                val itemChild = linearLayoutManager.getChildAt(index) ?: return

                val topView = itemChild.top
                val bottomView = itemChild.bottom
                val calculateCenterViewChild = abs(topView - displayMetricsOfHeightInPixels) + abs(bottomView - displayMetricsOfHeightInPixels)

                if (maxCenterOffset > calculateCenterViewChild) {
                    maxCenterOffset = calculateCenterViewChild
                    middleItemIndex = index + firstVisibleItemPosition
                }
            }
            visibleItemCenterPosition(middleItemIndex)
        }
    }

    abstract fun visibleItemCenterPosition(index: Int)
    abstract fun getTotalPageCount(): Int
}