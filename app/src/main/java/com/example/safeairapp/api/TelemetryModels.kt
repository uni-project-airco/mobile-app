package com.example.safeairapp.api

data class TelemetryData(
    val temperature: Double,
    val humidity: Double,
    val co2_level: Double,
    val pm2_level: Double,
    val aqi: Int
)

