package com.darekbx.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.darekbx.data.dto.StatusDto
import kotlinx.coroutines.flow.Flow

@Dao
interface StatusDao {

    @Query("SELECT * FROM status LIMIT 1")
    fun status(): Flow<StatusDto>

    @Query("UPDATE status SET value = :value")
    suspend fun update(value: Int)
}