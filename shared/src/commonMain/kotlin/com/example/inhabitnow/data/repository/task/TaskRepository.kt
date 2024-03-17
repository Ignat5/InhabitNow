package com.example.inhabitnow.data.repository.task

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.model.task.TaskWithContentEntity
import com.example.inhabitnow.data.model.task.content.FrequencyContentEntity
import com.example.inhabitnow.data.model.task.content.ProgressContentEntity
import com.example.inhabitnow.data.model.task.content.TaskContentEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface TaskRepository {
    fun readTaskWithContentById(taskId: String): Flow<TaskWithContentEntity?>
    suspend fun saveTaskWithContent(taskWithContentEntity: TaskWithContentEntity): ResultModel<Unit>
    suspend fun updateTaskTitleById(
        taskId: String,
        title: String
    ): ResultModel<Unit>
    suspend fun saveTaskProgressContent(
        taskId: String,
        targetDate: LocalDate,
        content: TaskContentEntity.ProgressContent
    ): ResultModel<Unit>
    suspend fun saveTaskFrequencyContent(
        taskId: String,
        targetDate: LocalDate,
        content: TaskContentEntity.FrequencyContent
    ): ResultModel<Unit>
}