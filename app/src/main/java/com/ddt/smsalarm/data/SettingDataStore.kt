package com.ddt.smsalarm.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ddt.smsalarm.data.model.Setting
import com.google.gson.Gson
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingDataStore @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private val vibratorPreferencesKey = booleanPreferencesKey("vibrator")
    private val maxVolumePreferencesKey = booleanPreferencesKey("maxVolume")
    private val settingPreferencesKey = stringPreferencesKey("setting")

    fun getSetting() = dataStore.data.map {
        Gson().fromJson(it[settingPreferencesKey], Setting::class.java) ?: Setting()
    }

    suspend fun storeSetting(setting: Setting) =
        dataStore.edit { it[settingPreferencesKey] = Gson().toJson(setting) }

    fun isVibratorEnable() = dataStore.data.map { it[vibratorPreferencesKey] ?: true }

    suspend fun storeIsVibratorEnable(isVibratorEnable: Boolean) =
        dataStore.edit { it[vibratorPreferencesKey] = isVibratorEnable }

    fun isMaxVolumeEnable() = dataStore.data.map { it[maxVolumePreferencesKey] ?: true }

    suspend fun storeIsMaxVolumeEnable(maxVolumeEnable: Boolean) =
        dataStore.edit { it[maxVolumePreferencesKey] = maxVolumeEnable }
}