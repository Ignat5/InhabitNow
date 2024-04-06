package com.example.inhabitnow.android.presentation.view_task_reminders.components

import com.example.inhabitnow.android.presentation.base.components.config.ScreenConfig
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.create.CreateReminderStateHolder
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.edit.EditReminderStateHolder

sealed interface ViewTaskRemindersScreenConfig : ScreenConfig {
    data class CreateReminder(
        val stateHolder: CreateReminderStateHolder
    ) : ViewTaskRemindersScreenConfig

    data class EditReminder(
        val stateHolder: EditReminderStateHolder
    ) : ViewTaskRemindersScreenConfig

    data class ConfirmDeleteReminder(
        val reminderId: String
    ) : ViewTaskRemindersScreenConfig

    data class CheckNotificationPermission(val shouldSkipRationale: Boolean) :
        ViewTaskRemindersScreenConfig

    data object NotificationPermissionRationale : ViewTaskRemindersScreenConfig
}