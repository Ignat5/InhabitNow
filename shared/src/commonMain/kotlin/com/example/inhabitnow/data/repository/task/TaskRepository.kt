package com.example.inhabitnow.data.repository.task

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.model.task.TaskWithContentModel

interface TaskRepository {
    suspend fun saveTaskWithContent(taskWithContentModel: TaskWithContentModel): ResultModel<Unit>
}