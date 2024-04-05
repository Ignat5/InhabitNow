package com.example.inhabitnow.android.platform.broadcast

import android.Manifest
import android.app.Notification
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.PendingIntentCompat
import com.example.inhabitnow.android.MainActivity
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.core.di.module.presentation.PresentationComponent
import com.example.inhabitnow.android.platform.util.ReminderUtil
import com.example.inhabitnow.android.ui.toHourMinute
import com.example.inhabitnow.core.type.ReminderType
import com.example.inhabitnow.domain.model.reminder.ReminderModel
import com.example.inhabitnow.domain.model.task.TaskModel
import com.example.inhabitnow.domain.use_case.read_task_with_content_by_id.ReadTaskWithContentByIdUseCase
import com.example.inhabitnow.domain.use_case.reminder.check_reminder_scheduled.CheckReminderScheduledUseCase
import com.example.inhabitnow.domain.use_case.reminder.read_reminder_by_id.ReadReminderByIdUseCase
import com.example.inhabitnow.domain.use_case.reminder.set_up_next_reminder.SetUpNextReminderUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ReminderBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("myTag", "ReminderBroadcastReceiver: onReceive...")
        if (context == null || intent == null) return
        intent.getStringExtra(ReminderUtil.REMINDER_ID_KEY)?.let { reminderId ->
            val pendingResult = goAsync()
            PresentationComponent.externalScope.launch {
                coroutineScope {
                    launch {
                        getReminderWithTaskById(reminderId)?.let { reminderWithTaskModel ->
                            showReminder(reminderWithTaskModel, context)
                        }
                    }
                    launch {
                        PresentationComponent.setUpNextReminderUseCase(reminderId)
                    }
                }
                pendingResult.finish()
            }
        }
    }

    private fun showReminder(
        reminderWithTask: ReminderWithTaskModel,
        context: Context
    ) {
        Log.d("myTag", "showReminderNotification: reminderWithTask: $reminderWithTask")
        NotificationManagerCompat.from(context).let { notificationManager ->
            if (notificationManager.areNotificationsEnabled()) {
                when (reminderWithTask.reminderModel.type) {
                    ReminderType.Notification -> showNotificationReminder(reminderWithTask, context)
                    else -> return
                }
            }
        }
    }

    private fun showNotificationReminder(
        reminderWithTask: ReminderWithTaskModel,
        context: Context
    ) {
        val channel = createNotificationReminderChannel(context)
        val pendingContentIntent = run {
            val contentIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val requestCode = ReminderUtil.stringToInteger(reminderWithTask.reminderModel.taskId)
            PendingIntent.getActivity(
                context,
                requestCode,
                contentIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }
        val notification = NotificationCompat.Builder(context, channel.id)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(reminderWithTask.taskModel.title)
            .setContentText(reminderWithTask.reminderModel.time.toHourMinute())
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setCategory(Notification.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setContentIntent(pendingContentIntent)
            .build()

        val notificationId = ReminderUtil.stringToInteger(reminderWithTask.reminderModel.taskId)
        showNotification(context, notificationId, notification)
    }

    private fun showNotification(
        context: Context,
        notificationId: Int,
        notification: Notification
    ) {
        NotificationManagerCompat.from(context).let { notificationManager ->
            val isPermissionGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            } else true
            if (isPermissionGranted) {
                notificationManager.notify(notificationId, notification)
            }
        }
    }

    private fun createNotificationReminderChannel(context: Context): NotificationChannelCompat {
        val notificationManager = NotificationManagerCompat.from(context)
        val channelName = "Notification reminders"
        val channel = NotificationChannelCompat.Builder(
            NOTIFICATION_REMINDER_CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_HIGH
        )
            .setName(channelName)
            .setImportance(NotificationManagerCompat.IMPORTANCE_HIGH)
            .setShowBadge(true)
            .build()

        notificationManager.createNotificationChannel(channel)
        return channel
    }

    private suspend fun getReminderWithTaskById(reminderId: String): ReminderWithTaskModel? =
        coroutineScope {
            val isReminderScheduledDef = async {
                PresentationComponent.checkReminderScheduledUseCase(
                    reminderId = reminderId,
                    targetDate = todayDate
                )
            }
            val reminderWithTaskDef = async {
                PresentationComponent.readReminderByIdUseCase(reminderId).firstOrNull()
                    ?.let { reminderModel ->
                        PresentationComponent.readTaskWithContentByIdUseCase(reminderModel.taskId)
                            .firstOrNull()
                            ?.let { taskModel ->
                                ReminderWithTaskModel(
                                    reminderModel = reminderModel,
                                    taskModel = taskModel
                                )
                            }
                    }
            }
            isReminderScheduledDef.await().let { isReminderScheduled ->
                if (isReminderScheduled) {
                    reminderWithTaskDef.await()
                } else null
            }
        }

    private val todayDate: LocalDate
        get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    private data class ReminderWithTaskModel(
        val reminderModel: ReminderModel,
        val taskModel: TaskModel
    )

    companion object {
        private const val NOTIFICATION_REMINDER_CHANNEL_ID = "NOTIFICATION_REMINDER_CHANNEL_ID"
    }

}