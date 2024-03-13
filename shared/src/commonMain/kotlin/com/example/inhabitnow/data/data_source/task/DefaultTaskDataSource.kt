package com.example.inhabitnow.data.data_source.task

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.data_source.base.BaseDataSource
import com.example.inhabitnow.database.InhabitNowDatabase
import database.SelectTaskWithContentById
import database.TaskContentTable
import database.TaskTable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class DefaultTaskDataSource(
    private val db: InhabitNowDatabase,
    private val ioDispatcher: CoroutineDispatcher
) : BaseDataSource(db, ioDispatcher), TaskDataSource {

    private val taskDao = db.taskDaoQueries

    override fun readTaskWithContentById(taskId: String): Flow<List<SelectTaskWithContentById>> =
        readQueryList {
            taskDao.selectTaskWithContentById(taskId)
        }

    override suspend fun insertTaskWithContent(
        taskTable: TaskTable,
        allTaskContent: List<TaskContentTable>
    ): ResultModel<Unit> = runTransaction {
        taskDao.apply {
            insertTask(taskTable)
            allTaskContent.forEach { taskContentTable ->
                insertTaskContent(taskContentTable)
            }
        }
    }

}