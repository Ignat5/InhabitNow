package com.example.inhabitnow.data.repository.task

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.data_source.task.TaskDataSource
import com.example.inhabitnow.data.model.task.TaskWithContentEntity
import com.example.inhabitnow.data.util.toTaskContentTable
import com.example.inhabitnow.data.util.toTaskTable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class DefaultTaskRepository(
    private val taskDataSource: TaskDataSource,
    private val json: Json,
    private val defaultDispatcher: CoroutineDispatcher
) : TaskRepository {

    override suspend fun saveTaskWithContent(taskWithContentEntity: TaskWithContentEntity): ResultModel<Unit> =
        withContext(defaultDispatcher) {
            taskDataSource.insertTaskWithContent(
                taskTable = taskWithContentEntity.task.toTaskTable(json),
                allTaskContent = listOf(
                    taskWithContentEntity.progressContent.toTaskContentTable(json),
                    taskWithContentEntity.frequencyContent.toTaskContentTable(json),
                    taskWithContentEntity.archiveContent.toTaskContentTable(json)
                )
            )
        }

}