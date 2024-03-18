package com.example.inhabitnow.android.presentation.view_task_reminders.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent

sealed interface ViewTaskRemindersScreenEvent : ScreenEvent {
    data class OnReminderClick(val reminderId: String) : ViewTaskRemindersScreenEvent
    data class OnDeleteReminderClick(val reminderId: String) : ViewTaskRemindersScreenEvent
    data object OnCreateReminderClick : ViewTaskRemindersScreenEvent
    data object OnBackClick : ViewTaskRemindersScreenEvent
}