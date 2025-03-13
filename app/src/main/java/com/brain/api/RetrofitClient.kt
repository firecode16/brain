package com.brain.api

import com.brain.repository.AuthRepository
import com.brain.util.AuthInterceptor
import com.brain.util.Util.BASE_AUTH_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Create OkHttpClient with the interceptor
    private fun provideOkHttpClient(token: String): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(token))
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    // Create Retrofit instance
    private fun provideRetrofit(token: String): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_AUTH_URL)
            .client(provideOkHttpClient(token)) // Add the OkHttp client with the interceptor
            .addConverterFactory(GsonConverterFactory.create())  // Use Gson for object conversion
            .build()
    }

    // You can create an ApiService here as well
    fun authApiService(token: String): AuthRepository {
        return provideRetrofit(token).create(AuthRepository::class.java)
    }
}