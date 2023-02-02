package com.ddt.smsalarm.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FilterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilter(filterEntity: FilterEntity)

    @Update
    suspend fun updateFilter(filterEntity: FilterEntity)

    @Delete
    suspend fun deleteFilter(filterEntity: FilterEntity)

    @Query("SELECT * FROM filter")
    fun getFilters(): Flow<List<FilterEntity>>

}