package com.example.inhabitnow.data.util

import com.example.inhabitnow.core.type.ReminderType
import com.example.inhabitnow.core.type.TaskProgressType
import com.example.inhabitnow.core.type.TaskType
import com.example.inhabitnow.data.model.record.RecordModel
import com.example.inhabitnow.data.model.record.content.RecordContentModel
import com.example.inhabitnow.data.model.reminder.ReminderModel
import com.example.inhabitnow.data.model.reminder.content.ReminderContentModel
import com.example.inhabitnow.data.model.task.TaskModel
import com.example.inhabitnow.data.model.task.content.ArchiveContentEntity
import com.example.inhabitnow.data.model.task.content.BaseTaskContentModel
import com.example.inhabitnow.data.model.task.content.FrequencyContentEntity
import com.example.inhabitnow.data.model.task.content.ProgressContentEntity
import com.example.inhabitnow.data.model.task.content.TaskContentModel
import database.RecordTable
import database.ReminderTable
import database.TaskContentTable
import database.TaskTable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/** Task **/

fun TaskModel.toTaskTable(json: Json) = TaskTable(
    id = id,
    type = type.toJson(json),
    progressType = progressType.toJson(json),
    title = title,
    description = description,
    startEpochDay = startDate.toEpochDay(),
    endEpochDay = (endDate ?: DataConst.distantFutureDate).toEpochDay(),
    priority = priority,
    createdAt = createdAt,
    deletedAt = deletedAt
)

fun TaskTable.toTaskModel(json: Json) = TaskModel(
    id = this.id,
    type = this.type.fromJsonTaskType(json),
    progressType = this.progressType.fromJsonTaskProgressType(json),
    title = this.title,
    description = this.description,
    startDate = this.startEpochDay.toLocalDate(),
    endDate = this.endEpochDay.toLocalDate().let { date ->
        if (date != DataConst.distantFutureDate) date
        else null
    },
    priority = this.priority,
    createdAt = this.createdAt,
    deletedAt = this.deletedAt
)

fun <T : TaskContentModel> BaseTaskContentModel<T>.toTaskContentTable(json: Json) =
    TaskContentTable(
        id = id,
        taskId = taskId,
        content = content.toJson(json),
        contentType = when (this) {
            is ProgressContentEntity -> TaskContentModel.Type.Progress
            is FrequencyContentEntity -> TaskContentModel.Type.Frequency
            is ArchiveContentEntity -> TaskContentModel.Type.Archive
        }.toJson(json),
        startEpochDay = startEpochDay,
        createdAt = createdAt
    )

fun TaskContentTable.toBaseTaskContentModel(json: Json): BaseTaskContentModel<*>? = try {
    when (val decodedContent = content.fromJsonTaskContentEntity(json)) {
        is TaskContentModel.ProgressContent -> {
            ProgressContentEntity(
                id = id,
                taskId = taskId,
                content = decodedContent,
                startEpochDay = startEpochDay,
                createdAt = createdAt
            )
        }

        is TaskContentModel.FrequencyContent -> {
            FrequencyContentEntity(
                id = id,
                taskId = taskId,
                content = decodedContent,
                startEpochDay = startEpochDay,
                createdAt = createdAt
            )
        }

        is TaskContentModel.ArchiveContent -> {
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
fun String.fromJsonTaskType(json: Json) = json.decodeFromString<TaskType>(this)

fun TaskProgressType.toJson(json: Json) = json.encodeToString(this)
fun String.fromJsonTaskProgressType(json: Json) = json.decodeFromString<TaskProgressType>(this)

fun TaskContentModel.Type.toJson(json: Json) = json.encodeToString(this)
fun TaskContentModel.toJson(json: Json) = json.encodeToString<TaskContentModel>(this)

private fun String.fromJsonTaskContentEntity(json: Json): TaskContentModel =
    json.decodeFromString<TaskContentModel>(this)

private fun String.fromJsonContentType(json: Json) =
    json.decodeFromString<TaskContentModel.Type>(this)

/** reminder **/
fun ReminderTable.toReminderModel(json: Json) = ReminderModel(
    id = this.id,
    taskId = this.taskId,
    type = this.type.fromJsonReminderType(json),
    time = this.time.fromJsonTime(json),
    schedule = this.schedule.fromJsonScheduleContent(json),
    createdAt = this.createdAt
)

fun ReminderModel.toReminderTable(json: Json) = ReminderTable(
    id = this.id,
    taskId = this.taskId,
    type = this.type.toJson(json),
    schedule = this.schedule.toJson(json),
    time = this.time.toJson(json),
    createdAt = this.createdAt
)

private fun ReminderContentModel.ScheduleContent.toJson(json: Json) =
    json.encodeToString<ReminderContentModel.ScheduleContent>(this)

private fun String.fromJsonScheduleContent(json: Json) =
    json.decodeFromString<ReminderContentModel.ScheduleContent>(this)

private fun LocalTime.toJson(json: Json) =
    json.encodeToString<LocalTime>(this)

private fun String.fromJsonTime(json: Json) =
    json.decodeFromString<LocalTime>(this)

private fun ReminderType.toJson(json: Json) =
    json.encodeToString(this)

private fun String.fromJsonReminderType(json: Json) =
    json.decodeFromString<ReminderType>(this)


/** record **/
fun RecordTable.toRecordModel(json: Json) = RecordModel(
    id = id,
    taskId = taskId,
    date = epochDay.toLocalDate(),
    entry = entry.fromJsonRecordEntry(json),
    createdAt = createdAt
)

fun RecordModel.toRecordTable(json: Json) = RecordTable(
    id = id,
    taskId = taskId,
    epochDay = date.toEpochDay(),
    entry = entry.toJson(json),
    createdAt = createdAt
)

private fun RecordContentModel.Entry.toJson(json: Json) =
    json.encodeToString<RecordContentModel.Entry>(this)

private fun String.fromJsonRecordEntry(json: Json) =
    json.decodeFromString<RecordContentModel.Entry>(this)

/** other **/

private fun LocalDate.toEpochDay() = this.toEpochDays().toLong()
private fun Long.toLocalDate() = LocalDate.fromEpochDays(this.toInt())
