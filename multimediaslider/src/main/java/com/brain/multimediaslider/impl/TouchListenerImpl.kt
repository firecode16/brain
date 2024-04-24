package com.brain.multimediaslider.impl

import com.brain.multimediaslider.util.ActionTypes

interface TouchListenerImpl {
    fun onTouched(touched: ActionTypes)
}