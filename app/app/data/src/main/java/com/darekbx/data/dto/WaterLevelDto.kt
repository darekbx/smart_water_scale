package com.darekbx.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "level")
class WaterLevelDto(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "value") val value: Float,
    @ColumnInfo(name = "timestamp") val timestamp: Long
)