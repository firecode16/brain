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
import androidx.media3.ui.PlayerView
import androidx.viewpager.widget.PagerAdapter
import com.brain.multimediaplayer.service.MediaPlayerService
import com.brain.multimediaslider.R
import com.brain.multimediaslider.impl.ItemClickListenerImpl
import com.brain.multimediaslider.model.Multimedia
import com.bumptech.glide.Glide

class ViewPagerAdapter(
    var context: Context?,
    mediaList: List<Multimedia>,
    private var placeholder: Int,
    private var titleBackground: Int,
    private var textAlign: String
) : PagerAdapter() {
    private var mediaList: List<Multimedia>? = mediaList
    private var layoutInflater: LayoutInflater? = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?

    private var itemClickListener: ItemClickListenerImpl? = null
    private var url: String? = null

    override fun getCount(): Int {
        return mediaList!!.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    @SuppressLint("ClickableViewAccessibility", "MissingInflatedId")
    override fun instantiateItem(container: ViewGroup, position: Int): View {
        val itemView = layoutInflater!!.inflate(R.layout.multimedia_container, container, false)

        val sliderImageView = itemView.findViewById<ImageView>(R.id.sliderImageView)
        val sliderPlayerView = itemView.findViewById<PlayerView>(R.id.sliderPlayerView)
        val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar)
        val footerLinear = itemView.findViewById<LinearLayout>(R.id.footerLinear)
        val textView = itemView.findViewById<TextView>(R.id.textView)

        if (mediaList!![position].title != null) {
            textView.text = mediaList!![position].title
            footerLinear.setBackgroundResource(titleBackground)
            textView.gravity = getGravityFromAlign(textAlign)
            footerLinear.gravity = getGravityFromAlign(textAlign)
        } else {
            footerLinear.visibility = View.INVISIBLE
        }

        if (mediaList!![position].url !== null) {
            url = mediaList!![position].url!!
            context.let {
                if (mediaList!![position].contentType.equals("image/jpg")) {
                    Glide.with(it!!).load(url).centerCrop().into(sliderImageView)
                } else if (mediaList!![position].contentType.equals("video/mp4") || mediaList!![position].contentType.equals("audio/mp3")) {
                    MediaPlayerService.initPlayer(it!!, url!!, position, "SLIDER", false, sliderPlayerView, progressBar)
                } else {}
            }
        }

        container.addView(itemView)
        sliderImageView.setOnClickListener { itemClickListener?.onItemSelected(position) }
        sliderPlayerView.setOnClickListener { itemClickListener?.onItemSelected(position) }

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

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }
}