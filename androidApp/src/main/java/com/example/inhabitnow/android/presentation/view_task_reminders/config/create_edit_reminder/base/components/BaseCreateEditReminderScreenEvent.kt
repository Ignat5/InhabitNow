package com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.base.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.core.type.ReminderType
import com.example.inhabitnow.domain.model.reminder.content.ReminderContentModel
import kotlinx.datetime.DayOfWeek

sealed interface BaseCreateEditReminderScreenEvent : ScreenEvent {
    data class OnHoursValueUpdate(val hours: Int) : BaseCreateEditReminderScreenEvent
    data class OnMinutesValueUpdate(val minutes: Int) : BaseCreateEditReminderScreenEvent
    data class OnReminderTypeClick(val type: ReminderType) : BaseCreateEditReminderScreenEvent
    data class OnReminderScheduleTypeClick(
        val type: ReminderContentModel.ScheduleContent.Type
    ) : BaseCreateEditReminderScreenEvent

    data class OnDayOfWeekClick(val dayOfWeek: DayOfWeek) : BaseCreateEditReminderScreenEvent

    data object OnConfirmClick : BaseCreateEditReminderScreenEvent
    data object OnDismissRequest : BaseCreateEditReminderScreenEvent
}