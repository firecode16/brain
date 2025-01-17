package com.brain.multimediaposts.model

import android.net.Uri
import java.io.Serializable

class Post : Serializable {
    var collectionId: Long = 0L
    var fileId: Long = 0L
    lateinit var filePath: Uri
    var comments: MutableList<Comments> = mutableListOf()
    var mimeType: String = ""
    var type: String = ""
    var share: Long = 0L
    var likes: Long = 0L

    override fun toString(): String {
        return "{collectionId:$collectionId, fileId:$fileId, filePath:'${filePath.path}', comments:$comments, mimeType:'$mimeType', type:'$type', share:$share, likes:$likes}"
    }
}