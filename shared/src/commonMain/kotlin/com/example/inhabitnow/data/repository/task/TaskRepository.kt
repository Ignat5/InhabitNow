package com.example.inhabitnow.data.repository.task

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.model.task.TaskWithContentEntity
import com.example.inhabitnow.data.model.task.content.FrequencyContentEntity
import com.example.inhabitnow.data.model.task.content.ProgressContentEntity
import com.example.inhabitnow.data.model.task.content.TaskContentEntity
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun readTaskWithContentById(taskId: String): Flow<TaskWithContentEntity?>
    suspend fun saveTaskWithContent(taskWithContentEntity: TaskWithContentEntity): ResultModel<Unit>
    suspend fun updateTaskTitleById(
        taskId: String,
        title: String
    ): ResultModel<Unit>
    suspend fun updateTaskProgressContentById(
        contentId: String,
        progressContent: TaskContentEntity.ProgressContent
    ): ResultModel<Unit>
    suspend fun saveTaskProgressContent(progressContentEntity: ProgressContentEntity): ResultModel<Unit>
    suspend fun getTaskProgressContentByTaskId(taskId: String): ProgressContentEntity?
    suspend fun saveTaskFrequencyContent(frequencyContentEntity: FrequencyContentEntity): ResultModel<Unit>
    suspend fun getTaskFrequencyContentByTaskId(taskId: String): FrequencyContentEntity?
}