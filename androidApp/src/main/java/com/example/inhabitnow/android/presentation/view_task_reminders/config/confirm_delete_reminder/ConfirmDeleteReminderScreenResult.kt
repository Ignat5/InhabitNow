package com.example.inhabitnow.android.presentation.view_task_reminders.config.confirm_delete_reminder

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult

sealed interface ConfirmDeleteReminderScreenResult : ScreenResult {
    data class Confirm(val reminderId: String) : ConfirmDeleteReminderScreenResult
    data object Dismiss : ConfirmDeleteReminderScreenResult
}