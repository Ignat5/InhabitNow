package com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.model.UIReminderContent
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.components.BaseCreateEditReminderScreenEvent
import com.example.inhabitnow.core.type.ReminderType
import kotlinx.datetime.DayOfWeek

sealed interface CreateReminderScreenEvent : ScreenEvent {
    data class BaseEvent(
        val baseEvent: BaseCreateEditReminderScreenEvent
    ) : CreateReminderScreenEvent
}