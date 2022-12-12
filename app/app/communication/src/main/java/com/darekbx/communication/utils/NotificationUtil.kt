package com.darekbx.communication.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.darekbx.communication.R

class NotificationUtil(
    private val context: Context,
    private val notificationManager: NotificationManager
) {

    companion object {
        const val NOTIFICATION_ID = 341
        private const val NOTIFICATION_CHANNEL_ID = "sensor_channel_id"
    }

    fun updateNotification(waterLevel: Float) {
        val notification = createNotification(waterLevel)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    fun createNotification(waterLevel: Float?): Notification {
        val title = context.getString(R.string.notifiction_title)
        val text = waterLevel
            ?.let { context.getString(R.string.notifiction_text, it) }
            ?: context.getString(R.string.notifiction_na)
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_water)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        var channel = notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID)
        if (channel == null) {
            channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, title,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }

        return builder.build()
    }
}
