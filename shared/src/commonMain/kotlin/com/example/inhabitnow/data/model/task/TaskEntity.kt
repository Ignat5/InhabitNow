package com.example.inhabitnow.data.model.task

import com.example.inhabitnow.core.type.TaskProgressType
import com.example.inhabitnow.core.type.TaskType
import kotlinx.datetime.LocalDate

data class TaskEntity(
    val id: String,
    val type: TaskType,
    val progressType: TaskProgressType,
    val title: String,
    val description: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val priority: Int,
    val createdAt: Long,
    val deletedAt: Long?
)
