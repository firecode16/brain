package com.brain.repository

import com.brain.model.MediaApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @Author FLE
 * @Company Brain Inc.
 * @Email hfredi35@gmail.com
 */
interface PostHistoryRepository {
    @GET("allPost")
    fun getPosts(@Query("userId") userId: Long, @Query("page") page: Int, @Query("size") size: Int): Call<MediaApiResponse>
}