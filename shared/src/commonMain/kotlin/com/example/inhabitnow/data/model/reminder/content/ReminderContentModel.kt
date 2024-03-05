package com.example.inhabitnow.data.model.reminder.content

import kotlinx.datetime.DayOfWeek
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("ReminderContent")
@Serializable
sealed interface ReminderContentModel {
    @SerialName("ReminderContent.ScheduleContent")
    @Serializable
    sealed interface ScheduleContent : ReminderContentModel {
        @SerialName("ReminderContent.ScheduleContent.EveryDay")
        @Serializable
        data object EveryDay : ScheduleContent

        @SerialName("ReminderContent.ScheduleContent.DaysOfWeek")
        @Serializable
        data class DaysOfWeek(val daysOfWeek: Set<DayOfWeek>) : ScheduleContent
    }
}