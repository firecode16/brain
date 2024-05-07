package com.brain.multimediapuzzlesviewer.model

import android.graphics.Color
import android.view.View

internal class BuilderData<T>(val mediaList: List<Poster>) {
    var backgroundColor = Color.BLACK
    var startPosition: Int = 0
    var overlayView: View? = null
}
