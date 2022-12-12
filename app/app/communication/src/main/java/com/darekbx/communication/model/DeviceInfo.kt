package com.darekbx.communication.model

import java.util.UUID

class DeviceInfo(
    val deviceName: String,
    val serviceUUID: UUID,
    val notificationCharacteristicUUID: UUID
)