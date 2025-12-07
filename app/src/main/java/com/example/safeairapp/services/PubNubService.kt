package com.example.safeairapp.services

import android.util.Log
import com.example.safeairapp.api.TelemetryData
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.v2.PNConfiguration
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
    private var currentChannelName: String? = null

    private val TAG = "PubNubService"

    data class NotificationData(
        val title: String,
        val message: String,
        val status: String = "info",
        val timestamp: Long = System.currentTimeMillis(),
        val value: Int
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
            currentChannelName = channelName
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
        currentChannelName = null
        isInitialized = false
        Log.d(TAG, "PubNub disconnected")
    }

    /**
     * Gets the current channel name that PubNub is subscribed to
     */
    fun getCurrentChannelName(): String? {
        return currentChannelName
    }

    fun isConnected(): Boolean {
        return pubnub != null
    }

    fun publish(
        channelName: String,
        message: Map<String, Any>,
        callback: ((Boolean, String?) -> Unit)? = null
    ) {
        if (pubnub == null) {
            Log.e(TAG, "Cannot publish: PubNub not initialized")
            callback?.invoke(false, "PubNub not initialized")
            return
        }

        try {
            val channel = pubnub!!.channel(channelName)

            channel.publish(
                message = message
            ).async { result ->
                result.onFailure { exception ->
                    Log.e(TAG, "Publish failed: $exception")
                    callback?.invoke(false, "Offline")
                }.onSuccess { value ->
                    Log.d(
                        TAG,
                        "Published successfully to channel: $channelName, timetoken: ${value.timetoken}"
                    )
                    callback?.invoke(true, null)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error publishing message: ${e.message}", e)
            callback?.invoke(false, e.message)
        }
    }
}

