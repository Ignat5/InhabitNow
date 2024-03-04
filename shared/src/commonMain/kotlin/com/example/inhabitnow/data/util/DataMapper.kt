package com.example.inhabitnow.data.util

import com.example.inhabitnow.core.type.ReminderType
import com.example.inhabitnow.core.type.TaskProgressType
import com.example.inhabitnow.core.type.TaskType
import com.example.inhabitnow.data.model.reminder.ReminderEntity
import com.example.inhabitnow.data.model.reminder.ReminderWithContentEntity
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
fun ReminderTable.toReminderWithContentEntity(json: Json) = ReminderWithContentEntity(
    reminder = this.toReminderEntity(json),
    timeContent = this.timeContent.fromJsonTimeContent(json),
    scheduleContent = this.scheduleContent.fromJsonScheduleContent(json)
)

private fun ReminderTable.toReminderEntity(json: Json) = ReminderEntity(
    id = this.id,
    taskId = this.taskId,
    type = this.type.fromJsonReminderType(json),
    createdAt = this.createdAt
)

fun ReminderWithContentEntity.toReminderTable(json: Json) = ReminderTable(
    id = this.reminder.id,
    taskId = this.reminder.taskId,
    type = this.reminder.type.toJson(json),
    scheduleContent = this.scheduleContent.toJson(json),
    timeContent = this.timeContent.toJson(json),
    createdAt = this.reminder.createdAt
)

private fun ReminderContentEntity.ScheduleContent.toJson(json: Json) =
    json.encodeToString<ReminderContentEntity.ScheduleContent>(this)

private fun String.fromJsonScheduleContent(json: Json) =
    json.decodeFromString<ReminderContentEntity.ScheduleContent>(this)

private fun ReminderContentEntity.TimeContent.toJson(json: Json) =
    json.encodeToString<ReminderContentEntity.TimeContent>(this)

private fun String.fromJsonTimeContent(json: Json) =
    json.decodeFromString<ReminderContentEntity.TimeContent>(this)

private fun ReminderType.toJson(json: Json) =
    json.encodeToString(this)

private fun String.fromJsonReminderType(json: Json) =
    json.decodeFromString<ReminderType>(this)
