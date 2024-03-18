package com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.model.UIReminderContent
import com.example.inhabitnow.core.type.ReminderType
import kotlinx.datetime.DayOfWeek

sealed interface CreateReminderScreenEvent : ScreenEvent {
    data object OnPickReminderTimeClick : CreateReminderScreenEvent
    data class OnPickReminderType(val type: ReminderType) : CreateReminderScreenEvent
    data class OnReminderScheduleTypeClick(
        val type: UIReminderContent.Schedule.Type
    ) : CreateReminderScreenEvent

    data class OnDayOfWeekClick(val dayOfWeek: DayOfWeek) : CreateReminderScreenEvent

    data object OnConfirmClick : CreateReminderScreenEvent
    data object OnDismissRequest : CreateReminderScreenEvent
}