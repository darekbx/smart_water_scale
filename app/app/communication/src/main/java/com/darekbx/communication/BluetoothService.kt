package com.darekbx.communication

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.IBinder
import com.darekbx.communication.utils.NotificationUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BluetoothService : Service() {

    companion object {
        private var IS_SERVICE_ACTIVE = false

        private const val CONNECTION_ACTION_NAME = "ble_connection_action"
        private const val CONNECTION_VALUE_KEY = "ble_connection_value"

        private const val DATA_ACTION_NAME = "ble_data_action"
        private const val DATA_VALUE_KEY = "ble_data_value"
    }

    @Inject
    lateinit var notificationUtil: NotificationUtil

    @Inject
    lateinit var clientManager: BleClientManager

    @Inject
    lateinit var bleScanner: BleScanner

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        IS_SERVICE_ACTIVE = true

        startForeground(
            NotificationUtil.NOTIFICATION_ID,
            notificationUtil.createNotification(null),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE
        )

        notifyStatus(BleClientManager.DeviceStatus.CONNECTING)

        clientManager.notifyStatus = ::notifyStatus
        clientManager.notifyData = ::notifyData

        bleScanner.scanForDevices()
        bleScanner.deviceCallback = { device ->
            clientManager
                .connect(device)
                .useAutoConnect(true)
                .enqueue()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        clientManager.close()
        bleScanner.stopScanner()

        IS_SERVICE_ACTIVE = false
    }

    private fun notifyStatus(deviceStatus: BleClientManager.DeviceStatus) {
        notificationUtil.updateNotification(0.0F)
        sendBroadcast(Intent(CONNECTION_ACTION_NAME).apply {
            putExtra(CONNECTION_VALUE_KEY, deviceStatus.value)
            component =
                ComponentName(
                    "com.darekbx.water",
                    "com.darekbx.water.broadcasts.StatusBroadcast"
                )
        })
    }

    private fun notifyData(data: Float) {
        notificationUtil.updateNotification(data)
        sendBroadcast(Intent(DATA_ACTION_NAME).apply {
            putExtra(DATA_VALUE_KEY, data)
            component =
                ComponentName(
                    "com.darekbx.water",
                    "com.darekbx.water.broadcasts.DataBroadcast"
                )
        })
    }
}
