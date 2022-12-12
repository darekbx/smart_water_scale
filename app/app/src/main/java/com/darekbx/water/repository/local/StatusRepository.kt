package com.darekbx.water.repository.local

import com.darekbx.communication.BleClientManager
import com.darekbx.data.dao.StatusDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class StatusRepository @Inject constructor(
    private val statusDao: StatusDao
) {

    fun statusFlow() = statusDao.status()

    fun updateStatus(deviceStatus: BleClientManager.DeviceStatus) {
        CoroutineScope(Dispatchers.IO).launch {
            statusDao.update(deviceStatus.value)
        }
    }
}