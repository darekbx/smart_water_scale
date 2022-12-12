package com.darekbx.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.darekbx.data.dao.LevelDao
import com.darekbx.data.dao.StatusDao
import com.darekbx.data.dto.LevelDto
import com.darekbx.data.dto.StatusDto

@Database(
    entities = [
        LevelDto::class,
        StatusDto::class
    ],
    exportSchema = true,
    version = 1
)
abstract class WaterDatabase : RoomDatabase() {

    abstract fun statusDao(): StatusDao

    abstract fun levelDao(): LevelDao

    companion object {
        val DB_NAME = "water_db"
    }
}