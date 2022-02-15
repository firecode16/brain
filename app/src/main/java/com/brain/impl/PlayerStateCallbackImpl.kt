package com.brain.impl

import com.google.android.exoplayer2.Player

/**
 * @Author FLE
 * @Company Brain Inc.
 * @Email hfredi35@gmail.com
 */
interface PlayerStateCallbackImpl {
    // Callback to when the PlayerView has fetched the duration of video
    fun onVideoDurationRetrieved(duration: Long, player: Player)
    fun onVideoBuffering(player: Player)
    fun onStartedPlaying(player: Player)
    fun onFinishedPlaying(player: Player)
}