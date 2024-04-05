package com.example.inhabitnow.android.presentation.view_task_reminders.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.android.presentation.view_task_reminders.config.confirm_delete_reminder.ConfirmDeleteReminderScreenResult
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.create.components.CreateReminderScreenResult
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.edit.components.EditReminderScreenResult
import com.example.inhabitnow.android.presentation.view_task_reminders.config.permission.CheckNotificationPermissionScreenResult

sealed interface ViewTaskRemindersScreenEvent : ScreenEvent {
    data class OnReminderClick(val reminderId: String) : ViewTaskRemindersScreenEvent
    data class OnDeleteReminderClick(val reminderId: String) : ViewTaskRemindersScreenEvent
    data object OnCreateReminderClick : ViewTaskRemindersScreenEvent
    data object OnNotificationPermissionRationalDismissRequest : ViewTaskRemindersScreenEvent
    data object OnBackClick : ViewTaskRemindersScreenEvent

    sealed interface ResultEvent : ViewTaskRemindersScreenEvent {
        val result: ScreenResult

        data class CreateReminder(
            override val result: CreateReminderScreenResult
        ) : ResultEvent

        data class EditReminder(
            override val result: EditReminderScreenResult
        ) : ResultEvent

        data class ConfirmDeleteReminder(
            override val result: ConfirmDeleteReminderScreenResult
        ) : ResultEvent

        data class CheckNotificationPermission(
            override val result: CheckNotificationPermissionScreenResult
        ) : ResultEvent
    }
}