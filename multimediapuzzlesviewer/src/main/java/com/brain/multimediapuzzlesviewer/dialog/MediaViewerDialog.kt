package com.brain.multimediapuzzlesviewer.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.brain.multimediapuzzlesviewer.R
import com.brain.multimediapuzzlesviewer.model.BuilderData
import com.brain.multimediapuzzlesviewer.model.Poster
import com.brain.multimediapuzzlesviewer.view.MediaViewerView

internal class MediaViewerDialog<T>(
    context: Context,
    private val builderData: BuilderData<Poster>,
    private val url: String
) {
    private val dialog: AlertDialog
    private val mediaViewer: MediaViewerView<T> = MediaViewerView(context)

    init {
        setupViewerView()

        dialog = AlertDialog
            .Builder(context, R.style.MediaViewerDialog_NoStatusBar)
            .setView(mediaViewer)
            .create()
            .apply {
                setOnShowListener { mediaViewer.open(url) }
            }
    }

    private fun setupViewerView() {
        mediaViewer.apply {
            setBackgroundColor(builderData.backgroundColor)
            setMultimedia(builderData.mediaList, builderData.startPosition, url)
            onDismiss = { dialog.dismiss() }
        }
    }

    fun show() {
        dialog.show()
    }
}