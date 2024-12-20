package com.brain.service

import android.content.Context
import com.brain.multimediapuzzlesviewer.MultimediaPuzzlesViewer
import com.brain.multimediapuzzlesviewer.model.Poster
import com.brain.multimediaslider.impl.ItemClickListenerImpl
import com.brain.multimediaslider.model.Multimedia
import com.brain.util.Util.URL
import com.brain.util.Util.URL_PART

class OpenDialogSliderClickListenerService(
    private var context: Context,
    private var multimediaList: MutableList<Multimedia>
) : ItemClickListenerImpl {
    private lateinit var posterList: MutableList<Poster>
    private lateinit var model: Poster
    private val adjustUrl: String = URL + URL_PART

    override fun onItemSelected(itemPosition: Int, position: Int) {
        posterList = mutableListOf()

        multimediaList.forEach { item ->
            model = Poster()

            model.id = item.id
            model.contentType = item.contentType
            model.userName = item.title
            posterList.add(model)
        }

        MultimediaPuzzlesViewer.Builder(context, posterList, itemPosition, position, adjustUrl, "SLIDER").show()
    }
}