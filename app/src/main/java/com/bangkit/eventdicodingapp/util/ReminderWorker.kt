package com.bangkit.eventdicodingapp.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bangkit.eventdicodingapp.R
import com.bangkit.eventdicodingapp.data.remote.retrofit.ApiConfig

class ReminderWorker(private val context: Context, workerParams: WorkerParameters): CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val sharedPref = context.getSharedPreferences("reminder_prefs", Context.MODE_PRIVATE)
        val isReminderActive = sharedPref.getBoolean("daily_reminder", false)

        if (isReminderActive) {
            try {
                val apiService = ApiConfig.getApiService()
                val response = apiService.getEventReminder()

                if (!response.error && response.listEvents.isNotEmpty()) {
                    val event = response.listEvents.first()
                    showNotification(event.name, event.beginTime, event.link)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                return Result.retry()
            }
        }

        return Result.success()
    }

    private fun showNotification(eventName: String, eventTime: String, linkEvent: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationIntent = Intent(Intent.ACTION_VIEW, Uri.parse(linkEvent))
        val pendingIntent = PendingIntent.getActivity(
            context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, "reminder_channel")
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle("Event Terdekat")
            .setContentText("Event: $eventName pada $eventTime")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(1001, builder.build())
    }

}