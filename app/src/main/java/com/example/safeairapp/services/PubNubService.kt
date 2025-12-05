package com.example.safeairapp.services

import android.util.Log
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PubNubService {
    private var pubnub: PubNub? = null
    private val _notifications = MutableStateFlow<List<NotificationData>>(emptyList())
    val notifications: StateFlow<List<NotificationData>> = _notifications.asStateFlow()

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
        channelName: String = "notifications",
        userId: String = "android-user"
    ) {
        try {
            val config = PNConfiguration(UserId(userId)).apply {
                this.publishKey = publishKey
                this.subscribeKey = subscribeKey
            }

            pubnub = PubNub(config)

            // Subscribe to the channel
            pubnub?.subscribe(
                channels = listOf(channelName),
                withPresence = true
            )

            // Add listener for incoming messages
            pubnub?.addListener(object : SubscribeCallback() {
                override fun message(pubnub: PubNub, message: PNMessageResult) {
                    try {
                        val json = message.message.asJsonObject  // JsonObject

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
                        val currentList = _notifications.value.toMutableList()
                        currentList.add(0, notificationData)
                        _notifications.value = currentList
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parsing message: ${e.message}", e)
                        // Fallback: create notification from raw message
                        val notificationData = NotificationData(
                            title = "New Notification",
                            message = message.message?.toString() ?: "No message content",
                            status = "info",
                            timestamp = System.currentTimeMillis()
                        )
                        val currentList = _notifications.value.toMutableList()
                        currentList.add(0, notificationData)
                        _notifications.value = currentList
                    }
                }

                override fun presence(pubnub: PubNub, presence: PNPresenceEventResult) {
                    Log.d(TAG, "Presence event: ${presence.event}")
                }

                override fun status(
                    pubnub: PubNub,
                    pnStatus: PNStatus
                ) {
                }
            })

            Log.d(TAG, "PubNub initialized and subscribed to channel: $channelName")
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing PubNub: ${e.message}", e)
        }
    }

    fun disconnect() {
        pubnub?.unsubscribeAll()
        pubnub?.destroy()
        pubnub = null
        Log.d(TAG, "PubNub disconnected")
    }

    fun isConnected(): Boolean {
        return pubnub != null
    }
}

