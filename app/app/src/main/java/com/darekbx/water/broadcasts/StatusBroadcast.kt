package com.darekbx.water.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.darekbx.communication.BleClientManager
import com.darekbx.water.repository.local.StatusRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StatusBroadcast: BroadcastReceiver() {

    companion object {
        private const val CONNECTION_ACTION_NAME = "ble_connection_action"
        private const val CONNECTION_VALUE_KEY = "ble_connection_value"
    }

    @Inject
    lateinit var statusRepository: StatusRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == CONNECTION_ACTION_NAME) {
            val statusInt = intent.getIntExtra(
                CONNECTION_VALUE_KEY,
                BleClientManager.DeviceStatus.CONNECTING.value
            )
            val status = BleClientManager.DeviceStatus.values()
                .firstOrNull { it.value == statusInt }
                ?: throw IllegalStateException("Invalid ble device status!")
            statusRepository.updateStatus(status)
        }
    }
}