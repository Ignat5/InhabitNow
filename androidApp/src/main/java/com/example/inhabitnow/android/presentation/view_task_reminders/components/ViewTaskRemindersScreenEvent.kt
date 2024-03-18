package com.example.inhabitnow.android.presentation.view_task_reminders.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder.components.CreateReminderScreenResult

sealed interface ViewTaskRemindersScreenEvent : ScreenEvent {
    data class OnReminderClick(val reminderId: String) : ViewTaskRemindersScreenEvent
    data class OnDeleteReminderClick(val reminderId: String) : ViewTaskRemindersScreenEvent
    data object OnCreateReminderClick : ViewTaskRemindersScreenEvent
    data object OnBackClick : ViewTaskRemindersScreenEvent

    sealed interface ResultEvent : ViewTaskRemindersScreenEvent {
        val result: ScreenResult

        data class CreateReminder(
            override val result: CreateReminderScreenResult
        ) : ResultEvent
    }
}