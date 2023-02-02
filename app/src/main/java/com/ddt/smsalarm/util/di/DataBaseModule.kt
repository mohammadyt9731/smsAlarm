package com.ddt.smsalarm.util.di

import android.content.Context
import androidx.room.Room
import com.ddt.smsalarm.data.db.AppDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideFilterDao(appDataBase: AppDataBase) = appDataBase.filterDao()

    @Provides
    @Singleton
    fun provideAppDataBase(@ApplicationContext context: Context): AppDataBase =
        Room.databaseBuilder(
            context,
            AppDataBase::class.java,
            "filter_data_base"
        ).fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
}