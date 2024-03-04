package com.example.inhabitnow.domain.model.task

import com.example.inhabitnow.core.type.TaskProgressType
import com.example.inhabitnow.core.type.TaskType
import kotlinx.datetime.LocalDate

data class TaskModel(
    val id: String,
    val type: TaskType,
    val progressType: TaskProgressType,
    val title: String,
    val description: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val priority: Long,
    val createdAt: Long,
    val deletedAt: Long?
)
