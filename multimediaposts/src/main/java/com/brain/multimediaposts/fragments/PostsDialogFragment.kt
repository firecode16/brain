package com.brain.multimediaposts.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.brain.multimediaplayer.service.MediaPlayerService
import com.brain.multimediaposts.R
import com.bumptech.glide.Glide

class PostsDialogFragment : DialogFragment() {
    private lateinit var imageView: ImageView

    companion object {
        fun newInstance(): PostsDialogFragment {
            return PostsDialogFragment()
        }
    }

    private val pickMultipleMedia = registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
        if (uris.isNotEmpty()) {
            Log.d("PhotoPicker", "Selected URI: ${uris.size}")
            Glide.with(requireContext()).load(uris[0]).into(imageView)
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar_Fullscreen)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.posts_dialog_fragment, container, false)

        val toolbar = view.findViewById<Toolbar>(R.id.dialogToolbar)
        imageView = view.findViewById(R.id.imageView)
        val btnPick = view.findViewById<Button>(R.id.buttonPick)
        val btnCancel = view.findViewById<Button>(R.id.buttonCancel)
        val btnPost = view.findViewById<Button>(R.id.buttonPost)

        toolbar.setNavigationIcon(R.drawable.ic_plane_24)
        toolbar.title = "Crear Publicación"

        btnPick.setOnClickListener(onPickListener)
        btnCancel.setOnClickListener(onDismissListener)
        btnPost.setOnClickListener(onPostListener)
        return view
    }

    private var onPickListener: View.OnClickListener = View.OnClickListener {
        pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
    }

    private var onDismissListener: View.OnClickListener = View.OnClickListener {
        MediaPlayerService.Companion.resumePlayerIndexCurrent()
        dismiss()
    }

    private var onPostListener: View.OnClickListener = View.OnClickListener {
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