package com.example.safeairapp.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "SafeAirPrefs")

class TokenManager private constructor(context: Context) {
    private val dataStore = context.dataStore

    companion object {
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val KEY_SENSOR_TOKEN = stringPreferencesKey("sensor_token")

        @Volatile
        private var INSTANCE: TokenManager? = null

        fun getInstance(context: Context): TokenManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: TokenManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    suspend fun saveTokens(accessToken: String?, refreshToken: String?, sensorToken: String?) {
        dataStore.edit { preferences ->
            accessToken?.let { preferences[KEY_ACCESS_TOKEN] = it }
            refreshToken?.let { preferences[KEY_REFRESH_TOKEN] = it }
            sensorToken?.let { preferences[KEY_SENSOR_TOKEN] = it }
        }
    }

    val accessToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[KEY_ACCESS_TOKEN]
    }

    val refreshToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[KEY_REFRESH_TOKEN]
    }

    val sensorToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[KEY_SENSOR_TOKEN]
    }

    suspend fun getAccessToken(): String? = accessToken.first()
    
    suspend fun getRefreshToken(): String? = refreshToken.first()
    
    suspend fun getSensorToken(): String? = sensorToken.first()

    suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_ACCESS_TOKEN)
            preferences.remove(KEY_REFRESH_TOKEN)
            preferences.remove(KEY_SENSOR_TOKEN)
        }
    }

    suspend fun hasTokens(): Boolean {
        val accessToken = getAccessToken()
        val sensorToken = getSensorToken()
        return accessToken != null && sensorToken != null
    }
}


