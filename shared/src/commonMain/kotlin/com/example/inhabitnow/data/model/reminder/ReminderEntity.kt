package com.example.inhabitnow.data.model.reminder

import com.example.inhabitnow.core.type.ReminderType
import kotlinx.datetime.LocalTime

data class ReminderEntity(
    val id: String,
    val taskId: String,
    val type: ReminderType,
    val createdAt: Long
)
