package com.darekbx.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.darekbx.data.dto.LevelDto
import kotlinx.coroutines.flow.Flow

@Dao
interface LevelDao {

    @Query("SELECT * FROM level")
    fun levels(): Flow<List<LevelDto>>

    @Insert
    fun addLevel(levelDto: LevelDto)
}