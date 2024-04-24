package com.brain.service

import android.content.Context
import android.util.Log
import android.view.View
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.brain.R
import com.brain.holders.MultimediaViewHolder

/**
 * @Author FLE
 * @Company Brain Inc.
 * @Email hfredi35@gmail.com
 */
class VideoPlayService {
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

        fun getThePlayIndexAndPausePreviousPlayer(index: Int) {
            if (playersMap[index]?.playWhenReady == false || playersMap[index]?.playWhenReady == null) {
                pauseCurrentPlayingVideo()
                if (playersMap[index]?.playWhenReady != null) {
                    playersMap[index]?.playWhenReady = true
                    currentPlayingVideo = Pair(index, playersMap[index]!!)
                }
            }
        }

        @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
        fun initPlayer(context: Context, url: String, itemIndex: Int? = null, autoPlay: Boolean = false, holder: MultimediaViewHolder) {
            dataSourceFactory = DefaultHttpDataSource.Factory()
            mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(url))
            mediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory)

            exoPlayer = ExoPlayer.Builder(context).setMediaSourceFactory(mediaSourceFactory).build()
            exoPlayer.addMediaSource(mediaSource)
            exoPlayer.seekTo(0)
            exoPlayer.playWhenReady = autoPlay
            exoPlayer.repeatMode = Player.REPEAT_MODE_ALL
            exoPlayer.prepare()

            // When changing track, retain the latest frame instead of showing a black screen
            holder.postMedia.setKeepContentOnPlayerReset(true)
            // We'll show the controller, change to true if want controllers as pause and start
            holder.postMedia.useController = false
            holder.postMedia.requestFocus()
            setVolumeControl(VolumeState.ON, holder)
            // Bind the player to the view.
            holder.postMedia.player = exoPlayer

            // add player with its index to map
            if (playersMap.containsKey(itemIndex)) {
                playersMap.remove(itemIndex)
            }
            if (itemIndex != null) {
                playersMap[itemIndex] = exoPlayer
            }

            exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    when (playbackState) {
                        Player.STATE_BUFFERING -> holder.progressBar.visibility = View.VISIBLE
                        Player.STATE_ENDED -> {
                            Log.e("STATE_ENDED:: ", "onPlaybackStateChanged: Video ended.")
                            exoPlayer.seekTo(0)
                        }
                        Player.STATE_IDLE -> Log.e("STATE_IDLE:: ", "onPlaybackStateChanged: Video idle.")
                        Player.STATE_READY -> holder.progressBar.visibility = View.GONE
                        else -> Log.e("PLAY_STATE:: ", "NOT_FOUND")
                    }
                }
            })
        }

        private fun getAnimateVolumeControl(holder: MultimediaViewHolder) {
            if (holder.volumeControl != null) {
                holder.volumeControl.bringToFront()
                if (volumeState == VolumeState.OFF) {
                    holder.volumeControl.setImageResource(R.drawable.ic_volume_off)
                } else if (volumeState == VolumeState.ON) {
                    holder.volumeControl.setImageResource(R.drawable.ic_volume_up)
                }
            }
        }

        private fun setVolumeControl(state: VolumeState, holder: MultimediaViewHolder) {
            volumeState = state
            if (state == VolumeState.OFF) {
                exoPlayer.volume = 0F
                getAnimateVolumeControl(holder)
            } else if (state == VolumeState.ON) {
                exoPlayer.volume = 1F
                getAnimateVolumeControl(holder)
            }
        }

        private fun getToggleVolume(holder: MultimediaViewHolder) {
            if (volumeState == VolumeState.OFF) {
                setVolumeControl(VolumeState.ON, holder)
            } else if (volumeState == VolumeState.ON) {
                setVolumeControl(VolumeState.OFF, holder)
            }
        }
    }
}