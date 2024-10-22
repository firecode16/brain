package com.brain.multimediaslider.service

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView

/**
 * @Author FLE
 * @Company Brain Inc.
 * @Email hfredi35@gmail.com
 */
/*class SliderMediaPlayerService {
    companion object {
        enum class VolumeState { ON, OFF }

        // controlling playback state
        private lateinit var volumeState: VolumeState

        // for hold all players generated
        private var playersMap: MutableMap<Int, ExoPlayer> = mutableMapOf()

        // for hold current player
        private var currentPlayingVideo: Pair<Int, ExoPlayer>? = null

        private lateinit var exoPlayer: ExoPlayer
        private lateinit var dataSourceFactory: DataSource.Factory
        private lateinit var mediaSource: MediaSource
        private lateinit var mediaSourceFactory: MediaSource.Factory

        fun pauseAllPlayers() {
            playersMap.map {
                it.value.playWhenReady = false
                it.value.playbackState
            }
        }

        fun resumePlayerIndexCurrent(index: Int) {
            if (playersMap[index]?.playWhenReady != null) {
                playersMap[index]?.playWhenReady = true
                playersMap[index]?.playbackState
            }
        }

        fun prepareAllPlayers() {
            playersMap.map { it.value.prepare() }
        }

        fun releaseAllPlayers() {
            playersMap.map { it.value.release() }
        }

        // call when item recycled to improve performance
        fun releaseRecycledPlayers(index: Int) {
            playersMap[index]?.release()
        }

        // call when scroll to pause any playing player
        private fun pauseCurrentPlayingVideo() {
            if (currentPlayingVideo != null) {
                currentPlayingVideo?.second?.playWhenReady = false
            }
        }

        fun playIndexAndPausePreviousPlayer(index: Int) {
            if (playersMap[index]?.playWhenReady == false || playersMap[index]?.playWhenReady == null) {
                pauseCurrentPlayingVideo()
                if (playersMap[index]?.playWhenReady != null) {
                    playersMap[index]?.playWhenReady = true
                    currentPlayingVideo = Pair(index, playersMap[index]!!)
                }
            }
        }

        @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
        fun initPlayer(context: Context, url: String, itemIndex: Int? = null, autoPlay: Boolean = false, sliderPlayerView: PlayerView, progressBar: ProgressBar) {
            // first remove if the map contains the specified key
            if (!playersMap.containsKey(itemIndex)) {
                dataSourceFactory = DefaultHttpDataSource.Factory()
                mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(url))

                exoPlayer = ExoPlayer.Builder(context).build()
                exoPlayer.setMediaSource(mediaSource)
                exoPlayer.prepare()
                exoPlayer.playWhenReady = autoPlay
                playersMap.remove(itemIndex)
            }

            sliderPlayerView.visibility = View.VISIBLE
            // When changing track, retain the latest frame instead of showing a black screen
            sliderPlayerView.setKeepContentOnPlayerReset(true)
            // We'll show the controller, change to true if want controllers as pause and start
            sliderPlayerView.useController = true
            sliderPlayerView.requestFocus()
            // Bind the player to the view.
            sliderPlayerView.player = exoPlayer
            // after, associates the specified value with the specified key in the map.
            playersMap[itemIndex!!] = exoPlayer

            exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    when (playbackState) {
                        Player.STATE_BUFFERING -> progressBar.visibility = View.VISIBLE
                        Player.STATE_ENDED -> {
                            Log.e("STATE_ENDED:: ", "onPlaybackStateChanged: Video ended.")
                            exoPlayer.seekTo(0)
                        }

                        Player.STATE_IDLE -> Log.e("STATE_IDLE:: ", "onPlaybackStateChanged: Video idle.")

                        Player.STATE_READY -> progressBar.visibility = View.GONE
                        else -> Log.e("PLAY_STATE:: ", "NOT_FOUND")
                    }
                }
            })
        }
    }
}*/