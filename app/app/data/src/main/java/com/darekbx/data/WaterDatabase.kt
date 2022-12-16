package com.darekbx.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.darekbx.data.dao.WaterLevelDao
import com.darekbx.data.dao.StatusDao
import com.darekbx.data.dto.WaterLevelDto
import com.darekbx.data.dto.StatusDto

@Database(
    entities = [
        WaterLevelDto::class,
        StatusDto::class
    ],
    exportSchema = true,
    version = 1
)
abstract class WaterDatabase : RoomDatabase() {

    abstract fun statusDao(): StatusDao

    abstract fun levelDao(): WaterLevelDao

    companion object {
        val DB_NAME = "water_db"
    }
}