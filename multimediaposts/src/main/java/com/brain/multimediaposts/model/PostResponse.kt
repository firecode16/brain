package com.brain.multimediaposts.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PostResponse(
    @SerializedName("result")
    @Expose
    var result: String,
    @SerializedName("page")
    @Expose
    var page: Int,
    @SerializedName("data")
    @Expose
    var data: Any,
    @SerializedName("totalItems")
    @Expose
    var totalItems: Int,
    @SerializedName("size")
    @Expose
    var size: Int
) : Serializable