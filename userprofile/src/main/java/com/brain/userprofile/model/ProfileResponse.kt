package com.brain.userprofile.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("result")
    @Expose
    var result: String,
    @SerializedName("message")
    @Expose
    var message: String,
)
