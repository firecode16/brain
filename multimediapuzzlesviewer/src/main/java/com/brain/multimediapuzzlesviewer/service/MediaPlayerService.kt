package com.brain.multimediapuzzlesviewer.service

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

internal class MediaPlayerService {
    companion object {
        private lateinit var exoPlayer: ExoPlayer
        private lateinit var dataSourceFactory: DataSource.Factory
        private lateinit var mediaSource: MediaSource
        private lateinit var mediaSourceFactory: MediaSource.Factory

        @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
        fun initPlayer(context: Context, url: String, position: Int? = null, autoPlay: Boolean = false, playerView: PlayerView, progressBar: ProgressBar) {
            dataSourceFactory = DefaultHttpDataSource.Factory()
            mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(url))
            mediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory)

            exoPlayer = ExoPlayer.Builder(context).setMediaSourceFactory(mediaSourceFactory).build()
            exoPlayer.addMediaSource(mediaSource)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = autoPlay
            exoPlayer.seekTo(0, 0)

            playerView.visibility = View.VISIBLE
            playerView.setShowNextButton(false)
            playerView.setShowPreviousButton(false)
            // We'll show the controller, change to true if want controllers as pause and start
            playerView.useController = true
            // When changing track, retain the latest frame instead of showing a black screen
            playerView.setKeepContentOnPlayerReset(true)
            playerView.player = exoPlayer

            exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    when (playbackState) {
                        Player.STATE_BUFFERING -> progressBar.visibility = View.VISIBLE
                        Player.STATE_ENDED -> {
                            Log.e("STATE_ENDED:: ", "onPlaybackStateChanged: Video ended.")
                            exoPlayer.seekTo(0, 0)
                        }

                        Player.STATE_IDLE -> Log.e(
                            "STATE_IDLE:: ", "onPlaybackStateChanged: Video idle."
                        )

                        Player.STATE_READY -> progressBar.visibility = View.GONE
                        else -> Log.e("PLAY_STATE:: ", "NOT_FOUND")
                    }
                }
            })
        }
    }
}