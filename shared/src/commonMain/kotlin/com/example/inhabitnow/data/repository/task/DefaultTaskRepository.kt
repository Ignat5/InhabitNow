package com.example.inhabitnow.data.repository.task

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.core.util.randomUUID
import com.example.inhabitnow.data.data_source.task.TaskDataSource
import com.example.inhabitnow.data.model.task.TaskWithContentEntity
import com.example.inhabitnow.data.model.task.content.ArchiveContentEntity
import com.example.inhabitnow.data.model.task.content.BaseTaskContentEntity
import com.example.inhabitnow.data.model.task.content.FrequencyContentEntity
import com.example.inhabitnow.data.model.task.content.ProgressContentEntity
import com.example.inhabitnow.data.model.task.content.TaskContentEntity
import com.example.inhabitnow.data.util.toBaseTaskContentEntity
import com.example.inhabitnow.data.util.toEpochDay
import com.example.inhabitnow.data.util.toJson
import com.example.inhabitnow.data.util.toLocalDate
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

    override suspend fun saveTaskProgressContent(
        taskId: String,
        targetDate: LocalDate,
        content: TaskContentEntity.ProgressContent
    ): ResultModel<Unit> = saveTaskContent(
        taskId = taskId,
        targetDate = targetDate,
        content = content
    )

    override suspend fun saveTaskFrequencyContent(
        taskId: String,
        targetDate: LocalDate,
        content: TaskContentEntity.FrequencyContent
    ): ResultModel<Unit> = saveTaskContent(
        taskId = taskId,
        targetDate = targetDate,
        content = content
    )

    private suspend fun saveTaskContent(
        taskId: String,
        targetDate: LocalDate,
        content: TaskContentEntity
    ): ResultModel<Unit> = withContext(defaultDispatcher) {
        val contentType = when (content) {
            is TaskContentEntity.ProgressContent -> TaskContentEntity.Type.Progress
            is TaskContentEntity.FrequencyContent -> TaskContentEntity.Type.Frequency
            is TaskContentEntity.ArchiveContent -> TaskContentEntity.Type.Archive
        }
        taskDataSource.getTaskContentByTaskId(
            taskId = taskId,
            taskContentType = contentType.toJson(json)
        )?.let { taskContentTable ->
            val targetDateEpochDay = targetDate.toEpochDay()
            val isNew = targetDateEpochDay > taskContentTable.startEpochDay
            val contentId = if (isNew) randomUUID() else taskContentTable.id
            taskDataSource.insertTaskContent(
                taskContentTable.copy(
                    id = contentId,
                    content = content.toJson(json),
                    startEpochDay = targetDateEpochDay
                )
            )
        } ?: ResultModel.Error(IllegalStateException())
    }

}