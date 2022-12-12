package com.darekbx.communication

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.util.Log
import com.darekbx.communication.model.DeviceInfo
import no.nordicsemi.android.support.v18.scanner.*

class BleScanner(
    private val deviceInfo: DeviceInfo
) {

    var deviceCallback: (BluetoothDevice) -> Unit = { }

    fun scanForDevices() {
        Log.v(TAG, "Start scanning...")
        val scanner = BluetoothLeScannerCompat.getScanner()
        val settings: ScanSettings = ScanSettings.Builder()
            .setLegacy(false)
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setReportDelay(5000)
            .setUseHardwareBatchingIfSupported(true)
            .build()
        val filters = listOf(ScanFilter.Builder().setDeviceName(deviceInfo.deviceName).build())
        scanner.startScan(filters, settings, scanCallback)
    }

    fun stopScanner() {
        Log.v(TAG, "Scanner stopped")
        val scanner = BluetoothLeScannerCompat.getScanner()
        scanner.stopScan(scanCallback)
    }

    private val scanCallback = object: ScanCallback() {

        @SuppressLint("MissingPermission")
        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            super.onBatchScanResults(results)
            results
                .firstOrNull { it.device.name == deviceInfo.deviceName }
                ?.let {
                    Log.v(TAG, "Found device: ${it.device.name}")
                    deviceCallback(it.device)
                    stopScanner()
                }
        }

        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Log.v(TAG, "Found device: ${result.device.name}")
            deviceCallback(result.device)
            stopScanner()
        }
    }

    companion object {
        private const val TAG = "BleScanner"
    }
}