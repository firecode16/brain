package com.brain.multimediapuzzlesviewer.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import androidx.viewpager.widget.PagerAdapter
import com.brain.multimediaplayer.service.MediaPlayerService
import com.brain.multimediapuzzlesviewer.R
import com.brain.multimediapuzzlesviewer.model.Poster
import com.brain.multimediapuzzlesviewer.util.Util.Companion.AUDIO_MP3
import com.brain.multimediapuzzlesviewer.util.Util.Companion.IMG_GIF
import com.brain.multimediapuzzlesviewer.util.Util.Companion.IMG_JPEG
import com.brain.multimediapuzzlesviewer.util.Util.Companion.IMG_JPG
import com.brain.multimediapuzzlesviewer.util.Util.Companion.IMG_PNG
import com.brain.multimediapuzzlesviewer.util.Util.Companion.VIDEO_MP4
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.gif.GifDrawable

internal class MediaPagerAdapter(
    val context: Context,
    private val objList: List<Poster>,
    private val itemPosition: Int,
    private val url: String
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

        val contentType: String = objList[position].contentType

        when (contentType) {
            IMG_JPG, IMG_JPEG, IMG_PNG -> {
                val drawable: Drawable? = imageView.getDrawable()
                val adjustUrl: String = url + objList[position].id

                if (drawable is GifDrawable) {
                    imageView.setImageDrawable(null)
                    Glide.with(imageView.context).asBitmap().load(adjustUrl).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView)
                } else if (drawable == null) {
                    Glide.with(imageView.context).asBitmap().load(adjustUrl).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView)
                }
            }

            IMG_GIF -> {
                val drawable: Drawable? = imageView.getDrawable()
                val adjustUrl: String = url + objList[position].id

                if (drawable is BitmapDrawable) {
                    imageView.setImageBitmap(null)
                    Glide.with(imageView.context).asGif().load(adjustUrl).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView)
                } else if (drawable == null) {
                    Glide.with(imageView.context).asGif().load(adjustUrl).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView)
                }
            }

            VIDEO_MP4, AUDIO_MP3 -> {
                val player: Player? = playerView.player
                if (player == null) {
                    MediaPlayerService.prepareIndexesOfMultimediaWhenOpenDialog(itemPosition, position, playerView)
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