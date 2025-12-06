package com.example.safeairapp.services

import android.util.Log
import com.example.safeairapp.api.TelemetryData
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.v2.callbacks.EventListener

class PubNubMessageListener(
    private val onNotificationReceived: (PubNubService.NotificationData) -> Unit,
    private val onTelemetryReceived: (String, TelemetryData) -> Unit
) : EventListener {

    private val TAG = "PubNubMessageListener"

    override fun message(pubnub: PubNub, result: PNMessageResult) {
        try {
            Log.d(TAG, "New message received")
            val json = result.message.asJsonObject

            val requestType = json["request_type"]?.asString

            when (requestType) {
                "send_alert" -> {
                    handleAlertMessage(json)
                }

                "send_telemetry" -> {
                    handleTelemetryMessage(json)
                }

                else -> {
                    Log.w(TAG, "Unknown request_type: $requestType")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing message: ${e.message}", e)

            val notificationData = PubNubService.NotificationData(
                title = "New Notification",
                message = result.message.toString(),
                status = "info",
                timestamp = System.currentTimeMillis()
            )
            onNotificationReceived(notificationData)
        }
    }

    private fun handleAlertMessage(json: com.google.gson.JsonObject) {
        val title = json["title"]?.asString
            ?: json["Title"]?.asString
            ?: "New Notification"

        val messageText = json["message"]?.asString
            ?: json["Message"]?.asString
            ?: json["text"]?.asString
            ?: json["Text"]?.asString
            ?: json.toString()

        val status = json["status"]?.asString
            ?: json["Status"]?.asString
            ?: "info"

        val notificationData = PubNubService.NotificationData(
            title = title,
            message = messageText,
            status = status,
            timestamp = System.currentTimeMillis()
        )

        onNotificationReceived(notificationData)
        Log.d(TAG, "Notification created: $title")
    }

    private fun handleTelemetryMessage(json: com.google.gson.JsonObject) {
        try {
            val sensorId = json["sensor_id"]?.asString
                ?: throw IllegalArgumentException("sensor_id is missing")

            val telemetry_aqi = json["aqi"]?.asInt
                ?: throw IllegalArgumentException("sensor_id is missing")

            val telemetryJson = json["telemetry"]?.asJsonObject
                ?: throw IllegalArgumentException("telemetry object is missing")

            val temperature = telemetryJson["temperature"]?.asDouble
                ?: throw IllegalArgumentException("temperature is missing")
            val humidity = telemetryJson["humidity"]?.asDouble
                ?: throw IllegalArgumentException("humidity is missing")
            val co2Level = telemetryJson["co2_level"]?.asDouble
                ?: throw IllegalArgumentException("co2_level is missing")
            val pm2Level = telemetryJson["pm2_level"]?.asDouble
                ?: throw IllegalArgumentException("pm2_level is missing")

            val telemetryData = TelemetryData(
                temperature = temperature,
                humidity = humidity,
                co2_level = co2Level,
                pm2_level = pm2Level,
                aqi = telemetry_aqi
            )

            onTelemetryReceived(sensorId, telemetryData)
            Log.d(TAG, "Telemetry received for sensor: $sensorId")
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing telemetry message: ${e.message}", e)
        }
    }
}

