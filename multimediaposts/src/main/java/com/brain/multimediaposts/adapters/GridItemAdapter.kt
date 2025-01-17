package com.brain.multimediaposts.adapters

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.brain.multimediaposts.R
import com.brain.multimediaposts.model.Post
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import kotlin.math.abs

class GridItemAdapter(
    private var context: Context?,
    private var list: List<@JvmSuppressWildcards Uri>?
) : BaseAdapter() {
    constructor() : this(null, null)

    private lateinit var post: Post
    private var listPost: MutableList<Post> = mutableListOf()
    private var fileId: Long = 0L

    override fun getCount(): Int {
        return list!!.size
    }

    override fun getItem(position: Int): @JvmSuppressWildcards Uri {
        return list!![position]
    }

    override fun getItemId(position: Int): Long {
        return list!![position].path.hashCode().toLong()
    }

    fun getPost(): MutableList<Post> {
        val uniqueId: Long = getUID()
        listPost.forEach { model ->
            model.collectionId = uniqueId
        }
        return listPost
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.grid_item_fragment, parent, false)

        val imageView: ImageView = view.findViewById(R.id.imageViewGrid)
        val iconView: ImageView = view.findViewById(R.id.playIcon)

        val contentResolver: ContentResolver = context!!.contentResolver
        val mimeType: String = contentResolver.getType(list!![position]).toString()

        post = Post()
        this.fileId = getItemId(position)

        if (getMimeType(mimeType)) {
            Glide.with(context!!).load(list!![position]).into(imageView)
            val type: String = mimeType.replaceRange(0, 6, ".")

            if (listPost.isNotEmpty()) {
                listPost.removeIf { t -> t.fileId == this.fileId }
            }

            post.fileId = this.fileId
            post.filePath = list!![position]
            post.mimeType = mimeType
            post.type = type
            listPost.add(post)
        } else if (mimeType == "video/mp4" || mimeType == "audio/mp3") {
            iconView.visibility = View.VISIBLE
            Glide.with(context!!).load(list!![position]).into(imageView)
            val type: String = mimeType.replaceRange(0, 6, ".")

            if (listPost.isNotEmpty()) {
                listPost.removeIf { t -> t.fileId == this.fileId }
            }

            post.fileId = this.fileId
            post.filePath = list!![position]
            post.mimeType = mimeType
            post.type = type
            listPost.add(post)
        }
        return view
    }

    private fun getMimeType(mimeType: String): Boolean {
        return mimeType == "image/jpg" || mimeType == "image/jpeg" || mimeType == "image/png" || mimeType == "image/gif"
    }

    private fun getUID(): Long {
        return abs((0..999999999999).random())
    }

    @SuppressLint("Recycle")
    fun getMultipartFile(uri: Uri, mimeType: String): MultipartBody.Part {
        var projection: Array<String>? = null

        when (mimeType) {
            "image/jpg", "image/jpeg", "image/png", "image/gif" -> {
                projection = arrayOf(MediaStore.Images.Media.DATA)
            }

            "video/mp4" -> {
                projection = arrayOf(MediaStore.Video.Media.DATA)
            }

            "audio/mp3" -> {
                projection = arrayOf(MediaStore.Audio.Media.DATA)
            }

            else -> println("MIME Not found !")
        }

        val cursor: Cursor? = context!!.contentResolver.query(uri, projection, null, null, null)
        lateinit var filePart: MultipartBody.Part

        if (cursor != null) {
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(projection!![0])
            val file = File(cursor.getString(columnIndex))

            val requestBody = file.asRequestBody(mimeType.toMediaTypeOrNull()) // put MIME correctly
            filePart = MultipartBody.Part.createFormData("file", file.name, requestBody)
        }

        return filePart
    }
}