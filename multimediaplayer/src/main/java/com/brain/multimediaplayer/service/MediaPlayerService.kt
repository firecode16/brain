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

/**
 * @author hfredi35@gmail.com
 * @company Brain Inc
 */
class MediaPlayerService {
    companion object {
        // for hold all players generated
        private var playerTrackMapSingle: MutableMap<Int, ExoPlayer> = mutableMapOf()
        private var playerTrackMapSlider: MutableMap<Int, ExoPlayer> = mutableMapOf()
        private var playersMap = mutableMapOf<String, MutableMap<Int, ExoPlayer>>()

        // for hold current player
        private var pairCurrentPlayerSingle: Pair<Int, ExoPlayer>? = null
        private var pairCurrentPlayerSlider: Pair<Int, ExoPlayer>? = null
        private var currentPlayingVideo: Pair<String, Pair<Int, ExoPlayer>>? = null

        private lateinit var exoPlayer: ExoPlayer
        private lateinit var dataSourceFactory: DataSource.Factory
        private lateinit var mediaSource: MediaSource
        private var pairIndexMedia: Pair<Int, String>? = null

        fun pauseAllPlayers() {
            if (currentPlayingVideo != null) {
                currentPlayingVideo!!.second.second.playWhenReady = false
                currentPlayingVideo!!.second.second.playbackState
            }
        }

        fun resumePlayerIndexCurrent(sourceType: String, index: Int) {
            when (sourceType) {
                "SINGLE" -> {
                    playersMap["SINGLE"]?.get(index)?.playWhenReady  = true
                }
                "SLIDER" -> {
                    if (pairIndexMedia != null) {
                        val lastIndex = pairIndexMedia!!.first
                        playersMap["SLIDER"]?.get(lastIndex)?.playWhenReady  = true
                    }
                }
            }
        }

        fun prepareAllPlayers(sourceType: String, index: Int) {
            when (sourceType) {
                "SINGLE" -> {
                    playersMap["SINGLE"]?.get(index)?.prepare()
                }
                "SLIDER" -> {
                    if (pairIndexMedia != null) {
                        val lastIndex = pairIndexMedia!!.first
                        playersMap["SLIDER"]?.get(lastIndex)?.prepare()
                    }
                }
            }
        }

        // call when item recycled to improve performance
        fun releaseAllPlayers(sourceType: String, index: Int) {
            when (sourceType) {
                "SINGLE" -> {
                    playersMap["SINGLE"]?.get(index)?.release()
                }
                "SLIDER" -> {
                    if (pairIndexMedia != null) {
                        val lastIndex = pairIndexMedia!!.first
                        playersMap["SLIDER"]?.get(lastIndex)?.release()
                    }
                }
            }
        }

        // call when scroll to pause any playing player
        fun playIndexAndPausePreviousPlayer(source: String, contentType: String, index: Int) {
            when (source) {
                "SINGLE" -> {
                    if (playersMap["SINGLE"]?.get(index)?.playWhenReady == false || playersMap["SINGLE"]?.get(index)?.playWhenReady == null) {
                        if (currentPlayingVideo?.first.equals("SINGLE")) {
                            currentPlayingVideo?.second?.second?.playWhenReady = false
                        }

                        if (playersMap["SINGLE"]?.get(index)?.playWhenReady != null) {
                            playersMap["SINGLE"]?.get(index)?.playWhenReady = true
                            pairCurrentPlayerSingle = Pair(index, playersMap["SINGLE"]?.get(index)!!)
                            currentPlayingVideo = Pair("SINGLE", pairCurrentPlayerSingle!!)

                            if (pairCurrentPlayerSlider?.second?.playWhenReady == true) {
                                pairCurrentPlayerSlider!!.second.playWhenReady = false
                            }
                        }
                    }
                }
                "SLIDER" -> {
                    when (contentType) {
                        "video/audio" -> {
                            pairIndexMedia = Pair(index, "video/audio")
                        }
                        "image" -> {
                            pairIndexMedia = Pair(index, "image")
                        }
                    }

                    if (pairIndexMedia != null) {
                        val lastIndex = pairIndexMedia!!.first
                        if (playersMap["SLIDER"]?.get(lastIndex)?.playWhenReady == false || playersMap["SLIDER"]?.get(lastIndex)?.playWhenReady == null) {
                            if (currentPlayingVideo?.first.equals("SLIDER")) {
                                currentPlayingVideo?.second?.second?.playWhenReady = false
                            }

                            if (playersMap["SLIDER"]?.get(lastIndex)?.playWhenReady != null) {
                                playersMap["SLIDER"]?.get(lastIndex)?.playWhenReady = true
                                pairCurrentPlayerSlider = Pair(lastIndex, playersMap["SLIDER"]?.get(lastIndex)!!)
                                currentPlayingVideo = Pair("SLIDER", pairCurrentPlayerSlider!!)

                                if (pairCurrentPlayerSingle?.second?.playWhenReady == true) {
                                    pairCurrentPlayerSingle!!.second.playWhenReady = false
                                }
                            } else {
                                if (pairCurrentPlayerSingle?.second?.playWhenReady == true) {
                                    pairCurrentPlayerSingle!!.second.playWhenReady = false
                                }
                            }
                        }
                    }
                }
            }
        }

        @SuppressLint("UnsafeOptInUsageError")
        fun initPlayer(context: Context, url: String, itemIndex: Int? = null, source: String, autoPlay: Boolean = false, playerView: PlayerView?, progressBar: ProgressBar?) {
            dataSourceFactory = DefaultHttpDataSource.Factory()
            mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(url))

            exoPlayer = ExoPlayer.Builder(context).build()
            exoPlayer.setMediaSource(mediaSource)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = autoPlay

            playerView?.visibility = View.VISIBLE
            // When changing track, retain the latest frame instead of showing a black screen
            playerView?.setKeepContentOnPlayerReset(true)
            // We'll show the controller, change to true if want controllers as pause and start
            playerView?.useController = false
            playerView?.requestFocus()
            // Bind the player to the view.
            playerView?.player = exoPlayer

            // add player with its index to map
            when (source) {
                "SINGLE" -> {
                    if (playerTrackMapSingle.containsKey(itemIndex)) {
                        playerTrackMapSingle.remove(itemIndex)
                    }
                    playerTrackMapSingle[itemIndex!!] = exoPlayer
                    playersMap[source] = playerTrackMapSingle

                    if (itemIndex == 0 && currentPlayingVideo == null) {
                        playersMap["SINGLE"]?.get(itemIndex)?.playWhenReady = true
                        pairCurrentPlayerSingle = Pair(itemIndex, playersMap["SINGLE"]?.get(itemIndex)!!)
                        currentPlayingVideo = Pair("SINGLE", pairCurrentPlayerSingle!!)
                    }
                }
                "SLIDER" -> {
                    if (playerTrackMapSlider.containsKey(itemIndex)) {
                        playerTrackMapSlider.remove(itemIndex)
                    }
                    playerTrackMapSlider[itemIndex!!] = exoPlayer
                    playersMap[source] = playerTrackMapSlider

                    if (itemIndex == 0 && currentPlayingVideo == null) {
                        playersMap["SLIDER"]?.get(itemIndex)?.playWhenReady = true
                        pairCurrentPlayerSlider = Pair(itemIndex, playersMap["SLIDER"]?.get(itemIndex)!!)
                        currentPlayingVideo = Pair("SLIDER", pairCurrentPlayerSlider!!)
                        pairIndexMedia = Pair(itemIndex, "video/audio")
                    }
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