package com.brain.multimediaplayer.service

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
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
        private var trackHashMap: Map<Int, List<Item>> = mutableMapOf()
        private var pairIndexMedia: Pair<Int, Int> = Pair(0, 0)

        // for hold current player
        private var currentPlayingVideo: Pair<Int, Pair<Int, ExoPlayer>>? = null

        private lateinit var exoPlayer: ExoPlayer
        private lateinit var dataSourceFactory: DataSource.Factory
        private lateinit var mediaSource: MediaSource

        fun resumePlayerIndexCurrent() {
            val lastItemPosition = pairIndexMedia.first
            val lastIndex = pairIndexMedia.second

            if (trackHashMap[lastItemPosition]?.find { t -> t.position == lastIndex }?.exoPlayer?.playWhenReady == false) {
                trackHashMap[lastItemPosition]?.find { t -> t.position == lastIndex }?.exoPlayer?.playWhenReady = true
            }
        }

        // call when item recycled to improve performance
        fun releasePlayer() {
            val lastItemPosition = pairIndexMedia.first
            val lastIndex = pairIndexMedia.second

            trackHashMap[lastItemPosition]?.find { t -> t.position == lastIndex }?.exoPlayer?.stop()
            trackHashMap[lastItemPosition]?.find { t -> t.position == lastIndex }?.exoPlayer?.release()
            pairIndexMedia = Pair(0, 0)
            currentPlayingVideo = null
            mediaPlayList.clear()
            trackHashMap = mutableMapOf()
        }

        fun pauseCurrentPlayingVideo() {
            if (currentPlayingVideo != null) {
                currentPlayingVideo!!.second.second.playWhenReady = false
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
            playerView.player = trackHashMap[itemPosition]?.find { t -> t.position == index }?.exoPlayer
        }

        // call when scroll to pause any playing player
        fun playIndexWhenScrolledUpOrDownOrSliderAndPausePreviousPlayer(itemPosition: Int, index: Int) {
            if (trackHashMap[itemPosition]?.find { t -> t.position == index }?.exoPlayer?.playWhenReady == false || trackHashMap[itemPosition]?.find { t -> t.position == index }?.exoPlayer?.playWhenReady == null) {
                pauseCurrentPlayingVideo()

                if (trackHashMap[itemPosition]?.find { t -> t.position == index }?.exoPlayer?.playWhenReady != null) {
                    trackHashMap[itemPosition]?.find { t -> t.position == index }?.exoPlayer?.playWhenReady = true
                    currentPlayingVideo = Pair(itemPosition, Pair(index, trackHashMap[itemPosition]?.find { t -> t.position == index }?.exoPlayer!!))
                }
            }
            pairIndexMedia = Pair(itemPosition, index)
        }

        @SuppressLint("UnsafeOptInUsageError")
        fun initPlayer(context: Context, url: String, position: Int? = null, itemPosition: Int? = null, autoPlay: Boolean = false, playerView: PlayerView?, progressBar: ProgressBar?) {
            dataSourceFactory = DefaultHttpDataSource.Factory()
            mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(url))

            exoPlayer = ExoPlayer.Builder(context).build()
            exoPlayer.setMediaSource(mediaSource)
            exoPlayer.prepare()
            exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
            exoPlayer.playWhenReady = autoPlay

            playerView?.visibility = View.VISIBLE
            // When changing track, retain the latest frame instead of showing a black screen
            playerView?.setKeepContentOnPlayerReset(true)
            // We'll show the controller, change to true if want controllers as pause and start
            playerView?.useController = false
            playerView?.requestFocus()
            // Bind the player to the view.
            playerView?.player = exoPlayer

            // add player with its index to List
            if (mediaPlayList.isNotEmpty()) {
                mediaPlayList.removeIf { t -> t.itemPosition == itemPosition && t.position == position }
            }
            val item = Item(itemPosition!!, position, exoPlayer)
            mediaPlayList.add(item)

            trackHashMap = mediaPlayList.groupBy { it.itemPosition }

            if (itemPosition == 0 && currentPlayingVideo == null) {
                if (trackHashMap[itemPosition]?.stream()?.findAny()?.get()?.position == 0) {
                    trackHashMap[itemPosition]?.stream()?.findAny()?.get()?.exoPlayer?.playWhenReady = true
                    currentPlayingVideo = Pair(itemPosition, Pair(position!!, trackHashMap[itemPosition]?.stream()?.findAny()?.get()!!.exoPlayer))
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
            })
        }
    }
}