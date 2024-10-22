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
        private var playerTrackMapDefault: MutableMap<Int, ExoPlayer> = mutableMapOf()
        private var playerTrackMapSlider: MutableMap<Int, ExoPlayer> = mutableMapOf()
        private var playersMap = mutableMapOf<String, MutableMap<Int, ExoPlayer>>()

        // for hold current player
        private var pairCurrentPlayerDefault: Pair<Int, ExoPlayer>? = null
        private var pairCurrentPlayerSlider: Pair<Int, ExoPlayer>? = null
        private var currentPlayingVideo: Pair<String, Pair<Int, ExoPlayer>>? = null

        private lateinit var exoPlayer: ExoPlayer
        private lateinit var dataSourceFactory: DataSource.Factory
        private lateinit var mediaSource: MediaSource

        private var pairIndexMedia: Pair<String, String>? = null

        fun pauseAllPlayers() {
            /*playersMap.map {
                it.value.playWhenReady = false
                it.value.playbackState
            }*/
        }

        fun resumePlayerIndexCurrent(index: Int) {
            /*if (playersMap[index]?.playWhenReady != null) {
                playersMap[index]?.playWhenReady = true
                playersMap[index]?.playbackState
            }*/
        }

        fun prepareAllPlayers() {
            //playersMap.map { it.value.prepare() }
        }

        fun releaseAllPlayers() {
            //playersMap.map { it.value.release() }
        }

        // call when item recycled to improve performance
        fun releaseRecycledPlayers(index: Int) {
            //playersMap[index]?.release()
        }

        // call when scroll to pause any playing player
        private fun pauseCurrentPlayingVideo(source: String) {
            /*if (currentPlayingVideo != null) {
                currentPlayingVideo?.second?.playWhenReady = false
            }*/
            if (currentPlayingVideo != null) {
                when (source) {
                    "DEFAULT" -> {
                        if (currentPlayingVideo?.first.equals(source)) {
                            currentPlayingVideo?.second?.second?.playWhenReady = false

                            /*if (pairCurrentPlayerSlider?.second?.playWhenReady == false) {
                                pairCurrentPlayerSlider!!.second.playWhenReady = true
                            }*/
                        }
                    }
                    "SLIDER" -> {
                        if (currentPlayingVideo?.first.equals(source)) {
                            currentPlayingVideo?.second?.second?.playWhenReady = false
                        }/* else if (currentPlayingVideo?.first.equals("DEFAULT")) {
                            pairCurrentPlayerSlider!!.second.playWhenReady = false
                            pairCurrentPlayerDefault!!.second.playWhenReady = true
                        }*/
                    }
                }
            }
        }

        fun playIndexAndPausePreviousPlayer(source: String, index: Int) {
            when (source) {
                "DEFAULT" -> {
                    if (playersMap["DEFAULT"]?.get(index)?.playWhenReady == false || playersMap["DEFAULT"]?.get(index)?.playWhenReady == null) {
                        if (currentPlayingVideo?.first.equals("DEFAULT")) {
                            currentPlayingVideo?.second?.second?.playWhenReady = false
                        }

                        if (playersMap["DEFAULT"]?.get(index)?.playWhenReady != null) {
                            playersMap["DEFAULT"]?.get(index)?.playWhenReady = true
                            pairCurrentPlayerDefault = Pair(index, playersMap["DEFAULT"]?.get(index)!!)
                            currentPlayingVideo = Pair("DEFAULT", pairCurrentPlayerDefault!!)

                            if (pairCurrentPlayerSlider?.second?.playWhenReady == true) {
                                pairCurrentPlayerSlider!!.second.playWhenReady = false
                            }
                        } else {
                            if (pairIndexMedia != null && pairIndexMedia!!.first == "SLIDER" && pairIndexMedia!!.second == "VideoOrAudio") {
                                if (pairCurrentPlayerSlider?.second?.playWhenReady == false) {
                                    pairCurrentPlayerSlider!!.second.playWhenReady = true
                                }
                            }
                        }
                    }
                }
                "SLIDER" -> {
                    if (playersMap["SLIDER"]?.get(index)?.playWhenReady == false || playersMap["SLIDER"]?.get(index)?.playWhenReady == null) {
                        if (currentPlayingVideo?.first.equals("SLIDER")) {
                            currentPlayingVideo?.second?.second?.playWhenReady = false
                        }

                        if (playersMap["SLIDER"]?.get(index)?.playWhenReady != null) {
                            playersMap["SLIDER"]?.get(index)?.playWhenReady = true
                            pairIndexMedia = Pair("SLIDER", "VideoOrAudio")
                            pairCurrentPlayerSlider = Pair(index, playersMap["SLIDER"]?.get(index)!!)
                            currentPlayingVideo = Pair("SLIDER", pairCurrentPlayerSlider!!)
                        } else {
                            pairIndexMedia = Pair("SLIDER", "Image")
                            if (pairCurrentPlayerSlider?.second?.playWhenReady == true) {
                                pairCurrentPlayerSlider!!.second.playWhenReady = false
                            }
                        }
                    }
                }
                else -> {}
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
                "DEFAULT" -> {
                    if (playerTrackMapDefault.containsKey(itemIndex)) {
                        playerTrackMapDefault.remove(itemIndex)
                    }
                    playerTrackMapDefault[itemIndex!!] = exoPlayer
                    playersMap[source] = playerTrackMapDefault

                    if (itemIndex == 0 && currentPlayingVideo == null) {
                        playersMap["DEFAULT"]?.get(itemIndex)?.playWhenReady = true
                        pairCurrentPlayerDefault = Pair(itemIndex, playersMap["DEFAULT"]?.get(itemIndex)!!)
                        currentPlayingVideo = Pair("DEFAULT", pairCurrentPlayerDefault!!)
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
                        pairIndexMedia = Pair("SLIDER", "VideoOrAudio")
                        pairCurrentPlayerSlider = Pair(itemIndex, playersMap["SLIDER"]?.get(itemIndex)!!)
                        currentPlayingVideo = Pair("SLIDER", pairCurrentPlayerSlider!!)
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