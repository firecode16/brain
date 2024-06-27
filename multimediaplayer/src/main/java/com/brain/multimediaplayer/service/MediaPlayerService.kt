package com.brain.multimediaplayer.service

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
import com.brain.multimediaplayer.util.PlayerFlag

/**
 * @author hfredi35@gmail.com
 * @company Brain Inc
 */
class MediaPlayerService {
    companion object {
        //********** Vars for Player Dialog **********
        private lateinit var exoPlayerD: ExoPlayer
        private lateinit var dataSourceFactoryD: DataSource.Factory
        private lateinit var mediaSourceD: MediaSource
        private lateinit var mediaItemD: MediaItem
        private lateinit var mediaSourceFactoryD: MediaSource.Factory

        // for hold all players generated
        private var playersMapD: MutableMap<Int, ExoPlayer> = mutableMapOf()

        // for hold current player
        private var currentPlayingVideoD: Pair<Int, ExoPlayer>? = null
        //*********************************************

        //********** Vars for Player Slider **********
        private lateinit var exoPlayerS: ExoPlayer
        private lateinit var dataSourceFactoryS: DataSource.Factory
        private lateinit var mediaSourceS: MediaSource
        private lateinit var mediaItemS: MediaItem
        private lateinit var mediaSourceFactoryS: MediaSource.Factory

        // for hold all players generated
        private var playersMapS: MutableMap<Int, ExoPlayer> = mutableMapOf()

        // for hold current player
        private var currentPlayingVideoS: Pair<Int, ExoPlayer>? = null
        //*********************************************

        fun releaseAllPlayers(playerFlag: PlayerFlag, position: Int?) {
            when (playerFlag) {
                PlayerFlag.DIALOG -> {
                    playersMapD[position]?.pause()
                }
                PlayerFlag.SLIDER -> {
                    playersMapS.map { it.value.release() }
                }
                PlayerFlag.DEFAULT -> println("Player Not Found")
            }
        }

        private fun pauseCurrentPlayingVideo(playerFlag: PlayerFlag) {
            when (playerFlag) {
                PlayerFlag.DIALOG -> {
                    if (currentPlayingVideoD != null) {
                        currentPlayingVideoD?.second?.playWhenReady = false
                    }
                }
                PlayerFlag.SLIDER -> {
                    if (currentPlayingVideoS != null) {
                        currentPlayingVideoS?.second?.playWhenReady = false
                    }
                }
                PlayerFlag.DEFAULT -> println("Player Not Found")
            }
        }

        fun playIndexAndPausePreviousPlayer(index: Int, playerFlag: PlayerFlag) {
            when (playerFlag) {
                PlayerFlag.DIALOG -> {
                    if (playersMapD[index]?.playWhenReady == false || playersMapD[index]?.playWhenReady == null) {
                        pauseCurrentPlayingVideo(PlayerFlag.DIALOG)
                        if (playersMapD[index]?.playWhenReady != null) {
                            playersMapD[index]?.playWhenReady = true
                            currentPlayingVideoD = Pair(index, playersMapD[index]!!)
                        }
                    }
                }
                PlayerFlag.SLIDER -> {
                    if (playersMapS[index]?.playWhenReady == false || playersMapS[index]?.playWhenReady == null) {
                        pauseCurrentPlayingVideo(PlayerFlag.SLIDER)
                        if (playersMapS[index]?.playWhenReady != null) {
                            playersMapS[index]?.playWhenReady = true
                            currentPlayingVideoS = Pair(index, playersMapS[index]!!)
                        }
                    }
                }
                PlayerFlag.DEFAULT -> println("Player Not Found")
            }
        }

        @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
        fun initPlayer(context: Context, url: String, position: Int? = null, autoPlay: Boolean = false, playerView: PlayerView, progressBar: ProgressBar, playerFlag: PlayerFlag) {
            when (playerFlag) {
                PlayerFlag.DIALOG -> {
                    dataSourceFactoryD = DefaultHttpDataSource.Factory()
                    mediaItemD = MediaItem.fromUri(url)
                    mediaSourceD = ProgressiveMediaSource.Factory(dataSourceFactoryD).createMediaSource(mediaItemD)
                    mediaSourceFactoryD = DefaultMediaSourceFactory(dataSourceFactoryD)

                    exoPlayerD = ExoPlayer.Builder(context).setMediaSourceFactory(mediaSourceFactoryD).build()
                    exoPlayerD.addMediaSource(mediaSourceD)
                    exoPlayerD.prepare()
                    exoPlayerD.seekTo(0)

                    playerView.visibility = View.VISIBLE
                    playerView.requestFocus()
                    playerView.setShowNextButton(false)
                    playerView.setShowPreviousButton(false)
                    // We'll show the controller, change to true if want controllers as pause and start
                    playerView.useController = true
                    // When changing track, retain the latest frame instead of showing a black screen
                    playerView.setKeepContentOnPlayerReset(true)
                    playerView.player = exoPlayerD

                    // first remove if the map contains the specified key
                    if (playersMapD.containsKey(position)) {
                        playersMapD.remove(position)
                    }

                    // after, associates the specified value with the specified key in the map.
                    playersMapD[position!!] = exoPlayerD

                    exoPlayerD.addListener(object : Player.Listener {
                        override fun onPlaybackStateChanged(playbackState: Int) {
                            super.onPlaybackStateChanged(playbackState)
                            when (playbackState) {
                                Player.STATE_BUFFERING -> progressBar.visibility = View.VISIBLE
                                Player.STATE_ENDED -> {
                                    Log.e("STATE_ENDED:: ", "onPlaybackStateChanged: Video ended.")
                                    exoPlayerD.seekTo(0)
                                }

                                Player.STATE_IDLE -> Log.e("STATE_IDLE:: ", "onPlaybackStateChanged: Video idle.")

                                Player.STATE_READY -> progressBar.visibility = View.GONE
                                else -> Log.e("PLAY_STATE:: ", "NOT_FOUND")
                            }
                        }
                    })
                }
                PlayerFlag.SLIDER -> {
                    // first remove if the map contains the specified key
                    if (!playersMapS.containsKey(position)) {
                        dataSourceFactoryS = DefaultHttpDataSource.Factory()
                        mediaItemS = MediaItem.fromUri(url)
                        mediaSourceS = ProgressiveMediaSource.Factory(dataSourceFactoryS).createMediaSource(mediaItemS)
                        mediaSourceFactoryS = DefaultMediaSourceFactory(dataSourceFactoryS)

                        exoPlayerS = ExoPlayer.Builder(context).setMediaSourceFactory(mediaSourceFactoryS).build()
                        exoPlayerS.addMediaSource(mediaSourceS)
                        exoPlayerS.prepare()
                        exoPlayerS.playWhenReady = autoPlay
                        exoPlayerS.seekTo(0)

                        playersMapS.remove(position)
                    }

                    playerView.visibility = View.VISIBLE
                    playerView.requestFocus()
                    playerView.setShowNextButton(false)
                    playerView.setShowPreviousButton(false)
                    // We'll show the controller, change to true if want controllers as pause and start
                    playerView.useController = true
                    // When changing track, retain the latest frame instead of showing a black screen
                    playerView.setKeepContentOnPlayerReset(true)
                    playerView.player = exoPlayerS

                    // after, associates the specified value with the specified key in the map.
                    playersMapS[position!!] = exoPlayerS

                    exoPlayerS.addListener(object : Player.Listener {
                        override fun onPlaybackStateChanged(playbackState: Int) {
                            super.onPlaybackStateChanged(playbackState)
                            when (playbackState) {
                                Player.STATE_BUFFERING -> progressBar.visibility = View.VISIBLE
                                Player.STATE_ENDED -> {
                                    Log.e("STATE_ENDED:: ", "onPlaybackStateChanged: Video ended.")
                                    exoPlayerS.seekTo(0)
                                }

                                Player.STATE_IDLE -> Log.e("STATE_IDLE:: ", "onPlaybackStateChanged: Video idle.")

                                Player.STATE_READY -> progressBar.visibility = View.GONE
                                else -> Log.e("PLAY_STATE:: ", "NOT_FOUND")
                            }
                        }
                    })
                }
                PlayerFlag.DEFAULT -> println("Player Not Found")
            }
        }
    }
}