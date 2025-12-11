package com.example.safeairapp

import android.app.Application
import android.util.Log
import com.example.safeairapp.services.PubNubService
import com.example.safeairapp.utils.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SafeAirApplication : Application() {

    companion object {
        lateinit var instance: SafeAirApplication
            private set
    }

    val pubNubService: PubNubService by lazy {
        PubNubService()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        com.example.safeairapp.utils.NotificationHelper.createNotificationChannel(this)

        initializePubNub()
    }

    fun initializePubNub() {
        try {
            val tokenManager = TokenManager.getInstance(this)
            CoroutineScope(Dispatchers.Main).launch {
                val sensorToken = tokenManager.getSensorToken()

                if (sensorToken == null) {
                    Log.w("SafeAirApplication", "Sensor token not available, skipping PubNub initialization")
                    return@launch
                }

                pubNubService.initialize(
                    publishKey = "pub-c-6b19366c-41f5-4c2e-acd2-72584e72cdca",
                    subscribeKey = "sub-c-2cfb801c-715b-494b-b401-12764cf0ecfa",
                    channelName = "sensor_0271a7bf-b4d6-4f74-95d9-4b83f80d2808_5eb81cf8-d129-11f0-86d2-4a0ab95da33d",
                    authToken = sensorToken,
                    userId = "android-user",
                    context = this@SafeAirApplication
                )
                Log.d("SafeAirApplication", "PubNubService initialized successfully with sensor token from DataStore")
            }
        } catch (e: Exception) {
            Log.e("SafeAirApplication", "Failed to initialize PubNubService: ${e.message}", e)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        pubNubService.disconnect()
    }
}

