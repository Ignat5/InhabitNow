package com.example.inhabitnow.domain.model.reminder.content

import kotlinx.datetime.DayOfWeek

sealed interface ReminderContentModel {
    sealed class ScheduleContent(val type: Type) : ReminderContentModel {
        enum class Type { EveryDay, DaysOfWeek }

        data object EveryDay : ScheduleContent(Type.EveryDay)
        data class DaysOfWeek(val daysOfWeek: Set<DayOfWeek>) : ScheduleContent(Type.DaysOfWeek)
    }
}