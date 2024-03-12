package com.example.inhabitnow.data.model.reminder

import com.example.inhabitnow.core.type.ReminderType
import com.example.inhabitnow.data.model.reminder.content.ReminderContentEntity
import kotlinx.datetime.LocalTime

data class ReminderEntity(
    val id: String,
    val taskId: String,
    val type: ReminderType,
    val time: LocalTime,
    val schedule: ReminderContentEntity.ScheduleContent,
    val createdAt: Long
)
