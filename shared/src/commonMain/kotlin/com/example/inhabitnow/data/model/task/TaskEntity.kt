package com.example.inhabitnow.data.model.task

import com.example.inhabitnow.core.type.TaskProgressType
import com.example.inhabitnow.core.type.TaskType

data class TaskEntity(
    val id: String,
    val type: TaskType,
    val progressType: TaskProgressType,
    val title: String,
    val description: String,
    val startEpochDay: Long,
    val endEpochDay: Long,
    val priority: Long,
    val createdAt: Long,
    val deletedAt: Long?
)
