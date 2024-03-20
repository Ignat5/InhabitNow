package com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.edit.components

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.android.presentation.model.UIReminderContent
import com.example.inhabitnow.core.type.ReminderType
import kotlinx.datetime.LocalTime

sealed interface EditReminderScreenResult : ScreenResult {
    data class Confirm(
        val reminderId: String,
        val reminderTime: LocalTime,
        val reminderType: ReminderType,
        val reminderSchedule: UIReminderContent.Schedule
    ) : EditReminderScreenResult

    data object Dismiss : EditReminderScreenResult
}