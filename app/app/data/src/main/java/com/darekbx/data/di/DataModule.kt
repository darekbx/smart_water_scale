package com.darekbx.data.di

import android.content.Context
import androidx.room.Room
import com.darekbx.data.WaterDatabase
import com.darekbx.data.dao.WaterLevelDao
import com.darekbx.data.dao.StatusDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideStatusDao(database: WaterDatabase): StatusDao {
        return database.statusDao()
    }

    @Provides
    fun provideLevelDao(database: WaterDatabase): WaterLevelDao {
        return database.levelDao()
    }

    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): WaterDatabase {
        return Room
            .databaseBuilder(
                appContext,
                WaterDatabase::class.java,
                WaterDatabase.DB_NAME
            )
            .createFromAsset("initial_database.db")
            .build()
    }
}
