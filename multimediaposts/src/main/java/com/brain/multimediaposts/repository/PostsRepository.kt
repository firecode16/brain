package com.brain.multimediaposts.repository

import com.brain.multimediaposts.model.PostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface PostsRepository {
    @Multipart
    @POST("save/posts")
    fun savePost(@PartMap partMap: MutableMap<String, RequestBody>, @Part file: MutableList<MultipartBody.Part>): Call<PostResponse>
}