package com.brain.multimediapuzzlesviewer.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.media3.ui.PlayerView
import androidx.viewpager.widget.PagerAdapter
import com.brain.multimediaplayer.service.MediaPlayerService
import com.brain.multimediaplayer.util.PlayerFlag
import com.brain.multimediapuzzlesviewer.R
import com.brain.multimediapuzzlesviewer.model.Poster
import com.bumptech.glide.Glide

internal class MediaPagerAdapter(
    val context: Context,
    private val objList: List<Poster>,
    val url: String
) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?

    override fun getCount(): Int {
        return objList.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    @SuppressLint("MissingInflatedId")
    override fun instantiateItem(viewGroup: ViewGroup, position: Int): Any {
        val itemView = layoutInflater!!.inflate(R.layout.multimedia_puzzles_viewer, viewGroup, false)

        val imageView = itemView.findViewById<ImageView>(R.id.imageView)
        val playerView = itemView.findViewById<PlayerView>(R.id.playerView)
        val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar)

        if (objList.isNotEmpty()) {
            context.let {
                if (objList[position].contentType.equals("image/jpg")) {
                    Glide.with(it).load(url + objList[position].id).into(imageView)
                } else if (objList[position].contentType.equals("video/mp4") || objList[position].contentType.equals("audio/mp3")) {
                    MediaPlayerService.initPlayer(it, url + objList[position].id, position, false, playerView, progressBar, PlayerFlag.DIALOG)
                } else {
                }
            }
        }

        viewGroup.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as CoordinatorLayout)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return null
    }
}