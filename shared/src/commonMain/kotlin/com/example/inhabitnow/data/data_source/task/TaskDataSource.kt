package com.example.inhabitnow.data.data_source.task

import com.example.inhabitnow.core.model.ResultModel
import database.SelectTaskWithContentById
import database.TaskContentTable
import database.TaskTable
import kotlinx.coroutines.flow.Flow

interface TaskDataSource {

    fun readTaskWithContentById(taskId: String): Flow<List<SelectTaskWithContentById>>

    suspend fun insertTaskWithContent(
        taskTable: TaskTable,
        allTaskContent: List<TaskContentTable>
    ): ResultModel<Unit>

    suspend fun updateTaskTitleById(taskId: String, title: String): ResultModel<Unit>

}