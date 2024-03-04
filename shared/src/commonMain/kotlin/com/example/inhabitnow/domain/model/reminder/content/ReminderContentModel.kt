package com.example.inhabitnow.domain.model.reminder.content

import kotlinx.datetime.DayOfWeek

sealed interface ReminderContentModel {
    sealed class ScheduleContent(val type: ScheduleType) : ReminderContentModel {
        enum class ScheduleType { EveryDay, DaysOfWeek }

        data object EveryDay : ScheduleContent(ScheduleType.EveryDay)
        data class DaysOfWeek(val daysOfWeek: Set<DayOfWeek>) :
            ScheduleContent(ScheduleType.DaysOfWeek)
    }
}