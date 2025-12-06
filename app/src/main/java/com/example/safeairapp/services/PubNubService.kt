package com.example.safeairapp.services

import android.util.Log
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.v2.PNConfiguration
import com.example.safeairapp.api.TelemetryData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PubNubService {
    private var pubnub: PubNub? = null
    private val _notifications = MutableStateFlow<List<NotificationData>>(emptyList())
    val notifications: StateFlow<List<NotificationData>> = _notifications.asStateFlow()
    
    private val _telemetry = MutableStateFlow<TelemetryData?>(null)
    val telemetry: StateFlow<TelemetryData?> = _telemetry.asStateFlow()
    
    private var isInitialized = false

    private val TAG = "PubNubService"

    data class NotificationData(
        val title: String,
        val message: String,
        val status: String = "info",
        val timestamp: Long = System.currentTimeMillis()
    )
    
    /**
     * Creates and adds a notification to the list
     */
    private fun createNotification(notificationData: NotificationData) {
        val currentList = _notifications.value.toMutableList()
        currentList.add(0, notificationData)
        _notifications.value = currentList
        Log.d(TAG, "New notification created: ${notificationData.title}")
    }
    
    /**
     * Updates telemetry data
     */
    private fun updateTelemetry(sensorId: String, telemetryData: TelemetryData) {
        _telemetry.value = telemetryData
        Log.d(TAG, "Telemetry updated for sensor: $sensorId")
    }

    fun initialize(
        publishKey: String,
        subscribeKey: String,
        channelName: String,
        authToken: String,
        userId: String = "android-user"
    ) {

        Log.d(TAG, "INIT START")

        if (isInitialized && pubnub != null) {
            Log.d(TAG, "PubNubService already initialized, skipping...")
            return
        }

        try {
            disconnect()

            val config = PNConfiguration.builder(UserId(userId), subscribeKey) {
                this.publishKey = publishKey
                this.authToken = authToken
                this.userId = UserId(userId)

            }.build()

            pubnub = PubNub.create(config)
            val subscription = pubnub?.channel(channelName)?.subscription()

            // Create listener with callbacks
            val listener = PubNubMessageListener(
                onNotificationReceived = { notificationData ->
                    createNotification(notificationData)
                },
                onTelemetryReceived = { sensorId, telemetryData ->
                    updateTelemetry(sensorId, telemetryData)
                }
            )

            // Add listener for incoming messages
            subscription?.addListener(listener)

           subscription?.subscribe()

            isInitialized = true
            Log.d(TAG, "PubNub initialized and subscribed to channel: $channelName")
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing PubNub: ${e.message}", e)
            isInitialized = false
        }
    }


    fun disconnect() {
        pubnub?.unsubscribeAll()
        pubnub?.destroy()
        pubnub = null
        isInitialized = false
        Log.d(TAG, "PubNub disconnected")
    }

    fun isConnected(): Boolean {
        return pubnub != null
    }
}

