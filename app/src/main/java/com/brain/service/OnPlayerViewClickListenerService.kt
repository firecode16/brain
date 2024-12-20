package com.brain.service

import android.view.View
import com.brain.model.MediaContent
import com.brain.multimediapuzzlesviewer.MultimediaPuzzlesViewer
import com.brain.multimediapuzzlesviewer.model.Poster
import com.brain.util.Util.URL
import com.brain.util.Util.URL_PART

class OnPlayerViewClickListenerService(
    private var mediaContents: MutableList<MediaContent>,
    private var itemPosition: Int,
    private var position: Int,
    private var container: String
) : View.OnClickListener {
    private lateinit var posterList: MutableList<Poster>
    private lateinit var model: Poster
    private val adjustUrl: String = URL + URL_PART

    override fun onClick(v: View) {
        posterList = mutableListOf()

        mediaContents.forEach { item ->
            model = Poster()

            model.id = item._id
            model.userName = item.multimediaName
            model.contentType = item.contentType
            posterList.add(model)
        }

        MultimediaPuzzlesViewer.Builder(v.context, posterList, itemPosition, position, adjustUrl, container).show()
    }
}