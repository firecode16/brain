package com.brain.multimediaposts.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.brain.multimediaplayer.service.MediaPlayerService
import com.brain.multimediaposts.R
import com.brain.multimediaposts.adapters.GridItemAdapter
import com.brain.multimediaposts.model.User
import com.brain.multimediaposts.service.PostsService
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PostsDialogFragment : DialogFragment() {
    private lateinit var gridView: GridView
    private lateinit var btnPost: Button
    private lateinit var gridItemAdapter: GridItemAdapter

    companion object {
        const val MAX_ITEMS = 50
        private lateinit var user: User

        fun newInstance(user: User): PostsDialogFragment {
            this.user = user
            return PostsDialogFragment()
        }
    }

    private val pickMultipleMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(MAX_ITEMS)) { uris ->
            if (uris.isNotEmpty()) {
                gridItemAdapter = GridItemAdapter(requireContext(), uris)
                gridView.adapter = gridItemAdapter

                btnPost.isEnabled = true
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MediaPlayerService.isMuted(true)
        setStyle(STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar_Fullscreen)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.posts_dialog_fragment, container, false)

        val toolbar = view.findViewById<Toolbar>(R.id.dialogToolbar)
        gridView = view.findViewById(R.id.gridView)
        btnPost = view.findViewById(R.id.buttonPost)
        val btnPlus = view.findViewById<FloatingActionButton>(R.id.fabPlus)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp)
        toolbar.title = "Crear publicación"
        toolbar.setNavigationOnClickListener {
            onDismissListener()
        }

        btnPost.isEnabled = false
        btnPost.setOnClickListener(onPostListener)
        btnPlus.setOnClickListener(onPickListener)
        return view
    }

    private var onPickListener: View.OnClickListener = View.OnClickListener {
        pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
    }

    private fun onDismissListener() {
        MediaPlayerService.isMuted(false)
        MediaPlayerService.resumePlayerIndexCurrent()
        gridItemAdapter = GridItemAdapter()
        dismiss()
    }

    private var onPostListener: View.OnClickListener = View.OnClickListener {
        MediaPlayerService.isMuted(false)
        MediaPlayerService.resumePlayerIndexCurrent()
        PostsService.savePost(requireContext(), gridItemAdapter, user)
        PostsService.clean()
        gridItemAdapter = GridItemAdapter()
        dismiss()
    }

    override fun onStart() {
        super.onStart()
        MediaPlayerService.isMuted(true)
        MediaPlayerService.pauseCurrentPlayingVideo()
    }

    override fun onResume() {
        super.onResume()
        MediaPlayerService.isMuted(true)
        MediaPlayerService.pauseCurrentPlayingVideo()
    }

}