package com.example.inhabitnow.data.model.reminder

import com.example.inhabitnow.data.model.reminder.content.ReminderContentEntity

data class ReminderWithContentEntity(
    val reminder: ReminderEntity,
    val timeContent: ReminderContentEntity.TimeContent,
    val scheduleContent: ReminderContentEntity.ScheduleContent
)
