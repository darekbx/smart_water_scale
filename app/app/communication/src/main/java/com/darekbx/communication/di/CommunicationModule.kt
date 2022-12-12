package com.darekbx.communication.di

import android.app.NotificationManager
import android.content.Context
import com.darekbx.communication.BleClientManager
import com.darekbx.communication.BleScanner
import com.darekbx.communication.model.DeviceInfo
import com.darekbx.communication.utils.NotificationUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.*

@Module
@InstallIn(SingletonComponent::class)
class CommunicationModule {

    @Provides
    fun provideNotificationUtil(
        @ApplicationContext context: Context,
        notificationManager: NotificationManager
    ): NotificationUtil {
        return NotificationUtil(context, notificationManager)
    }

    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @Provides
    fun provideBleClientManager(
        @ApplicationContext context: Context,
        deviceInfo: DeviceInfo
    ): BleClientManager {
        return BleClientManager(context, deviceInfo)
    }

    @Provides
    fun provideBleScanner(deviceInfo: DeviceInfo): BleScanner {
        return BleScanner(deviceInfo)
    }

    @Provides
    fun provideDeviceInfo(): DeviceInfo {
        return DeviceInfo(
            "Smart Scale",
            UUID.fromString("89409171-FE10-40B7-80A3-398A8C219855"),
            UUID.fromString("89409171-FE10-40AA-80A3-398A8C219855")
        )
    }
}