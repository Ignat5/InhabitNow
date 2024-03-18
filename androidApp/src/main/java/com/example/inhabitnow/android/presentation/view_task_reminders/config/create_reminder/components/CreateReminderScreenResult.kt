package com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder.components

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.android.presentation.model.UIReminderContent
import com.example.inhabitnow.core.type.ReminderType
import kotlinx.datetime.LocalTime

sealed interface CreateReminderScreenResult : ScreenResult {
    data class Confirm(
        val time: LocalTime,
        val type: ReminderType,
        val uiScheduleContent: UIReminderContent.Schedule
    ) : CreateReminderScreenResult

    data object Dismiss : CreateReminderScreenResult
}