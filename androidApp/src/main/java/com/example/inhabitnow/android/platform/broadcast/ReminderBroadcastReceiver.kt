package com.example.inhabitnow.android.platform.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.inhabitnow.android.core.di.module.presentation.PresentationComponent
import com.example.inhabitnow.android.platform.util.ReminderUtil
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
                            showReminderNotification(reminderWithTaskModel)
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

    private fun showReminderNotification(reminderWithTask: ReminderWithTaskModel) {
        Log.d("myTag", "showReminderNotification: reminderWithTask: $reminderWithTask")
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

}