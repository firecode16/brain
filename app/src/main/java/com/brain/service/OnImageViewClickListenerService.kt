package com.brain.service

import android.view.View
import com.brain.model.MediaContent
import com.brain.multimediapuzzlesviewer.MultimediaPuzzlesViewer
import com.brain.multimediapuzzlesviewer.model.Poster
import com.brain.util.Util.BASE_URL
import com.brain.util.Util.URL_PART

class OnImageViewClickListenerService(private var mediaContents: MutableList<MediaContent>, private var position: Int) : View.OnClickListener {
    private lateinit var posterList: MutableList<Poster>
    private lateinit var model: Poster
    private val adjustUrl: String = BASE_URL + URL_PART

    override fun onClick(v: View) {
        posterList = mutableListOf()

        mediaContents.forEach { item ->
            model = Poster()

            model.id = item._id
            model.userName = item.multimediaName
            model.contentType = item.contentType
            posterList.add(model)
        }

        MultimediaPuzzlesViewer.Builder(v.context, posterList, position, position, adjustUrl, "IMAGE").show()
    }
}