package com.brain.service

import android.content.Context
import android.util.Log
import android.view.View
import com.brain.R
import com.brain.holders.MultimediaViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource

/**
 * @Author FLE
 * @Company Brain Inc.
 * @Email hfredi35@gmail.com
 */
class VideoPlayService {
    companion object {
        private var currentPositionVideo: Int = 0

        enum class VolumeState {ON, OFF}
        // controlling playback state
        private lateinit var volumeState: VolumeState

        // for hold all players generated
        private var playersMap: MutableMap<Int, ExoPlayer> = mutableMapOf()

        // for hold current player
        private var currentPlayingVideo: Pair<Int, ExoPlayer>? = null

        private lateinit var exoPlayer: ExoPlayer
        private lateinit var dataSourceFactory: DataSource.Factory
        private lateinit var mediaSource: MediaSource
        private lateinit var mediaSourceFactory: MediaSourceFactory

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

        private fun getOneVideo(index: Int) {
            if (index == currentPositionVideo) {
                if (playersMap[index]?.isPlaying == false) {
                    playersMap[index]?.playWhenReady = true
                } else if (playersMap[index]?.isPlaying == true) {
                    return;
                }
            } else {
                if (playersMap[currentPositionVideo]?.isPlaying == true) {
                    playersMap[currentPositionVideo]?.playWhenReady = false
                }
            }
        }

        private fun getMultiVideo(index: Int) {
            if (playersMap[index]?.isPlaying == false) {
                pauseCurrentPlayingVideo()
                playersMap[index]?.playWhenReady = true
                currentPlayingVideo = Pair(index, playersMap[index]!!)
            }
        }

        fun getThePlayIndexAndPausePreviousPlayer(index: Int) {
            if (playersMap.size == 1) {
                getOneVideo(index)
            } else if (playersMap.size > 1) {
                getMultiVideo(index)
            }
        }

        fun initPlayer(context: Context, url: String, itemIndex: Int? = null, autoPlay: Boolean = false, holder: MultimediaViewHolder) {
            dataSourceFactory = DefaultHttpDataSource.Factory()
            mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(url))
            mediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory)

            exoPlayer = ExoPlayer.Builder(context).setMediaSourceFactory(mediaSourceFactory).build()
            exoPlayer.addMediaSource(mediaSource)
            exoPlayer.playWhenReady = autoPlay
            exoPlayer.repeatMode = Player.REPEAT_MODE_ALL
            exoPlayer.prepare()

            // When changing track, retain the latest frame instead of showing a black screen
            holder.videoPost.setKeepContentOnPlayerReset(true)
            // We'll show the controller, change to true if want controllers as pause and start
            holder.videoPost.useController = false
            holder.videoPost.requestFocus()
            setVolumeControl(VolumeState.OFF, holder)
            // Bind the player to the view.
            holder.videoPost.player = exoPlayer

            // add player with its index to map
            if (playersMap.containsKey(itemIndex)) {
                playersMap.remove(itemIndex)
            }
            if (itemIndex != null) {
                playersMap[itemIndex] = exoPlayer
                currentPositionVideo = itemIndex
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
                holder.volumeControl.bringToFront();
                if (volumeState == VolumeState.OFF) {
                    holder.volumeControl.setImageResource(R.drawable.ic_volume_off)
                } else if (volumeState == VolumeState.ON) {
                    holder.volumeControl.setImageResource(R.drawable.ic_volume_up)
                }
            }
        }

        private fun setVolumeControl(state: VolumeState, holder: MultimediaViewHolder) {
            volumeState = state;
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
            } else if(volumeState == VolumeState.ON) {
                setVolumeControl(VolumeState.OFF, holder)
            }
        }
    }
}