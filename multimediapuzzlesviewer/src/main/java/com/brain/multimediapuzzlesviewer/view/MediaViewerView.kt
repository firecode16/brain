package com.brain.multimediapuzzlesviewer.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.brain.multimediapuzzlesviewer.R
import com.brain.multimediapuzzlesviewer.model.Poster
import com.bumptech.glide.Glide

internal class MediaViewerView<T> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : RelativeLayout(context, attrs, defStyleAttr) {
    internal var onDismiss: (() -> Unit)? = null
    internal var overlayView: View? = null
        set(value) {
            field = value
            value?.let { rootContainer.addView(it) }
        }

    private val imageView: ImageView
    private var rootContainer: ViewGroup
    private var toolbar: Toolbar

    private var objList: List<Poster> = listOf()

    private var startPosition: Int = 0

    init {
        View.inflate(context, R.layout.multimedia_puzzles_viewer, this)

        rootContainer = findViewById(R.id.rootContainer)
        toolbar = findViewById(R.id.toolbarPuzzles)
        imageView = findViewById(R.id.imageView)

        (context as AppCompatActivity).setSupportActionBar(toolbar)
        context.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        context.supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            onDismiss?.invoke()
        }
    }

    internal fun setMultimedia(objList: List<Poster>, startPosition: Int) {
        this.objList = objList
        this.startPosition = startPosition
    }

    internal fun open(url: String) {
        context?.let {
            Glide.with(it).load(url + objList[startPosition].id).centerCrop().into(this.imageView)
        }
    }
}