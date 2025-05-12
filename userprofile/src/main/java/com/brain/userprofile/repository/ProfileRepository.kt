package com.brain.userprofile.repository

import com.brain.userprofile.model.ProfileResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ProfileRepository {
    @Multipart
    @PUT("profile/update/{userId}")
    fun updateProfile(@Path("userId") userId: Long, @Part file: MutableList<MultipartBody.Part>): Call<ProfileResponse>
}