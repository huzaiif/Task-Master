package com.example.taskmaster.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationHelper {

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "task_channel",
                "Task Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Notifications for scheduled tasks"

            val manager = context.getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }
}
