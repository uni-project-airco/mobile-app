package com.example.safeairapp.services

import android.util.Log
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.callbacks.EventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PubNubService {
    private var pubnub: PubNub? = null
    private val _notifications = MutableStateFlow<List<NotificationData>>(emptyList())
    val notifications: StateFlow<List<NotificationData>> = _notifications.asStateFlow()
    private var isInitialized = false

    private val TAG = "PubNubService"

    data class NotificationData(
        val title: String,
        val message: String,
        val status: String = "info",
        val timestamp: Long = System.currentTimeMillis()
    )

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

            // Add listener for incoming messages
            subscription?.addListener(object : EventListener {

                override fun message(pubnub: PubNub, result: PNMessageResult) {
                    try {
                        Log.d(TAG, "New MSG")
                        val json = result.message.asJsonObject  // JsonObject

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

                        val notificationData = NotificationData(
                            title = title,
                            message = messageText,
                            status = status,
                            timestamp = System.currentTimeMillis()
                        )

                        // Update notification list
                        val currentList = _notifications.value.toMutableList()
                        currentList.add(0, notificationData)
                        _notifications.value = currentList

                        Log.d(TAG, "New notification received: $title")
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parsing message: ${e.message}", e)

                        val notificationData = NotificationData(
                            title = "New Notification",
                            message = result.message.toString(),
                            status = "info",
                            timestamp = System.currentTimeMillis()
                        )

                        val currentList = _notifications.value.toMutableList()
                        currentList.add(0, notificationData)
                        _notifications.value = currentList
                    }
                }
            })

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

