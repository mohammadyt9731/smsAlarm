package com.ddt.smsalarm.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FilterEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun filterDao(): FilterDao
}