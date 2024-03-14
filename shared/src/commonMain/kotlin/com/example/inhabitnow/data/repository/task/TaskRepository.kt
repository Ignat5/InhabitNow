package com.example.inhabitnow.data.repository.task

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.model.task.TaskWithContentEntity
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun readTaskWithContentById(taskId: String): Flow<TaskWithContentEntity?>
    suspend fun saveTaskWithContent(taskWithContentEntity: TaskWithContentEntity): ResultModel<Unit>
    suspend fun updateTaskTitleById(
        taskId: String,
        title: String
    ): ResultModel<Unit>
}