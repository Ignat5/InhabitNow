package com.example.inhabitnow.data.model.reminder

import com.example.inhabitnow.core.type.ReminderType
import com.example.inhabitnow.data.model.reminder.content.ReminderContentModel
import kotlinx.datetime.LocalTime

data class ReminderModel(
    val id: String,
    val taskId: String,
    val type: ReminderType,
    val time: LocalTime,
    val schedule: ReminderContentModel.ScheduleContent,
    val createdAt: Long
)
