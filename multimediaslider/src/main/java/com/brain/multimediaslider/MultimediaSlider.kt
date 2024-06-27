package com.brain.multimediaslider

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.brain.multimediaplayer.service.MediaPlayerService
import com.brain.multimediaplayer.util.PlayerFlag
import com.brain.multimediaslider.adapter.ViewPagerAdapter
import com.brain.multimediaslider.impl.ItemChangeListenerImpl
import com.brain.multimediaslider.impl.ItemClickListenerImpl
import com.brain.multimediaslider.impl.TouchListenerImpl
import com.brain.multimediaslider.model.Multimedia
import com.brain.multimediaslider.util.ActionTypes

@SuppressLint("ClickableViewAccessibility")
class MultimediaSlider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private var viewPager: ViewPager? = null
    private var sliderDots: LinearLayout? = null
    private var viewPagerAdapter: ViewPagerAdapter? = null

    private var dots: Array<ImageView?>? = null

    private var cornerRadius: Int = 0
    private var period: Long = 0
    private var delay: Long = 0
    private var autoCycle = false

    private var selectedDot = 0
    private var unselectedDot = 0
    private var errorImage = 0
    private var placeholder = 0
    private var titleBackground = 0
    private var textAlign = "LEFT"
    private var indicatorAlign = "CENTER"

    private var itemChangeListener: ItemChangeListenerImpl? = null
    private var touchListener: TouchListenerImpl? = null

    init {
        LayoutInflater.from(getContext()).inflate(R.layout.slider_viewpager, this, true)
        viewPager = findViewById(R.id.viewPager)
        sliderDots = findViewById(R.id.sliderDots)

        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.MultimediaSlider, defStyleAttr, defStyleAttr)

        cornerRadius = typedArray.getInt(R.styleable.MultimediaSlider_iss_corner_radius, 1)
        period = typedArray.getInt(R.styleable.MultimediaSlider_iss_period, 1000).toLong()
        delay = typedArray.getInt(R.styleable.MultimediaSlider_iss_delay, 1000).toLong()
        autoCycle = typedArray.getBoolean(R.styleable.MultimediaSlider_iss_auto_cycle, false)
        placeholder = typedArray.getResourceId(R.styleable.MultimediaSlider_iss_placeholder, R.drawable.default_placeholder)
        selectedDot = typedArray.getResourceId(R.styleable.MultimediaSlider_iss_selected_dot, R.drawable.selected_dot)
        unselectedDot = typedArray.getResourceId(R.styleable.MultimediaSlider_iss_unselected_dot, R.drawable.unselected_dot)
        titleBackground = typedArray.getResourceId(R.styleable.MultimediaSlider_iss_title_background, R.drawable.gradient)

        if (typedArray.getString(R.styleable.MultimediaSlider_iss_text_align) != null) {
            textAlign = typedArray.getString(R.styleable.MultimediaSlider_iss_text_align)!!
        }

        if (typedArray.getString(R.styleable.MultimediaSlider_iss_indicator_align) != null) {
            indicatorAlign = typedArray.getString(R.styleable.MultimediaSlider_iss_indicator_align)!!
        }

        if (touchListener != null) {
            viewPager!!.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_MOVE -> touchListener!!.onTouched(ActionTypes.MOVE)
                    MotionEvent.ACTION_DOWN -> touchListener!!.onTouched(ActionTypes.DOWN)
                    MotionEvent.ACTION_UP -> touchListener!!.onTouched(ActionTypes.UP)
                }
                false
            }
        }

        setupViewPager(viewPager)
    }

    fun setMediaList(mediaList: List<Multimedia>) {
        viewPagerAdapter = ViewPagerAdapter(context, mediaList, placeholder, titleBackground, textAlign)
        viewPager!!.adapter = viewPagerAdapter

        if (mediaList.isNotEmpty()) {
            setupDots(mediaList.size)
        }
    }

    private fun setupDots(size: Int) {
        sliderDots!!.gravity = getGravityFromAlign(indicatorAlign)
        sliderDots!!.removeAllViews()
        dots = arrayOfNulls(size)

        for (i in 0 until size) {
            dots!![i] = ImageView(context)
            dots!![i]!!.setImageDrawable(ContextCompat.getDrawable(context, unselectedDot))

            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

            params.setMargins(8, 0, 8, 0)
            sliderDots!!.addView(dots!![i], params)
        }

        dots!![0]!!.setImageDrawable(ContextCompat.getDrawable(context, selectedDot))
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                for (dot in dots!!) {
                    dot!!.setImageDrawable(ContextCompat.getDrawable(context, unselectedDot))
                }

                dots!![position]!!.setImageDrawable(ContextCompat.getDrawable(context, selectedDot))

                if (itemChangeListener != null) {
                    itemChangeListener!!.onItemChanged(position)
                }

                MediaPlayerService.playIndexAndPausePreviousPlayer(position, PlayerFlag.SLIDER)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun getGravityFromAlign(textAlign: String): Int {
        return when (textAlign) {
            "RIGHT" -> {
                Gravity.RIGHT
            }

            "LEFT" -> {
                Gravity.LEFT
            }

            else -> {
                Gravity.CENTER
            }
        }
    }

    fun setItemClickListener(itemClickListener: ItemClickListenerImpl) {
        viewPagerAdapter?.setItemClickListener(itemClickListener)
    }
}