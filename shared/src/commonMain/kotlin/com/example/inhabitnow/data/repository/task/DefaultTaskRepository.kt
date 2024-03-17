package com.example.inhabitnow.data.repository.task

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.data_source.task.TaskDataSource
import com.example.inhabitnow.data.model.task.TaskWithContentEntity
import com.example.inhabitnow.data.model.task.content.BaseTaskContentEntity
import com.example.inhabitnow.data.model.task.content.FrequencyContentEntity
import com.example.inhabitnow.data.model.task.content.ProgressContentEntity
import com.example.inhabitnow.data.model.task.content.TaskContentEntity
import com.example.inhabitnow.data.util.toBaseTaskContentEntity
import com.example.inhabitnow.data.util.toJson
import com.example.inhabitnow.data.util.toTaskContentTable
import com.example.inhabitnow.data.util.toTaskTable
import com.example.inhabitnow.data.util.toTaskWithContentEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json

class DefaultTaskRepository(
    private val taskDataSource: TaskDataSource,
    private val json: Json,
    private val defaultDispatcher: CoroutineDispatcher
) : TaskRepository {

    override fun readTaskWithContentById(taskId: String): Flow<TaskWithContentEntity?> =
        taskDataSource.readTaskWithContentById(taskId).map { queryList ->
            if (queryList.isNotEmpty()) {
                withContext(defaultDispatcher) {
                    val tTask = queryList.first().toTaskTable()
                    val allTaskContent = queryList
                        .distinctBy { it.taskContent_id }
                        .map { it.toTaskContentTable() }
                    tTask.toTaskWithContentEntity(
                        allTaskContent = allTaskContent,
                        json = json
                    )
                }
            } else null
        }

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

    override suspend fun updateTaskTitleById(
        taskId: String,
        title: String
    ): ResultModel<Unit> = taskDataSource.updateTaskTitleById(
        taskId = taskId,
        title = title
    )

    override suspend fun updateTaskProgressContentById(
        contentId: String,
        progressContent: TaskContentEntity.ProgressContent
    ): ResultModel<Unit> = taskDataSource.updateTaskContentById(
        contentId = contentId,
        content = progressContent.toJson(json)
    )

    private suspend fun saveTaskContent(
        taskId: String,
        date: LocalDate,
        content: BaseTaskContentEntity
    ) {

    }

    override suspend fun saveTaskProgressContent(progressContentEntity: ProgressContentEntity): ResultModel<Unit> =
        saveBaseTaskContent(progressContentEntity)

    override suspend fun getTaskProgressContentByTaskId(taskId: String): ProgressContentEntity? =
        withContext(defaultDispatcher) {
            getBaseTaskContentByTaskId(
                taskId = taskId,
                contentType = TaskContentEntity.Type.Progress
            ) as? ProgressContentEntity
        }

    override suspend fun saveTaskFrequencyContent(frequencyContentEntity: FrequencyContentEntity): ResultModel<Unit> =
        saveBaseTaskContent(frequencyContentEntity)

    override suspend fun getTaskFrequencyContentByTaskId(taskId: String): FrequencyContentEntity? =
        withContext(defaultDispatcher) {
            getBaseTaskContentByTaskId(
                taskId = taskId,
                contentType = TaskContentEntity.Type.Frequency
            ) as? FrequencyContentEntity
        }

    private suspend fun saveBaseTaskContent(contentEntity: BaseTaskContentEntity) =
        taskDataSource.insertTaskContent(contentEntity.toTaskContentTable(json))

    private suspend fun getBaseTaskContentByTaskId(
        taskId: String,
        contentType: TaskContentEntity.Type
    ): BaseTaskContentEntity? = taskDataSource.getTaskContentByTaskId(
        taskId = taskId,
        taskContentType = contentType.toJson(json)
    )?.toBaseTaskContentEntity(json)

}