package com.example.safeairapp.api

data class HistoricalData(
    val avg_temperature: Double? = null,
    val avg_humidity: Double? = null,
    val avg_co2: Double? = null,
    val avg_pm25: Double? = null,
    val updated_at: String? = null,
    val sensor_id: String? = null
)
