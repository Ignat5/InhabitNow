package com.example.inhabitnow.data.data_source.task

import com.example.inhabitnow.core.model.ResultModel
import database.TaskContentTable
import database.TaskTable

interface TaskDataSource {

    suspend fun insertTaskWithContent(
        taskTable: TaskTable,
        allTaskContent: List<TaskContentTable>
    ): ResultModel<Unit>

}