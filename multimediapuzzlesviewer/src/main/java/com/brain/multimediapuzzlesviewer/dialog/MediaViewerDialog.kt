package com.brain.multimediapuzzlesviewer.dialog

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AlertDialog
import androidx.core.view.WindowInsetsCompat.Type
import com.brain.multimediapuzzlesviewer.R
import com.brain.multimediapuzzlesviewer.model.BuilderData
import com.brain.multimediapuzzlesviewer.view.MediaViewerView

internal class MediaViewerDialog(
    context: Context,
    private val builderData: BuilderData,
    private val itemPosition: Int,
    private val position: Int,
    private val url: String,
    private val container: String
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

        hideSystemBars()
    }

    private fun setupViewerView() {
        mediaViewer.apply {
            setMultimedia(builderData.mediaList, itemPosition, position, url, container)
            onDismiss = { dialog.dismiss() }
        }
    }

    fun show() {
        dialog.show()
    }

    private fun hideSystemBars() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dialog.window!!.decorView.windowInsetsController!!.hide(Type.systemBars())
        }
    }
}