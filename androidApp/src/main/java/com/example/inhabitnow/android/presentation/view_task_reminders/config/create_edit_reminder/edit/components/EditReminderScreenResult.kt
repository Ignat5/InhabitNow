package com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.edit.components

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.core.type.ReminderType
import com.example.inhabitnow.domain.model.reminder.content.ReminderContentModel
import kotlinx.datetime.LocalTime

sealed interface EditReminderScreenResult : ScreenResult {
    data class Confirm(
        val reminderId: String,
        val reminderTime: LocalTime,
        val reminderType: ReminderType,
        val reminderSchedule: ReminderContentModel.ScheduleContent
    ) : EditReminderScreenResult

    data object Dismiss : EditReminderScreenResult
}