package com.ddt.smsalarm.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "filter")
data class FilterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val text: String
)
