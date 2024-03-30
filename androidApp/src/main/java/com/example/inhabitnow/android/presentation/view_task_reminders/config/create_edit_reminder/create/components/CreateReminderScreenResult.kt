package com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.create.components

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.core.type.ReminderType
import com.example.inhabitnow.domain.model.reminder.content.ReminderContentModel
import kotlinx.datetime.LocalTime

sealed interface CreateReminderScreenResult : ScreenResult {
    data class Confirm(
        val time: LocalTime,
        val type: ReminderType,
        val schedule: ReminderContentModel.ScheduleContent
    ) : CreateReminderScreenResult

    data object Dismiss : CreateReminderScreenResult
}