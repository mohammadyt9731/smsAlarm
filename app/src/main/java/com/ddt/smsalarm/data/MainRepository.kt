package com.ddt.smsalarm.data

import com.ddt.smsalarm.data.db.FilterDao
import com.ddt.smsalarm.data.db.FilterEntity
import com.ddt.smsalarm.data.model.Setting
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val dataStore: SettingDataStore,
    private val dao: FilterDao
) {
    fun getSettingFlow(): Flow<Setting> = dataStore.getSettingFlow()

    suspend fun getSetting() = dataStore.getSetting()

    suspend fun storeISetting(setting: Setting) =
        dataStore.storeSetting(setting)

    suspend fun insertFilter(filterEntity: FilterEntity) = dao.insertFilter(filterEntity)

    suspend fun updateFilter(filterEntity: FilterEntity) = dao.updateFilter(filterEntity)

    suspend fun deleteFilter(filterEntity: FilterEntity) = dao.deleteFilter(filterEntity)

    fun getFilters() = dao.getFilters()

    suspend fun getFilter(id:Int) = dao.getFilter(id)
}


