package com.example.inhabitnow.data.util

import com.example.inhabitnow.core.type.TaskProgressType
import com.example.inhabitnow.core.type.TaskType
import com.example.inhabitnow.data.model.task.TaskEntity
import com.example.inhabitnow.data.model.task.content.base.BaseTaskContentEntity
import com.example.inhabitnow.data.model.task.content.base.TaskContentEntity
import database.TaskContentTable
import database.TaskTable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/** Task **/

fun TaskEntity.toTaskTable(json: Json) = TaskTable(
    id = id,
    type = type.toJson(json),
    progressType = progressType.toJson(json),
    title = title,
    description = description,
    startEpochDay = startEpochDay,
    endEpochDay = endEpochDay,
    priority = priority,
    createdAt = createdAt,
    deletedAt = deletedAt
)

fun <T : TaskContentEntity> BaseTaskContentEntity<T>.toTaskContentTable(json: Json) =
    TaskContentTable(
        id = id,
        taskId = taskId,
        content = content.toJson(json),
        contentType = when (content) {
            is TaskContentEntity.ProgressContent -> TaskContentEntity.Type.Progress
            is TaskContentEntity.FrequencyContent -> TaskContentEntity.Type.Frequency
            is TaskContentEntity.ArchiveContent -> TaskContentEntity.Type.Archive
            else -> throw IllegalStateException()
        }.toJson(json),
        startEpochDay = startEpochDay,
        createdAt = createdAt
    )


fun TaskType.toJson(json: Json) = json.encodeToString(this)
fun TaskProgressType.toJson(json: Json) = json.encodeToString(this)
fun TaskContentEntity.Type.toJson(json: Json) = json.encodeToString(this)
fun TaskContentEntity.toJson(json: Json) = json.encodeToString<TaskContentEntity>(this)