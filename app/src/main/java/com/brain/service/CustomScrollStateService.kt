package com.brain.service

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brain.adapters.MultimediaAdapter
import com.brain.holders.MultimediaViewHolder
import com.brain.multimediaslider.MultimediaSlider
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
    private var multimediaAdapter: MultimediaAdapter? = null
    private var multimediaViewHolder: MultimediaViewHolder? = null
    private var multimediaSlider: MultimediaSlider? = null

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        if (newState == controlState) {
            val visibleItemCount = linearLayoutManager.childCount
            val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()

            val displayMetricsOfHeightInPixels: Int = recyclerView.context.resources.displayMetrics.heightPixels / 2
            var maxCenterOffset = Int.MAX_VALUE
            var middleItemIndex = 0
            var currentItem = 0

            for (index in 0 until visibleItemCount) {
                val itemChild = linearLayoutManager.getChildAt(index) ?: return

                val topView = itemChild.top
                val bottomView = itemChild.bottom
                val calculateCenterViewChild = abs(topView - displayMetricsOfHeightInPixels) + abs(bottomView - displayMetricsOfHeightInPixels)

                if (maxCenterOffset > calculateCenterViewChild) {
                    maxCenterOffset = calculateCenterViewChild
                    middleItemIndex = index + firstVisibleItemPosition

                    val view = linearLayoutManager.getChildAt(index)
                    val viewHolder = view?.let { recyclerView.getChildViewHolder(view) }
                    val mediaAdapter = viewHolder?.bindingAdapter
                    multimediaAdapter = mediaAdapter as MultimediaAdapter?
                    val mediaDetailList = multimediaAdapter?.mediaDetailList
                    val mediaDetail = mediaDetailList?.get(middleItemIndex)

                    if (mediaDetail?.content?.size != 0 && mediaDetail?.array!! > 1) {
                        multimediaViewHolder = viewHolder as MultimediaViewHolder
                        multimediaSlider = multimediaViewHolder!!.multimediaSlider

                        currentItem = multimediaSlider!!.getCurrentItem()
                    } else if (mediaDetail.content.size != 0 && mediaDetail.array == 1) {
                        currentItem = middleItemIndex
                    }
                }
            }
            visibleItemCenterPosition(middleItemIndex, currentItem)
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount: Int = linearLayoutManager.childCount
        val totalItemCount: Int = linearLayoutManager.itemCount
        val firstVisibleItemPosition: Int = linearLayoutManager.findFirstVisibleItemPosition()

        if (!isLoading() && !isLastPage()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                loadMoreItems()
            }
        }
    }

    abstract fun visibleItemCenterPosition(itemPosition: Int, index: Int)
    abstract fun getTotalPageCount(): Int
    protected abstract fun loadMoreItems()
    abstract fun isLoading(): Boolean
    abstract fun isLastPage(): Boolean
}