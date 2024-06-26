package com.example.inhabitnow.domain.model.reminder

import com.example.inhabitnow.core.type.ReminderType
import com.example.inhabitnow.domain.model.reminder.content.ReminderContentModel
import kotlinx.datetime.LocalTime

data class ReminderModel(
    val id: String,
    val taskId: String,
    val type: ReminderType,
    val time: LocalTime,
    val schedule: ReminderContentModel.ScheduleContent,
    val createdAt: Long
)
