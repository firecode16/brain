package com.brain.util

import com.brain.util.Util.isTokenExpired
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 *
 * Use of the Token in Subsequent Requests
 * When you need to make an authenticated request, you can add the token in the Authorization header
 */
class AuthInterceptor(private val token: String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()

        if (token != "" && isTokenExpired(token)) {
            val newRequest: Request = originalRequest.newBuilder().header("Authorization", "Bearer $token").build()
            return chain.proceed(newRequest)
        }

        return chain.proceed(originalRequest)
    }
}