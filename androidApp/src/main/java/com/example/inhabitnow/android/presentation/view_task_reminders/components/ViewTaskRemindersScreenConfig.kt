package com.example.inhabitnow.android.presentation.view_task_reminders.components

import com.example.inhabitnow.android.presentation.base.components.config.ScreenConfig
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder.CreateReminderStateHolder

sealed interface ViewTaskRemindersScreenConfig : ScreenConfig {
    data class CreateReminder(
        val stateHolder: CreateReminderStateHolder
    ) : ViewTaskRemindersScreenConfig

    data class ConfirmDeleteReminder(
        val reminderId: String
    ) : ViewTaskRemindersScreenConfig
}