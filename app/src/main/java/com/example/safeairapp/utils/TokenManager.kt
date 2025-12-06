package com.example.safeairapp.utils

import android.content.Context
import android.content.SharedPreferences

class TokenManager private constructor(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    companion object {
        private const val PREFS_NAME = "SafeAirPrefs"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_SENSOR_TOKEN = "sensor_token"

        @Volatile
        private var INSTANCE: TokenManager? = null

        fun getInstance(context: Context): TokenManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: TokenManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    fun saveTokens(accessToken: String?, refreshToken: String?, sensorToken: String?) {
        prefs.edit().apply {
            accessToken?.let { putString(KEY_ACCESS_TOKEN, it) }
            refreshToken?.let { putString(KEY_REFRESH_TOKEN, it) }
            sensorToken?.let { putString(KEY_SENSOR_TOKEN, it) }
            apply()
        }
    }

    fun getAccessToken(): String? = prefs.getString(KEY_ACCESS_TOKEN, null)

    fun getRefreshToken(): String? = prefs.getString(KEY_REFRESH_TOKEN, null)

    fun getSensorToken(): String? = prefs.getString(KEY_SENSOR_TOKEN, null)

    fun clearTokens() {
        prefs.edit().apply {
            remove(KEY_ACCESS_TOKEN)
            remove(KEY_REFRESH_TOKEN)
            remove(KEY_SENSOR_TOKEN)
            apply()
        }
    }

    fun hasTokens(): Boolean {
        return getAccessToken() != null && getSensorToken() != null
    }
}


