package com.syncup.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.syncup.MainActivity
import com.syncup.R

class SyncUpMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Check if message contains data payload
        if (remoteMessage.data.isNotEmpty()) {
            val title = remoteMessage.data["title"] ?: "SyncUp"
            val body = remoteMessage.data["body"] ?: ""
            val type = remoteMessage.data["type"] ?: "default"

            sendNotification(title, body, type)
        }

        // Check if message contains notification payload
        remoteMessage.notification?.let {
            sendNotification(it.title ?: "SyncUp", it.body ?: "", "notification")
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Save token to backend or SharedPreferences for later use
        saveTokenToPreferences(token)
    }

    private fun sendNotification(title: String, messageBody: String, type: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("notification_type", type)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationId = System.currentTimeMillis().toInt()
        val channelId = "syncup_notifications"

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        when (type) {
            "critical_task" -> {
                notificationBuilder.setColor(getColor(android.R.color.holo_red_light))
                notificationBuilder.setVibrate(longArrayOf(0, 500, 250, 500))
            }
            "friendly_nudge" -> {
                notificationBuilder.setColor(getColor(android.R.color.holo_blue_light))
            }
            "blocker_flagged" -> {
                notificationBuilder.setColor(getColor(android.R.color.holo_orange_light))
                notificationBuilder.setVibrate(longArrayOf(0, 250, 250, 250))
            }
            "deadline_warning" -> {
                notificationBuilder.setColor(getColor(android.R.color.holo_orange_light))
            }
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "SyncUp Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Notifications for tasks, blockers, and deadlines"
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun saveTokenToPreferences(token: String) {
        val sharedPref = getSharedPreferences("syncup_prefs", Context.MODE_PRIVATE)
        sharedPref.edit().putString("fcm_token", token).apply()
    }
}
