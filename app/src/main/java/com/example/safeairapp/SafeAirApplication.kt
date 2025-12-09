package com.example.safeairapp

import android.app.Application
import android.util.Log
import com.example.safeairapp.services.PubNubService
import com.example.safeairapp.utils.TokenManager

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

        initializePubNub()
    }

    fun initializePubNub() {
        try {
            val tokenManager = TokenManager.getInstance(this)
            val sensorToken = tokenManager.getSensorToken()
            
            // Only initialize if we have a sensor token
            if (sensorToken == null) {
                Log.w("SafeAirApplication", "Sensor token not available, skipping PubNub initialization")
                return
            }
            
            // TODO: Move these keys to BuildConfig or a secure configuration file
            pubNubService.initialize(
                publishKey = "pub-c-6b19366c-41f5-4c2e-acd2-72584e72cdca",
                subscribeKey = "sub-c-2cfb801c-715b-494b-b401-12764cf0ecfa",
                channelName = "sensor_0271a7bf-b4d6-4f74-95d9-4b83f80d2808_5eb81cf8-d129-11f0-86d2-4a0ab95da33d",
                authToken = "qEF2AkF0Gmk4aglDdHRsGQPoQ3Jlc6VEY2hhbqF4UHNlbnNvcl8wMjcxYTdiZi1iNGQ2LTRmNzQtOTVkOS00YjgzZjgwZDI4MDhfNWViODFjZjgtZDEyOS0xMWYwLTg2ZDItNGEwYWI5NWRhMzNkA0NncnCgQ3NwY6F4UHNlbnNvcl8wMjcxYTdiZi1iNGQ2LTRmNzQtOTVkOS00YjgzZjgwZDI4MDhfNWViODFjZjgtZDEyOS0xMWYwLTg2ZDItNGEwYWI5NWRhMzNkA0N1c3KgRHV1aWSgQ3BhdKVEY2hhbqBDZ3JwoENzcGOgQ3VzcqBEdXVpZKBEbWV0YaBEdXVpZGxhbmRyb2lkLXVzZXJDc2lnWCAuHWQHQDpwK2MAbJG94UMODLse7MRN6d7_rXnjIDnAYA==",
                userId = "android-user"
            )
            Log.d("SafeAirApplication", "PubNubService initialized successfully with sensor token")
        } catch (e: Exception) {
            Log.e("SafeAirApplication", "Failed to initialize PubNubService: ${e.message}", e)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        pubNubService.disconnect()
    }
}

