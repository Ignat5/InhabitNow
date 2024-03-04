package com.example.inhabitnow.data.model.reminder.content

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("ReminderContent")
@Serializable
sealed interface ReminderContentEntity {
    @SerialName("ReminderContent.ScheduleContent")
    @Serializable
    sealed interface ScheduleContent : ReminderContentEntity {
        @SerialName("ReminderContent.ScheduleContent.EveryDay")
        @Serializable
        data object EveryDay : ScheduleContent
        @SerialName("ReminderContent.ScheduleContent.DaysOfWeek")
        @Serializable
        data class DaysOfWeek(val daysOfWeek: Set<DayOfWeek>) : ScheduleContent
    }
    @SerialName("ReminderContent.TimeContent")
    @Serializable
    data class TimeContent(val time: LocalTime) : ReminderContentEntity
}