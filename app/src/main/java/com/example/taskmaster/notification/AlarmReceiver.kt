package com.example.taskmaster.notification

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.taskmaster.R

class AlarmReceiver : BroadcastReceiver() {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {

        val title = intent.getStringExtra("TASK_TITLE") ?: "Task Reminder"
        val desc = intent.getStringExtra("TASK_DESC") ?: ""

        val builder = NotificationCompat.Builder(context, "task_channel")
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle(title)
            .setContentText(desc)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        NotificationManagerCompat.from(context)
            .notify(System.currentTimeMillis().toInt(), builder.build())
    }
}
