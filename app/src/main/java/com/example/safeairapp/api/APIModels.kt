package com.example.safeairapp.api

data class LoginRequest(
    val username: String,
    val password: String
)

// reuse as RegisterResponse
data class LoginResponse(
    val msg: String? = null,
    val access_token: String? = null,
    val refresh_token: String? = null,
    val sensor_token: String? = null
)

data class HistoryResponse(
    val day: List<HistoricalData> ?= null,
    val week: List<HistoricalData> ?= null
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val confirm_password: String
)