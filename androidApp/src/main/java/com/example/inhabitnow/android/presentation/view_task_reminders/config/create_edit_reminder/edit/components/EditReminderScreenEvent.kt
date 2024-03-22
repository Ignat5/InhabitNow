package com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.edit.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.base.components.BaseCreateEditReminderScreenEvent

sealed interface EditReminderScreenEvent : ScreenEvent {
    data class BaseEvent(
        val baseEvent: BaseCreateEditReminderScreenEvent
    ) : EditReminderScreenEvent
}