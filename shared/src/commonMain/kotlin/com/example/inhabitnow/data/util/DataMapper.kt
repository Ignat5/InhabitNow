package com.example.inhabitnow.data.util

import com.example.inhabitnow.core.type.ReminderType
import com.example.inhabitnow.core.type.TaskProgressType
import com.example.inhabitnow.core.type.TaskType
import com.example.inhabitnow.data.model.reminder.ReminderEntity
import com.example.inhabitnow.data.model.reminder.content.ReminderContentEntity
import com.example.inhabitnow.data.model.task.TaskEntity
import com.example.inhabitnow.data.model.task.content.ArchiveContentEntity
import com.example.inhabitnow.data.model.task.content.BaseTaskContentEntity
import com.example.inhabitnow.data.model.task.content.FrequencyContentEntity
import com.example.inhabitnow.data.model.task.content.ProgressContentEntity
import com.example.inhabitnow.data.model.task.content.TaskContentEntity
import database.ReminderTable
import database.TaskContentTable
import database.TaskTable
import kotlinx.datetime.LocalTime
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
        contentType = when (this) {
            is ProgressContentEntity -> TaskContentEntity.Type.Progress
            is FrequencyContentEntity -> TaskContentEntity.Type.Frequency
            is ArchiveContentEntity -> TaskContentEntity.Type.Archive
        }.toJson(json),
        startEpochDay = startEpochDay,
        createdAt = createdAt
    )

fun TaskContentTable.toBaseTaskContentEntity(json: Json): BaseTaskContentEntity<*>? = try {
    when (val decodedContent = content.fromJsonTaskContentEntity(json)) {
        is TaskContentEntity.ProgressContent -> {
            ProgressContentEntity(
                id = id,
                taskId = taskId,
                content = decodedContent,
                startEpochDay = startEpochDay,
                createdAt = createdAt
            )
        }

        is TaskContentEntity.FrequencyContent -> {
            FrequencyContentEntity(
                id = id,
                taskId = taskId,
                content = decodedContent,
                startEpochDay = startEpochDay,
                createdAt = createdAt
            )
        }

        is TaskContentEntity.ArchiveContent -> {
            ArchiveContentEntity(
                id = id,
                taskId = taskId,
                content = decodedContent,
                startEpochDay = startEpochDay,
                createdAt = createdAt
            )
        }
    }
} catch (e: Exception) {
    null
}

fun TaskType.toJson(json: Json) = json.encodeToString(this)
fun TaskProgressType.toJson(json: Json) = json.encodeToString(this)
fun TaskContentEntity.Type.toJson(json: Json) = json.encodeToString(this)
fun TaskContentEntity.toJson(json: Json) = json.encodeToString<TaskContentEntity>(this)

private fun String.fromJsonTaskContentEntity(json: Json): TaskContentEntity =
    json.decodeFromString<TaskContentEntity>(this)

private fun String.fromJsonContentType(json: Json) =
    json.decodeFromString<TaskContentEntity.Type>(this)

/** reminder **/
fun ReminderTable.toReminderEntity(json: Json) = ReminderEntity(
    id = this.id,
    taskId = this.taskId,
    type = this.type.fromJsonReminderType(json),
    time = this.time.fromJsonTime(json),
    schedule = this.schedule.fromJsonScheduleContent(json),
    createdAt = this.createdAt
)

fun ReminderEntity.toReminderTable(json: Json) = ReminderTable(
    id = this.id,
    taskId = this.taskId,
    type = this.type.toJson(json),
    schedule = this.schedule.toJson(json),
    time = this.time.toJson(json),
    createdAt = this.createdAt
)

private fun ReminderContentEntity.ScheduleContent.toJson(json: Json) =
    json.encodeToString<ReminderContentEntity.ScheduleContent>(this)

private fun String.fromJsonScheduleContent(json: Json) =
    json.decodeFromString<ReminderContentEntity.ScheduleContent>(this)

private fun LocalTime.toJson(json: Json) =
    json.encodeToString<LocalTime>(this)

private fun String.fromJsonTime(json: Json) =
    json.decodeFromString<LocalTime>(this)

private fun ReminderType.toJson(json: Json) =
    json.encodeToString(this)

private fun String.fromJsonReminderType(json: Json) =
    json.decodeFromString<ReminderType>(this)
