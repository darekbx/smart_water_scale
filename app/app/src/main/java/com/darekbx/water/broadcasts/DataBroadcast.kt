package com.darekbx.water.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.darekbx.water.repository.local.WaterLevelRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DataBroadcast: BroadcastReceiver() {

    companion object {
        private const val DATA_ACTION_NAME = "ble_data_action"
        private const val DATA_VALUE_KEY = "ble_data_value"
    }

    @Inject
    lateinit var levelRepository: WaterLevelRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == DATA_ACTION_NAME) {
            val data = intent.getFloatExtra(DATA_VALUE_KEY, 0.0F)
            if (data > 0.0F) {
                levelRepository.addWaterLevel(data)
            }
        }
    }
}