package com.ddt.smsalarm.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ddt.smsalarm.data.model.Setting
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingDataStore @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private val settingPreferencesKey = stringPreferencesKey("setting")

    fun getSettingFlow() = dataStore.data.map {
        Gson().fromJson(it[settingPreferencesKey], Setting::class.java) ?: Setting()
    }

    suspend fun getSetting() = dataStore.data.map {
        Gson().fromJson(it[settingPreferencesKey], Setting::class.java) ?: Setting()
    }.first()

    suspend fun storeSetting(setting: Setting) =
        dataStore.edit { it[settingPreferencesKey] = Gson().toJson(setting,Setting::class.java) }
}