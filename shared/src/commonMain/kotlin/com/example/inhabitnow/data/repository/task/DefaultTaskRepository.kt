package com.example.inhabitnow.data.repository.task

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.core.type.TaskType
import com.example.inhabitnow.core.util.randomUUID
import com.example.inhabitnow.data.data_source.task.TaskDataSource
import com.example.inhabitnow.data.model.task.TaskWithContentEntity
import com.example.inhabitnow.data.model.task.content.TaskContentEntity
import com.example.inhabitnow.data.model.task.derived.FullTaskEntity
import com.example.inhabitnow.data.model.task.derived.SelectFullTasksQuery
import com.example.inhabitnow.data.util.toEpochDay
import com.example.inhabitnow.data.util.toJson
import com.example.inhabitnow.data.util.toReminderEntity
import com.example.inhabitnow.data.util.toReminderTable
import com.example.inhabitnow.data.util.toSelectFullTasksQuery
import com.example.inhabitnow.data.util.toTagEntity
import com.example.inhabitnow.data.util.toTagTable
import com.example.inhabitnow.data.util.toTaskContentTable
import com.example.inhabitnow.data.util.toTaskEntity
import com.example.inhabitnow.data.util.toTaskTable
import com.example.inhabitnow.data.util.toTaskWithContentEntity
import database.TaskContentTable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
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

    override fun readTasksWithContentBySearchQuery(searchQuery: String): Flow<List<TaskWithContentEntity>> =
        taskDataSource.readTasksWithContentBySearchQuery(searchQuery).map { queryList ->
            if (queryList.isNotEmpty()) {
                withContext(defaultDispatcher) {
                    val allTaskIds = queryList.distinctBy { it.task_id }.map { it.task_id }
                    allTaskIds.map { taskId ->
                        async {
                            val tTask = queryList.find { it.task_id == taskId }?.toTaskTable()
                                ?: return@async null
                            val allTaskContent = queryList
                                .asSequence()
                                .filter { it.taskContent_taskId == taskId }
                                .distinctBy { it.taskContent_id }
                                .map { it.toTaskContentTable() }
                                .toList()

                            return@async tTask.toTaskWithContentEntity(
                                allTaskContent = allTaskContent,
                                json = json
                            )
                        }
                    }.awaitAll().filterNotNull()
                }
            } else emptyList()
        }

    override fun readFullTasksByDate(targetDate: LocalDate): Flow<List<FullTaskEntity>> =
        taskDataSource.readFullTasksByDate(targetDate.toEpochDay()).map { queryList ->
            if (queryList.isNotEmpty()) {
                withContext(defaultDispatcher) {
                    queryList.map { it.toSelectFullTasksQuery() }.toFullTaskEntityList()
                }
            } else emptyList()
        }

    override fun readFullTasksByType(allTaskTypes: Set<TaskType>): Flow<List<FullTaskEntity>> =
        taskDataSource.readFullTasksByType(allTaskTypes.map { it.toJson(json) }.toSet())
            .map { queryList ->
                if (queryList.isNotEmpty()) {
                    withContext(defaultDispatcher) {
                        queryList.map { it.toSelectFullTasksQuery() }.toFullTaskEntityList()
                    }
                } else emptyList()
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

    override suspend fun updateTaskDescriptionById(
        taskId: String,
        description: String
    ): ResultModel<Unit> = taskDataSource.updateTaskDescriptionById(
        taskId = taskId,
        description = description
    )

    override suspend fun updateTaskPriorityById(
        taskId: String,
        priority: Int
    ): ResultModel<Unit> = taskDataSource.updateTaskPriorityById(
        taskId = taskId,
        priority = priority.toLong()
    )

    override suspend fun saveTaskById(taskId: String): ResultModel<Unit> =
        taskDataSource.updateTaskDeletedAtById(
            taskId = taskId,
            deletedAt = null
        )

    override suspend fun deleteTaskById(taskId: String): ResultModel<Unit> =
        taskDataSource.updateTaskDeletedAtById(
            taskId = taskId,
            deletedAt = Clock.System.now().toEpochMilliseconds()
        )

    override suspend fun updateTaskStartDateById(
        taskId: String,
        taskStartDate: LocalDate
    ): ResultModel<Unit> = taskDataSource.updateTaskStartDateById(
        taskId = taskId,
        taskStartEpochDay = taskStartDate.toEpochDay()
    )

    override suspend fun updateTaskEndDateById(
        taskId: String,
        taskEndDate: LocalDate?
    ): ResultModel<Unit> = taskDataSource.updateTaskEndDateById(
        taskId = taskId,
        taskEndEpochDay = taskEndDate.toEpochDay()
    )

    override suspend fun updateTaskStartEndDateById(
        taskId: String,
        taskStartDate: LocalDate,
        taskEndDate: LocalDate?
    ): ResultModel<Unit> = taskDataSource.updateTaskStartEndDateById(
        taskId = taskId,
        taskStartEpochDay = taskStartDate.toEpochDay(),
        taskEndEpochDay = taskEndDate.toEpochDay()
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

    override suspend fun saveTaskArchiveContent(
        taskId: String,
        targetDate: LocalDate,
        content: TaskContentEntity.ArchiveContent
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
            if (isNew) {
                taskDataSource.insertTaskContent(
                    TaskContentTable(
                        id = randomUUID(),
                        taskId = taskContentTable.taskId,
                        contentType = taskContentTable.contentType,
                        content = content.toJson(json),
                        startEpochDay = targetDateEpochDay,
                        createdAt = Clock.System.now().toEpochMilliseconds()
                    )
                )
            } else {
                taskDataSource.updateTaskContentById(
                    contentId = taskContentTable.id,
                    content = content.toJson(json)
                )
            }
        } ?: ResultModel.Error(IllegalStateException())
    }

    private suspend fun List<SelectFullTasksQuery>.toFullTaskEntityList() = this.let { queryList ->
        coroutineScope {
            val allTaskIds = queryList.distinctBy { it.task_id }.map { it.task_id }
            allTaskIds.map { taskId ->
                async {
                    val taskWithContentEntityDef = async {
                        kotlin.run {
                            val tTask = queryList
                                .find { it.task_id == taskId }
                                ?.toTaskTable()
                                ?: return@run null

                            val allTaskContent = queryList
                                .asSequence()
                                .filter { it.taskContent_taskId == taskId }
                                .distinctBy { it.taskContent_id }
                                .map { it.toTaskContentTable() }
                                .toList()

                            tTask.toTaskWithContentEntity(
                                allTaskContent = allTaskContent,
                                json = json
                            )
                        }
                    }

                    val allRemindersDef = async {
                        queryList
                            .asSequence()
                            .filter { it.reminder_taskId == taskId }
                            .distinctBy { it.reminder_id }
                            .mapNotNull { it.toReminderTable() }
                            .map { it.toReminderEntity(json) }
                            .toList()
                    }

                    val allTagsDef = async {
                        queryList
                            .asSequence()
                            .filter { it.tagCross_taskId == taskId }
                            .distinctBy { it.tagCross_tagId }
                            .mapNotNull { it.toTagTable() }
                            .map { it.toTagEntity() }
                            .toList()
                    }

                    FullTaskEntity(
                        taskWithContentEntity = taskWithContentEntityDef.await()
                            ?: return@async null,
                        allReminders = allRemindersDef.await(),
                        allTags = allTagsDef.await()
                    )
                }
            }.awaitAll().filterNotNull()
        }
    }

}