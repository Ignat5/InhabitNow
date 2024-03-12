package com.example.inhabitnow.domain.model.reminder.content

import kotlinx.datetime.DayOfWeek

sealed interface ReminderContentModel {
    sealed interface ScheduleContent : ReminderContentModel {
        data object EveryDay : ScheduleContent
        data class DaysOfWeek(val daysOfWeek: Set<DayOfWeek>) : ScheduleContent
    }
}