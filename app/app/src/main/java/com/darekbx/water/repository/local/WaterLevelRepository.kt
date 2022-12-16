package com.darekbx.water.repository.local

import android.util.Log
import com.darekbx.data.dao.WaterLevelDao
import com.darekbx.data.dto.WaterLevelDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class WaterLevelRepository @Inject constructor(
    private val waterLevelDao: WaterLevelDao
) {

    /**
     * @param date Date in YYYYMMdd format
     */
    fun waterLevels(date: String) = waterLevelDao.waterLevels()

    fun addWaterLevel(value: Float) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.v("-----------", "data: $value")
            waterLevelDao.addWaterLevel(WaterLevelDto(null, value, System.currentTimeMillis()))
        }
    }
}