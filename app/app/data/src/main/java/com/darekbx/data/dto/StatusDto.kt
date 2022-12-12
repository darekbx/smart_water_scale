package com.darekbx.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "status")
class StatusDto(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "value") val value: Int
)