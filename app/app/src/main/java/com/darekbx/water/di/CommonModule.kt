package com.darekbx.water.di

import com.darekbx.data.dao.LevelDao
import com.darekbx.data.dao.StatusDao
import com.darekbx.water.repository.local.LevelRepository
import com.darekbx.water.repository.local.StatusRepository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class CommonModule {

    private fun provideStatusRepository(statusDao: StatusDao): StatusRepository {
        return StatusRepository(statusDao)
    }

    private fun provideLevelRepository(levelDao: LevelDao): LevelRepository {
        return LevelRepository(levelDao)
    }
}
