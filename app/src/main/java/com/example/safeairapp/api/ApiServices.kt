package com.example.safeairapp.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiServices {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("telemetry/get_historical")
    suspend fun getHistoricalData(): Response<HistoryResponse>

    @POST("auth/register")
    suspend fun registerUser(@Body user: RegisterRequest): LoginResponse

}

