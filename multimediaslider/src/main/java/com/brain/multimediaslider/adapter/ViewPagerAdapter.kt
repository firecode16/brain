package com.brain.multimediaslider.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import androidx.viewpager.widget.PagerAdapter
import com.brain.multimediaplayer.service.MediaPlayerService
import com.brain.multimediaslider.R
import com.brain.multimediaslider.impl.ItemClickListenerImpl
import com.brain.multimediaslider.model.ItemPlayerView
import com.brain.multimediaslider.model.Multimedia
import com.brain.multimediaslider.util.Util.Companion.AUDIO_MP3
import com.brain.multimediaslider.util.Util.Companion.IMG_GIF
import com.brain.multimediaslider.util.Util.Companion.IMG_JPEG
import com.brain.multimediaslider.util.Util.Companion.IMG_JPG
import com.brain.multimediaslider.util.Util.Companion.IMG_PNG
import com.brain.multimediaslider.util.Util.Companion.VIDEO_MP4
import com.brain.multimediaslider.util.Util.Companion.loadImage

class ViewPagerAdapter(
    context: Context,
    private var mediaList: MutableList<Multimedia>,
    private var titleBackground: Int,
    private var textAlign: String,
    private var itemPosition: Int
) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?

    private var itemClickListener: ItemClickListenerImpl? = null
    private var playerViewList: MutableList<ItemPlayerView> = mutableListOf()

    override fun getCount(): Int {
        return mediaList.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    @SuppressLint("ClickableViewAccessibility", "MissingInflatedId")
    override fun instantiateItem(container: ViewGroup, position: Int): View {
        val itemView = layoutInflater!!.inflate(R.layout.multimedia_container, container, false)

        val sliderImageView: ImageView = itemView.findViewById(R.id.sliderImageView)
        val sliderPlayerView: PlayerView = itemView.findViewById(R.id.sliderPlayerView)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        val footerLinear = itemView.findViewById<LinearLayout>(R.id.footerLinear)
        val textView = itemView.findViewById<TextView>(R.id.textView)

        if (mediaList[position].title != null) {
            textView.text = mediaList[position].title
            footerLinear.setBackgroundResource(titleBackground)
            textView.gravity = getGravityFromAlign(textAlign)
            footerLinear.gravity = getGravityFromAlign(textAlign)
        } else {
            footerLinear.visibility = View.INVISIBLE
        }

        val url: String = mediaList[position].url
        val contentType: String = mediaList[position].contentType

        when (contentType) {
            IMG_JPG, IMG_JPEG, IMG_PNG -> {
                sliderImageView.setImageResource(0)
                loadImage(url, sliderImageView.context, sliderImageView)
            }

            IMG_GIF -> {
                sliderImageView.setImageResource(0)
                loadImage(url, sliderImageView.context, sliderImageView)
            }

            VIDEO_MP4, AUDIO_MP3 -> {
                sliderImageView.setImageResource(0)
                val player: Player? = sliderPlayerView.player
                if (player == null) {
                    MediaPlayerService.initPlayer(sliderPlayerView.context, url, position, itemPosition, false, sliderPlayerView, progressBar)
                    val itemPlayerView = ItemPlayerView(itemPosition, position, sliderPlayerView)
                    playerViewList.add(itemPlayerView)
                }
            }
        }

        container.addView(itemView)
        sliderImageView.setOnClickListener { itemClickListener?.onItemSelected(itemPosition, position) }
        sliderPlayerView.setOnClickListener { itemClickListener?.onItemSelected(itemPosition, position) }

        return itemView
    }

    private fun getGravityFromAlign(textAlign: String): Int {
        return when (textAlign) {
            "RIGHT" -> {
                Gravity.RIGHT
            }

            "CENTER" -> {
                Gravity.CENTER
            }

            else -> {
                Gravity.LEFT
            }
        }
    }

    fun setItemClickListener(itemClickListener: ItemClickListenerImpl) {
        this.itemClickListener = itemClickListener
    }

    fun setSliderPlayerViewList(): MutableList<ItemPlayerView> {
        return playerViewList
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }
}