package com.brain.multimediaslider.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class Util {
    companion object {
        const val VIDEO_MP4 = "video/mp4"
        const val AUDIO_MP3 = "audio/mp3"
        const val IMG_JPG = "image/jpg"
        const val IMG_JPEG = "image/jpeg"
        const val IMG_PNG = "image/png"
        const val IMG_GIF = "image/gif"

        @SuppressLint("CheckResult")
        fun loadImage(url: String, ctx: Context, imageView: ImageView) {
            Glide.with(ctx)
                .load(url)
                .skipMemoryCache(true)
                .placeholder(circularProgress(ctx))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        return false
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(imageView)
        }

        private fun circularProgress(ctx: Context): Drawable {
            val circularProgress = CircularProgressDrawable(ctx)
            circularProgress.strokeWidth = 6f
            circularProgress.centerRadius = 50f
            circularProgress.setColorSchemeColors(Color.GRAY)
            circularProgress.start()
            return circularProgress
        }
    }
}