package com.example.inhabitnow.data.repository.task

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.model.task.TaskWithContentEntity
import com.example.inhabitnow.data.model.task.content.TaskContentEntity
import com.example.inhabitnow.data.model.task.derived.FullTaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface TaskRepository {
    fun readTaskWithContentById(taskId: String): Flow<TaskWithContentEntity?>
    fun readTasksWithContentBySearchQuery(searchQuery: String): Flow<List<TaskWithContentEntity>>
    fun readFullTasksByDate(targetDate: LocalDate): Flow<List<FullTaskEntity>>
    suspend fun saveTaskWithContent(taskWithContentEntity: TaskWithContentEntity): ResultModel<Unit>
    suspend fun saveTaskById(taskId: String): ResultModel<Unit>
    suspend fun deleteTaskById(taskId: String): ResultModel<Unit>
    suspend fun updateTaskTitleById(
        taskId: String,
        title: String
    ): ResultModel<Unit>
    suspend fun updateTaskDescriptionById(
        taskId: String,
        description: String
    ): ResultModel<Unit>
    suspend fun updateTaskPriorityById(
        taskId: String,
        priority: String
    ): ResultModel<Unit>
    suspend fun updateTaskStartDateById(
        taskId: String,
        taskStartDate: LocalDate
    ): ResultModel<Unit>
    suspend fun updateTaskEndDateById(
        taskId: String,
        taskEndDate: LocalDate?
    ): ResultModel<Unit>
    suspend fun updateTaskStartEndDateById(
        taskId: String,
        taskStartDate: LocalDate,
        taskEndDate: LocalDate
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
    suspend fun saveTaskArchiveContent(
        taskId: String,
        targetDate: LocalDate,
        content: TaskContentEntity.ArchiveContent
    ): ResultModel<Unit>
}