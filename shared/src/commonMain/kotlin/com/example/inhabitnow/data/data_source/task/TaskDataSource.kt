package com.example.inhabitnow.data.data_source.task

import com.example.inhabitnow.core.model.ResultModel
import database.SelectFullTasksByDate
import database.SelectFullTasksByType
import database.SelectTaskWithAllTimeContentById
import database.SelectTaskWithContentById
import database.SelectTasksWithContentBySearchQuery
import database.TaskContentTable
import database.TaskTable
import kotlinx.coroutines.flow.Flow

interface TaskDataSource {

    fun readTaskWithContentById(taskId: String): Flow<List<SelectTaskWithContentById>>

    fun readTasksWithContentBySearchQuery(searchQuery: String): Flow<List<SelectTasksWithContentBySearchQuery>>

    fun readFullTasksByDate(targetEpochDay: Long): Flow<List<SelectFullTasksByDate>>

    fun readFullTasksByType(allTaskTypes: Set<String>): Flow<List<SelectFullTasksByType>>

    fun readTaskWithAllTimeContentById(taskId: String): Flow<List<SelectTaskWithAllTimeContentById>>

    suspend fun insertTaskWithContent(
        taskTable: TaskTable,
        allTaskContent: List<TaskContentTable>
    ): ResultModel<Unit>

    suspend fun insertTaskContent(taskContentTable: TaskContentTable): ResultModel<Unit>

    suspend fun updateTaskTitleById(taskId: String, title: String): ResultModel<Unit>

    suspend fun updateTaskDescriptionById(
        taskId: String,
        description: String
    ): ResultModel<Unit>

    suspend fun updateTaskPriorityById(
        taskId: String,
        priority: Long
    ): ResultModel<Unit>

    suspend fun updateTaskDeletedAtById(
        taskId: String,
        deletedAt: Long?
    ): ResultModel<Unit>

    suspend fun deleteTaskById(taskId: String): ResultModel<Unit>

    suspend fun updateTaskStartDateById(
        taskId: String,
        taskStartEpochDay: Long
    ): ResultModel<Unit>

    suspend fun updateTaskEndDateById(
        taskId: String,
        taskEndEpochDay: Long
    ): ResultModel<Unit>

    suspend fun updateTaskStartEndDateById(
        taskId: String,
        taskStartEpochDay: Long,
        taskEndEpochDay: Long
    ): ResultModel<Unit>

    suspend fun updateTaskContentById(
        contentId: String,
        content: String
    ): ResultModel<Unit>

    suspend fun getTaskContentByTaskId(
        taskId: String,
        taskContentType: String
    ): TaskContentTable?

}