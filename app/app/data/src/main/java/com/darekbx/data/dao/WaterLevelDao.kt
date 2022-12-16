package com.darekbx.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.darekbx.data.dto.WaterLevelDto
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterLevelDao {

    @Query("SELECT * FROM level")
    fun waterLevels(): Flow<List<WaterLevelDto>>

    @Insert
    fun addWaterLevel(waterLevelDto: WaterLevelDto)
}