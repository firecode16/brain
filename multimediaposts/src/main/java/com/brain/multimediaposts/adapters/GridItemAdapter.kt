package com.brain.multimediaposts.adapters

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.BaseAdapter
import android.widget.ImageView
import com.brain.multimediaposts.R
import com.bumptech.glide.Glide

class GridItemAdapter(
    private var context: Context?,
    private var list: List<@JvmSuppressWildcards Uri>?
) : BaseAdapter() {
    constructor() : this(null, null)

    override fun getCount(): Int {
        return list!!.size
    }

    override fun getItem(position: Int): @JvmSuppressWildcards Uri {
        return list!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.grid_item_fragment, parent, false)

        val imageView: ImageView = view.findViewById(R.id.imageViewGrid)
        val iconView: ImageView = view.findViewById(R.id.playIcon)

        val contentResolver: ContentResolver = context!!.contentResolver
        val mimeType: String = contentResolver.getType(list!![position]).toString()

        if (getMimeType(mimeType)) {
            Glide.with(context!!).load(list!![position]).into(imageView)
        } else if (mimeType == "video/mp4") {
            iconView.visibility = View.VISIBLE
            Glide.with(context!!).load(list!![position]).into(imageView)
        }

        val type: String = MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(list!![position])).toString()
        println(type)
        return view
    }

    private fun getMimeType(mimeType: String): Boolean {
        return mimeType == "image/jpg" || mimeType == "image/jpeg" || mimeType == "image/png" || mimeType == "image/gif"
    }
}