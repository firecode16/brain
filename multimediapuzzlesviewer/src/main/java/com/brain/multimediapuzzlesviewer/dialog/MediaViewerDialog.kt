package com.brain.multimediapuzzlesviewer.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.brain.multimediapuzzlesviewer.R
import com.brain.multimediapuzzlesviewer.model.BuilderData
import com.brain.multimediapuzzlesviewer.model.Poster
import com.brain.multimediapuzzlesviewer.view.MediaViewerView

internal class MediaViewerDialog(
    context: Context,
    private val builderData: BuilderData<Poster>,
    private val itemPosition: Int,
    private val position: Int,
    private val url: String
) {
    private val dialog: AlertDialog
    private val mediaViewer: MediaViewerView = MediaViewerView(context)

    init {
        setupViewerView()

        dialog = AlertDialog
            .Builder(context, R.style.MediaViewerDialog_NoStatusBar)
            .setView(mediaViewer)
            .create()
            .apply {
                setOnShowListener { mediaViewer.open() }
            }
    }

    private fun setupViewerView() {
        mediaViewer.apply {
            setMultimedia(builderData.mediaList, itemPosition, position, url)
            onDismiss = { dialog.dismiss() }
        }
    }

    fun show() {
        dialog.show()
    }
}