package com.brain.service

import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brain.R

/**
 * @Author FLE
 * @Company Brain Inc.
 * @Email hfredi35@gmail.com
 */
class RecyclerViewScrollListenerService : NestedScrollView.OnScrollChangeListener {
    private lateinit var linearLayout: LinearLayout
    private lateinit var recyclerView: RecyclerView
    private var page: Int = 0
    private var loading: Boolean = true
    private var previousTotalItemCount: Int = 0
    private var startingPageIndex: Int = 0

    override fun onScrollChange(scrollView: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        if (scrollView != null) {
            linearLayout = scrollView.getChildAt(scrollView.childCount - 1) as LinearLayout
            recyclerView = linearLayout.getChildAt(linearLayout.childCount - 2) as RecyclerView

            val position = recyclerView.measuredHeight - scrollView.measuredHeight
            if (scrollY == position) {
                Log.e("position() ", "$position")
            }

            /*linearLayout = scrollView.getChildAt(scrollView.childCount - 1) as LinearLayout
            recyclerView = linearLayout.getChildAt(linearLayout.childCount - 2) as RecyclerView

            val loader = recyclerView.findViewById<View>(R.id.loading)
            val childCount = recyclerView.childCount
            val totalItemCount = layoutManager.itemCount
            val calculateScrolling = (scrollView.measuredHeight - recyclerView.measuredWidth)*/


            /*recyclerView = view as RecyclerView
            val rViewChildCount = recyclerView.childCount
            val childAt = recyclerView.getChildAt(rViewChildCount - 1).measuredHeight - recyclerView.measuredHeight

            // on scroll change we are checking when users scroll as bottom.
            if (scrollY >= childAt) {
                // in this method we are incrementing page number,
                // making progress bar visible and calling get data method.
                page ++;
                loader.visibility = View.VISIBLE
            }*/
        }
    }

/**
    companion object {
        // The minimum amount of items to have below your current scroll position before loading more.
        const val VISIBLE_THRESHOLD = 5
        private var loading: Boolean = false
        private var auxPosition: Int = 0
    }

    private var relativeLayout: RelativeLayout? = null
    private lateinit var linearLayout: LinearLayout
    private lateinit var styledPlayerView: StyledPlayerView

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager?

        val childCount = recyclerView.childCount
        val totalItemCount = layoutManager!!.itemCount
        val currentItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (currentItemPosition != -1 && (childCount - totalItemCount) <= (currentItemPosition + VISIBLE_THRESHOLD)) {
            //Log.e("currentItemPosition() ", "" + currentItemPosition)
            //val calculateHalf = layoutManager.height / 2
            //val view = recyclerView.getChildAt(childCount - 1)
            //val position = recyclerView.getChildAdapterPosition(view)
            //val diff = view.height - view.width
            //val calculateScrolling = view.bottom - layoutManager.height

            linearLayout = recyclerView.getChildAt(childCount - 1) as LinearLayout
            relativeLayout = linearLayout.getChildAt(childCount - 1) as RelativeLayout?

            if (currentItemPosition == auxPosition) {
                if (!loading) {
                    auxPosition = currentItemPosition
                    loading = true
                    Log.e("position() ", "$currentItemPosition")
                }
            } else {
                auxPosition = currentItemPosition
                loading = false
            }

            if (relativeLayout != null) {
                styledPlayerView = relativeLayout!!.getChildAt(relativeLayout!!.childCount - 4) as StyledPlayerView

                val playerViewHeight = (styledPlayerView.layoutParams.height - styledPlayerView.layoutParams.width) / 2
                val calculateHalf = playerViewHeight / 2
                val calculateScrolling = (linearLayout.bottom - linearLayout.height) / 2

                //Log.e("calculateScrolling() ", "$calculateScrolling")
            }
        }
    }*/
}