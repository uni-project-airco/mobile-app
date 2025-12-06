package com.example.safeairapp.api

data class HistoricalData(
    val avg_temperature: Int ?= null,
    val avg_humidity: Int ?= null,
    val avg_co2: Int ?= null,
    val avg_pm25: Int ?= null,
    val updated_at: String ?= null
)
