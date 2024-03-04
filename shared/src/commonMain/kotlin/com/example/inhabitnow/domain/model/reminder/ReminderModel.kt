package com.example.inhabitnow.domain.model.reminder

import com.example.inhabitnow.core.type.ReminderType

data class ReminderModel(
    val id: String,
    val taskId: String,
    val type: ReminderType,
    val createdAt: Long
)
