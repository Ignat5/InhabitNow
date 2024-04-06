package com.example.inhabitnow.data.repository.task

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.core.type.TaskType
import com.example.inhabitnow.data.model.task.TaskWithContentEntity
import com.example.inhabitnow.data.model.task.content.ArchiveContentEntity
import com.example.inhabitnow.data.model.task.content.FrequencyContentEntity
import com.example.inhabitnow.data.model.task.content.ProgressContentEntity
import com.example.inhabitnow.data.model.task.content.TaskContentEntity
import com.example.inhabitnow.data.model.task.derived.FullTaskEntity
import com.example.inhabitnow.data.model.task.derived.TaskWithAllContentEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface TaskRepository {
    fun readTaskWithContentById(taskId: String): Flow<TaskWithContentEntity?>
    fun readTasksWithContentBySearchQuery(searchQuery: String): Flow<List<TaskWithContentEntity>>
    fun readFullTasksByDate(targetDate: LocalDate): Flow<List<FullTaskEntity>>
    fun readFullTasksByType(allTaskTypes: Set<TaskType>): Flow<List<FullTaskEntity>>
    fun readTaskWithAllTimeContentById(taskId: String): Flow<TaskWithAllContentEntity?>
    suspend fun getTaskProgressByTaskId(taskId: String): ProgressContentEntity?
    suspend fun getTaskFrequencyByTaskId(taskId: String): FrequencyContentEntity?
    suspend fun getTaskArchiveByTaskId(taskId: String): ArchiveContentEntity?
    suspend fun saveTaskWithContent(taskWithContentEntity: TaskWithContentEntity): ResultModel<Unit>
    suspend fun saveTaskById(taskId: String): ResultModel<Unit>
    suspend fun saveTaskProgress(
        taskId: String,
        progressContent: TaskContentEntity.ProgressContent,
        startDate: LocalDate
    ): ResultModel<Unit>
    suspend fun saveTaskFrequency(
        taskId: String,
        frequencyContent: TaskContentEntity.FrequencyContent,
        startDate: LocalDate
    ): ResultModel<Unit>
    suspend fun saveTaskArchive(
        taskId: String,
        archiveContent: TaskContentEntity.ArchiveContent,
        startDate: LocalDate
    ): ResultModel<Unit>
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
        priority: Int
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
        taskEndDate: LocalDate?
    ): ResultModel<Unit>
    suspend fun updateTaskProgress(
        contentId: String,
        content: TaskContentEntity.ProgressContent
    ): ResultModel<Unit>
    suspend fun updateTaskFrequency(
        contentId: String,
        content: TaskContentEntity.FrequencyContent
    ): ResultModel<Unit>
    suspend fun updateTaskArchive(
        contentId: String,
        content: TaskContentEntity.ArchiveContent
    ): ResultModel<Unit>
}