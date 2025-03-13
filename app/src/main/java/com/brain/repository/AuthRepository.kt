package com.brain.repository

import com.brain.model.LoginRequest
import com.brain.model.LoginResponse
import com.brain.model.RefreshTokenRequest
import com.brain.model.RefreshTokenResponse
import com.brain.model.RegisterRequest
import com.brain.model.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @Author FLE
 * @Company Brain Inc.
 * @Email brain30316@gmail.com
 */
interface AuthRepository {
    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("auth/signup")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @POST("auth/refreshToken")
    fun refreshToken(@Body token: RefreshTokenRequest): Call<RefreshTokenResponse>
}