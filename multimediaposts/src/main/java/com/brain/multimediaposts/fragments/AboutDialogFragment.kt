package com.brain.multimediaposts.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.brain.multimediaplayer.service.MediaPlayerService
import com.brain.multimediaposts.R

class AboutDialogFragment : DialogFragment() {
    companion object {
        fun newInstance(): AboutDialogFragment {
            return AboutDialogFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar_Fullscreen)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.about_dialog_fragment, container, false)
        val btnDismissAbout = view.findViewById<Button>(R.id.btnDismissAbout)

        btnDismissAbout.setOnClickListener(onDismissListener)
        return view
    }

    private var onDismissListener: View.OnClickListener = View.OnClickListener {
        MediaPlayerService.Companion.resumePlayerIndexCurrent()
        dismiss()
    }

    override fun onStart() {
        super.onStart()
        MediaPlayerService.Companion.pauseCurrentPlayingVideo()
    }

    override fun onResume() {
        super.onResume()
        MediaPlayerService.Companion.pauseCurrentPlayingVideo()
    }
}