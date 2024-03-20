package com.example.inhabitnow.domain.util

import com.example.inhabitnow.data.model.reminder.content.ReminderContentEntity

fun ReminderContentEntity.ScheduleContent.checkIfOverlaps(targetSchedule: ReminderContentEntity.ScheduleContent): Boolean {
    return this.let { sourceSchedule ->
        when (sourceSchedule) {
            is ReminderContentEntity.ScheduleContent.EveryDay -> true
            is ReminderContentEntity.ScheduleContent.DaysOfWeek -> {
                when (targetSchedule) {
                    is ReminderContentEntity.ScheduleContent.EveryDay -> true
                    is ReminderContentEntity.ScheduleContent.DaysOfWeek -> {
                        sourceSchedule.daysOfWeek.any { it in targetSchedule.daysOfWeek }
                    }
                }
            }
        }
    }
}