package com.brain.multimediaplayer.model

import androidx.media3.exoplayer.ExoPlayer

data class Item(
    var itemPosition: Int,
    var position: Int?,
    var exoPlayer: ExoPlayer
)
