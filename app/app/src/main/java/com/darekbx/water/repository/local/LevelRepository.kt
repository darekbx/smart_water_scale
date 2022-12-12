package com.darekbx.water.repository.local

import com.darekbx.data.dao.LevelDao
import com.darekbx.data.dto.LevelDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LevelRepository @Inject constructor(
    private val levelDao: LevelDao
) {

    fun levelFlow() = levelDao.levels()

    fun addLevel(value: Float) {
        CoroutineScope(Dispatchers.IO).launch {
            levelDao.addLevel(LevelDto(null, value, System.currentTimeMillis()))
        }
    }
}