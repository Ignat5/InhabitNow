package com.example.inhabitnow.android.platform.manager

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.inhabitnow.android.platform.broadcast.ReminderBroadcastReceiver
import com.example.inhabitnow.android.platform.util.ReminderUtil
import com.example.inhabitnow.core.platform.ReminderManager

class DefaultReminderManager(private val context: Context) : ReminderManager {

    override fun setReminder(reminderId: String, epochMillis: Long) {
        setUpAlarm(reminderId, epochMillis)
    }

    override fun resetReminderById(reminderId: String) {
        cancelAlarm(reminderId)
    }

    private fun setUpAlarm(reminderId: String, epochMillis: Long) {
        runCatching {
            val areNotificationsEnabled =
                (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).areNotificationsEnabled()
            if (areNotificationsEnabled) {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val pendingIntent = buildPendingIntent(reminderId)
                val canScheduleExactAlarms = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    alarmManager.canScheduleExactAlarms()
                } else true
                if (canScheduleExactAlarms) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        epochMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        epochMillis,
                        pendingIntent
                    )
                }
            }
        }.onFailure {
            it.printStackTrace()
        }
    }

    private fun cancelAlarm(reminderId: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = buildPendingIntent(reminderId)
        alarmManager.cancel(pendingIntent)
    }

    private fun buildPendingIntent(reminderId: String): PendingIntent {
        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            putExtra(ReminderUtil.REMINDER_ID_KEY, reminderId)
        }
        val requestCode = ReminderUtil.stringToInteger(reminderId)
        return PendingIntent.getBroadcast(
            context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE
        )
    }
}