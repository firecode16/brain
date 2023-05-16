package com.brain.impl;

import com.brain.model.MediaApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @Author FLE
 * @Company Brain Inc.
 * @Email hfredi35@gmail.com
 */
public interface ApiRestImpl {

    @GET("multimediaAll")
    Call<MediaApiResponse> getTopRatedMultimedia(@Query("page") int page, @Query("size") int size);
}
