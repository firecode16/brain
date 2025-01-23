package com.brain.multimediaplayer.service

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.HttpDataSource
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.upstream.DefaultAllocator
import androidx.media3.ui.PlayerView
import com.brain.multimediaplayer.model.Item

/**
 * @author hfredi35@gmail.com
 * @company Brain Inc
 */
class MediaPlayerService {
    companion object {
        // for hold all players generated
        private var mediaPlayList: MutableList<Item> = mutableListOf()
        private var pairIndexMedia: Pair<Int, Int> = Pair(0, 0)

        // for hold current player
        private var currentPlayingVideo: Pair<Int, Pair<Int, ExoPlayer>>? = null

        fun resumePlayerIndexCurrent() {
            val lastItemPosition = pairIndexMedia.first
            val lastIndex = pairIndexMedia.second

            if (mediaPlayList.find { t -> t.itemPosition == lastItemPosition && t.position == lastIndex }?.exoPlayer?.playWhenReady == false) {
                mediaPlayList.find { t -> t.itemPosition == lastItemPosition && t.position == lastIndex }?.exoPlayer?.playWhenReady = true
            }
        }

        // call when item recycled to improve performance
        fun releasePlayer() {
            val lastItemPosition = pairIndexMedia.first
            val lastIndex = pairIndexMedia.second

            mediaPlayList.find { t -> t.itemPosition == lastItemPosition && t.position == lastIndex }?.exoPlayer?.stop()
            mediaPlayList.find { t -> t.itemPosition == lastItemPosition && t.position == lastIndex }?.exoPlayer?.release()
            pairIndexMedia = Pair(0, 0)
            currentPlayingVideo = null
            mediaPlayList.clear()
        }

        fun pauseCurrentPlayingVideo() {
            if (currentPlayingVideo != null) {
                currentPlayingVideo!!.second.second.playWhenReady = false
            }
        }

        fun isMuted(mute: Boolean) {
            if (currentPlayingVideo != null) {
                if (mute) {
                    currentPlayingVideo!!.second.second.volume = 0F
                } else {
                    currentPlayingVideo!!.second.second.volume = 1F
                }
            }
        }

        // call only when Open Dialog, Module: multimedia puzzles viewer.
        @SuppressLint("UnsafeOptInUsageError")
        fun prepareIndexesOfMultimediaWhenOpenDialog(itemPosition: Int, index: Int, playerView: PlayerView) {
            playerView.visibility = View.VISIBLE
            // When changing track, retain the latest frame instead of showing a black screen
            playerView.setKeepContentOnPlayerReset(true)
            // We'll show the controller, change to true if want controllers as pause and start
            playerView.useController = true
            playerView.setShowNextButton(false)
            playerView.setShowPreviousButton(false)

            playerView.player = null
            playerView.player = mediaPlayList.find { t -> t.itemPosition == itemPosition && t.position == index }?.exoPlayer
        }

        // call when scroll to pause any playing player
        fun playIndexWhenScrolledUpOrDownOrSliderAndPausePreviousPlayer(itemPosition: Int, index: Int) {
            if (mediaPlayList.find { t -> t.itemPosition == itemPosition && t.position == index }?.exoPlayer?.playWhenReady == false || mediaPlayList.find { t -> t.itemPosition == itemPosition && t.position == index }?.exoPlayer?.playWhenReady == null) {
                pauseCurrentPlayingVideo()

                if (mediaPlayList.find { t -> t.itemPosition == itemPosition && t.position == index }?.exoPlayer?.playWhenReady != null) {
                    mediaPlayList.find { t -> t.itemPosition == itemPosition && t.position == index }?.exoPlayer?.playWhenReady = true
                    currentPlayingVideo = Pair(itemPosition, Pair(index, mediaPlayList.find { t -> t.itemPosition == itemPosition && t.position == index }?.exoPlayer!!))
                }
            }
            pairIndexMedia = Pair(itemPosition, index)
        }

        @SuppressLint("UnsafeOptInUsageError")
        fun initPlayer(context: Context, url: String, position: Int? = null, itemPosition: Int? = null, autoPlay: Boolean = false, playerView: PlayerView?, progressBar: ProgressBar?) {
            val exoPlayer: ExoPlayer = ExoPlayer.Builder(context, renderersFactory(context)).setLoadControl(loadControl()).build()
            exoPlayer.setMediaSource(buildMediaSource(url))
            exoPlayer.prepare()
            exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
            exoPlayer.playWhenReady = autoPlay

            playerView?.visibility = View.VISIBLE
            progressBar?.visibility = View.VISIBLE
            // When changing track, retain the latest frame instead of showing a black screen
            playerView?.setKeepContentOnPlayerReset(true)
            // We'll show the controller, change to true if want controllers as pause and start
            playerView?.useController = false
            // Bind the player to the view.
            playerView?.player = null
            playerView?.player = exoPlayer

            // add player with its index to List
            if (mediaPlayList.isNotEmpty()) {
                mediaPlayList.removeIf { t -> t.itemPosition == itemPosition && t.position == position }
            }
            val item = Item(itemPosition!!, position, exoPlayer)
            mediaPlayList.add(item)

            if (itemPosition == 0 && currentPlayingVideo == null) {
                if (mediaPlayList[itemPosition].position == 0) {
                    mediaPlayList[itemPosition].exoPlayer.playWhenReady = true
                    currentPlayingVideo = Pair(itemPosition, Pair(position!!, mediaPlayList[itemPosition].exoPlayer))
                }
            }

            exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    when (playbackState) {
                        Player.STATE_BUFFERING -> progressBar?.visibility = View.VISIBLE
                        Player.STATE_ENDED -> {
                            Log.e("STATE_ENDED:: ", "onPlaybackStateChanged: Video ended.")
                            exoPlayer.seekTo(0)
                        }

                        Player.STATE_IDLE -> Log.e("STATE_IDLE:: ", "onPlaybackStateChanged: Video idle.")

                        Player.STATE_READY -> progressBar?.visibility = View.GONE
                        else -> Log.e("PLAY_STATE:: ", "NOT_FOUND")
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    val cause = error.cause
                    if (cause is HttpDataSource.HttpDataSourceException) {
                        // An HTTP error occurred.
                        val httpError = cause
                        Log.e("PLAYER_ERROR:: ", "onPlaybackException: ${httpError.message}")

                        if (httpError is HttpDataSource.InvalidResponseCodeException) {
                            Log.e("InvalidResponseCodeException():: ", "responseCodeException: ${httpError.responseCode}")
                        } else {
                            Log.e("Try_Calling_httpError():: ", "httpError_getCause(): ${httpError.message}")
                        }
                    }
                }
            })
        }

        @SuppressLint("UnsafeOptInUsageError")
        private fun buildMediaSource(url: String): MediaSource {
            val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
            val mediaSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(url))
            return mediaSource
        }

        @SuppressLint("UnsafeOptInUsageError")
        private fun renderersFactory(context: Context): DefaultRenderersFactory {
            return DefaultRenderersFactory(context).setEnableDecoderFallback(true).forceEnableMediaCodecAsynchronousQueueing().setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF)
        }

        @SuppressLint("UnsafeOptInUsageError")
        private fun loadControl(): DefaultLoadControl {
            return DefaultLoadControl.Builder()
                .setAllocator(DefaultAllocator(true, 16))
                .setBufferDurationsMs(
                    2500,                   //Minimum Video you want to buffer while Playing
                    5000,                   //Max Video you want to buffer during PlayBack
                    1500,             //Min Video you want to buffer before start Playing it
                    2000    //Min video You want to buffer when user resumes video
                )
                .setPrioritizeTimeOverSizeThresholds(true)
                .build()
        }
    }
}