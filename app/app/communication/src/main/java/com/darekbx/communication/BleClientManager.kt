package com.darekbx.communication

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.util.Log
import com.darekbx.communication.model.DeviceInfo
import no.nordicsemi.android.ble.BleManager

class BleClientManager(
    context: Context,
    private val deviceInfo: DeviceInfo
): BleManager(context) {

    enum class DeviceStatus(val value: Int) {
        CONNECTING(0),
        CONNECTED(1),
        FAILED(2)
    }

    var notifyStatus: (DeviceStatus) -> Unit = { }
    var notifyData: (Float) -> Unit = { }

    override fun getGattCallback(): BleManagerGattCallback = GattCallback()

    private inner class GattCallback : BleManagerGattCallback() {

        private var characteristic: BluetoothGattCharacteristic? = null

        override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
            val service = gatt.getService(deviceInfo.serviceUUID)
            characteristic = service?.getCharacteristic(deviceInfo.notificationCharacteristicUUID)
            val myCharacteristicProperties = characteristic?.properties ?: 0
            return myCharacteristicProperties and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0
        }

        override fun initialize() {
            Log.v(TAG, "Initialized ble client")
            setNotificationCallback(characteristic).with { _, data ->
                data.value?.let { buffer ->
                    val valueString = String(buffer, Charsets.UTF_8)
                    valueString.toFloatOrNull()?.let {
                        notifyData(it)
                    } ?: Log.w(TAG, "Unable to parse data to float! Data: \"$valueString\"")
                } ?: Log.w(TAG, "Bluetooth notification data is null!")
            }

            notifyStatus(DeviceStatus.CONNECTING)
            beginAtomicRequestQueue()
                .add(enableNotifications(characteristic)
                    .fail { _: BluetoothDevice?, _: Int ->
                        notifyStatus(DeviceStatus.FAILED)
                        disconnect().enqueue()
                    }
                )
                .done { notifyStatus(DeviceStatus.CONNECTED) }
                .fail { _, code ->
                    Log.v(TAG, "Initialization failed: $code")
                    notifyStatus(DeviceStatus.FAILED)
                }
                .enqueue()
        }

        override fun onServicesInvalidated() {
            characteristic = null
        }
    }

    companion object {
        private const val TAG = "BleClientManager"
    }
}