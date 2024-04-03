package com.example.inhabitnow.data.util

import com.example.inhabitnow.core.type.ReminderType
import com.example.inhabitnow.core.type.TaskProgressType
import com.example.inhabitnow.core.type.TaskType
import com.example.inhabitnow.data.model.record.RecordEntity
import com.example.inhabitnow.data.model.record.content.RecordContentEntity
import com.example.inhabitnow.data.model.reminder.ReminderEntity
import com.example.inhabitnow.data.model.reminder.content.ReminderContentEntity
import com.example.inhabitnow.data.model.tag.TagEntity
import com.example.inhabitnow.data.model.task.TaskEntity
import com.example.inhabitnow.data.model.task.TaskWithContentEntity
import com.example.inhabitnow.data.model.task.content.ArchiveContentEntity
import com.example.inhabitnow.data.model.task.content.BaseTaskContentEntity
import com.example.inhabitnow.data.model.task.content.FrequencyContentEntity
import com.example.inhabitnow.data.model.task.content.ProgressContentEntity
import com.example.inhabitnow.data.model.task.content.TaskContentEntity
import com.example.inhabitnow.data.model.task.derived.SelectFullTasksQuery
import database.RecordTable
import database.ReminderTable
import database.SelectFullTasksByDate
import database.SelectFullTasksByType
import database.SelectTaskWithAllTimeContentById
import database.SelectTaskWithContentById
import database.SelectTasksWithContentBySearchQuery
import database.TagTable
import database.TaskContentTable
import database.TaskTable
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.LocalDate
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
    startEpochDay = startDate.toEpochDay(),
    endEpochDay = endDate.toEpochDay(),
    priority = priority.toLong(),
    createdAt = createdAt,
    deletedAt = deletedAt
)

fun TaskTable.toTaskEntity(json: Json) = TaskEntity(
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
    priority = this.priority.toInt(),
    createdAt = this.createdAt,
    deletedAt = this.deletedAt
)

fun BaseTaskContentEntity.toTaskContentTable(json: Json) =
    TaskContentTable(
        id = id,
        taskId = taskId,
        content = content.toJson(json),
        contentType = when (this) {
            is ProgressContentEntity -> TaskContentEntity.Type.Progress
            is FrequencyContentEntity -> TaskContentEntity.Type.Frequency
            is ArchiveContentEntity -> TaskContentEntity.Type.Archive
        }.toJson(json),
        startEpochDay = startDate.toEpochDay(),
        createdAt = createdAt
    )

fun TaskContentTable.toBaseTaskContentEntity(json: Json): BaseTaskContentEntity? = try {
    when (val decodedContent = content.fromJsonTaskContentEntity(json)) {
        is TaskContentEntity.ProgressContent -> {
            ProgressContentEntity(
                id = id,
                taskId = taskId,
                content = decodedContent,
                startDate = startEpochDay.toLocalDate(),
                createdAt = createdAt
            )
        }

        is TaskContentEntity.FrequencyContent -> {
            FrequencyContentEntity(
                id = id,
                taskId = taskId,
                content = decodedContent,
                startDate = startEpochDay.toLocalDate(),
                createdAt = createdAt
            )
        }

        is TaskContentEntity.ArchiveContent -> {
            ArchiveContentEntity(
                id = id,
                taskId = taskId,
                content = decodedContent,
                startDate = startEpochDay.toLocalDate(),
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

internal fun ReminderContentEntity.ScheduleContent.toJson(json: Json) =
    json.encodeToString<ReminderContentEntity.ScheduleContent>(this)

private fun String.fromJsonScheduleContent(json: Json) =
    json.decodeFromString<ReminderContentEntity.ScheduleContent>(this)

internal fun LocalTime.toJson(json: Json) =
    json.encodeToString<LocalTime>(this)

private fun String.fromJsonTime(json: Json) =
    json.decodeFromString<LocalTime>(this)

internal fun ReminderType.toJson(json: Json) =
    json.encodeToString(this)

private fun String.fromJsonReminderType(json: Json) =
    json.decodeFromString<ReminderType>(this)


/** record **/
fun RecordTable.toRecordEntity(json: Json) = RecordEntity(
    id = id,
    taskId = taskId,
    date = epochDay.toLocalDate(),
    entry = entry.fromJsonRecordEntry(json),
    createdAt = createdAt
)

fun RecordEntity.toRecordTable(json: Json) = RecordTable(
    id = id,
    taskId = taskId,
    epochDay = date.toEpochDay(),
    entry = entry.toJson(json),
    createdAt = createdAt
)

internal fun RecordContentEntity.Entry.toJson(json: Json) =
    json.encodeToString<RecordContentEntity.Entry>(this)

internal fun String.fromJsonRecordEntry(json: Json) =
    json.decodeFromString<RecordContentEntity.Entry>(this)

/** tag model **/
fun TagTable.toTagEntity() = TagEntity(
    id = id,
    title = title,
    createdAt = createdAt
)

fun TagEntity.toTagTable() = TagTable(
    id = id,
    title = title,
    createdAt = createdAt
)

/** complex **/

suspend fun TaskTable.toTaskWithContentEntity(
    allTaskContent: List<TaskContentTable>,
    json: Json
): TaskWithContentEntity? {
    return coroutineScope {
        val task = async {
            this@toTaskWithContentEntity.toTaskEntity(json)
        }
        val progressContent = async {
            allTaskContent.toBaseTaskContentEntity(
                contentType = TaskContentEntity.Type.Progress,
                json = json
            ) as? ProgressContentEntity
        }

        val frequencyContent = async {
            allTaskContent.toBaseTaskContentEntity(
                contentType = TaskContentEntity.Type.Frequency,
                json = json
            ) as? FrequencyContentEntity
        }

        val archiveContent = async {
            allTaskContent.toBaseTaskContentEntity(
                contentType = TaskContentEntity.Type.Archive,
                json = json
            ) as? ArchiveContentEntity
        }

        TaskWithContentEntity(
            task = task.await(),
            progressContent = progressContent.await() ?: return@coroutineScope null,
            frequencyContent = frequencyContent.await() ?: return@coroutineScope null,
            archiveContent = archiveContent.await() ?: return@coroutineScope null
        )
    }
}

internal fun List<TaskContentTable>.toBaseTaskContentEntity(
    contentType: TaskContentEntity.Type,
    json: Json
): BaseTaskContentEntity? = this.let { allTaskContent ->
    val encodedContentType = contentType.toJson(json)
    allTaskContent
        .find { it.contentType == encodedContentType }
        ?.toBaseTaskContentEntity(json)
}

/** other **/

internal fun LocalDate?.toEpochDay() = (this ?: DataConst.distantFutureDate).toEpochDays().toLong()
internal fun Long.toLocalDate() = LocalDate.fromEpochDays(this.toInt())

/** query mappings  **/
fun SelectTaskWithContentById.toTaskTable(): TaskTable {
    return TaskTable(
        id = task_id,
        type = task_type,
        progressType = task_progressType,
        title = task_title,
        description = task_description,
        startEpochDay = task_startEpochDay,
        endEpochDay = task_endEpochDay,
        priority = task_priority,
        createdAt = task_createdAt,
        deletedAt = task_deletedAt
    )
}

fun SelectTaskWithContentById.toTaskContentTable(): TaskContentTable {
    return TaskContentTable(
        id = taskContent_id,
        taskId = taskContent_taskId,
        contentType = taskContent_contentType,
        content = taskContent_content,
        startEpochDay = taskContent_startEpochDay,
        createdAt = taskContent_createdAt
    )
}

// other

internal fun SelectFullTasksByDate.toSelectFullTasksQuery() = SelectFullTasksQuery(
    task_id = task_id,
    task_type = task_type,
    task_progressType = task_progressType,
    task_title = task_title,
    task_description = task_description,
    task_startEpochDay = task_startEpochDay,
    task_endEpochDay = task_endEpochDay,
    task_priority = task_priority,
    task_createdAt = task_createdAt,
    task_deletedAt = task_deletedAt,
    taskContent_id = taskContent_id,
    taskContent_taskId = taskContent_taskId,
    taskContent_contentType = taskContent_contentType,
    taskContent_content = taskContent_content,
    taskContent_startEpochDay = taskContent_startEpochDay,
    taskContent_createdAt = taskContent_createdAt,
    reminder_id = reminder_id,
    reminder_taskId = reminder_taskId,
    reminder_time = reminder_time,
    reminder_type = reminder_type,
    reminder_schedule = reminder_schedule,
    reminder_createdAt = reminder_createdAt,
    tagCross_taskId = tagCross_taskId,
    tagCross_tagId = tagCross_tagId,
    tag_id = tag_id,
    tag_title = tag_title,
    tag_createdAt = tag_createdAt
)

internal fun SelectFullTasksByType.toSelectFullTasksQuery() = SelectFullTasksQuery(
    task_id = task_id,
    task_type = task_type,
    task_progressType = task_progressType,
    task_title = task_title,
    task_description = task_description,
    task_startEpochDay = task_startEpochDay,
    task_endEpochDay = task_endEpochDay,
    task_priority = task_priority,
    task_createdAt = task_createdAt,
    task_deletedAt = task_deletedAt,
    taskContent_id = taskContent_id,
    taskContent_taskId = taskContent_taskId,
    taskContent_contentType = taskContent_contentType,
    taskContent_content = taskContent_content,
    taskContent_startEpochDay = taskContent_startEpochDay,
    taskContent_createdAt = taskContent_createdAt,
    reminder_id = reminder_id,
    reminder_taskId = reminder_taskId,
    reminder_time = reminder_time,
    reminder_type = reminder_type,
    reminder_schedule = reminder_schedule,
    reminder_createdAt = reminder_createdAt,
    tagCross_taskId = tagCross_taskId,
    tagCross_tagId = tagCross_tagId,
    tag_id = tag_id,
    tag_title = tag_title,
    tag_createdAt = tag_createdAt
)

/** query mappings  **/
fun SelectFullTasksQuery.toTaskTable(): TaskTable {
    return TaskTable(
        id = task_id,
        type = task_type,
        progressType = task_progressType,
        title = task_title,
        description = task_description,
        startEpochDay = task_startEpochDay,
        endEpochDay = task_endEpochDay,
        priority = task_priority,
        createdAt = task_createdAt,
        deletedAt = task_deletedAt
    )
}

fun SelectFullTasksQuery.toTaskContentTable(): TaskContentTable {
    return TaskContentTable(
        id = taskContent_id,
        taskId = taskContent_taskId,
        contentType = taskContent_contentType,
        content = taskContent_content,
        startEpochDay = taskContent_startEpochDay,
        createdAt = taskContent_createdAt
    )
}

fun SelectFullTasksQuery.toReminderTable(): ReminderTable? {
    return ReminderTable(
        id = reminder_id ?: return null,
        taskId = reminder_taskId ?: return null,
        type = reminder_type ?: return null,
        schedule = reminder_schedule ?: return null,
        time = reminder_time ?: return null,
        createdAt = reminder_createdAt ?: return null
    )
}

fun SelectFullTasksQuery.toTagTable(): TagTable? {
    return TagTable(
        id = tag_id ?: return null,
        title = tag_title ?: return null,
        createdAt = tag_createdAt ?: return null
    )
}


fun SelectTasksWithContentBySearchQuery.toTaskTable(): TaskTable {
    return TaskTable(
        id = task_id,
        type = task_type,
        progressType = task_progressType,
        title = task_title,
        description = task_description,
        startEpochDay = task_startEpochDay,
        endEpochDay = task_endEpochDay,
        priority = task_priority,
        createdAt = task_createdAt,
        deletedAt = task_deletedAt
    )
}

fun SelectTasksWithContentBySearchQuery.toTaskContentTable(): TaskContentTable {
    return TaskContentTable(
        id = taskContent_id,
        taskId = taskContent_taskId,
        contentType = taskContent_contentType,
        content = taskContent_content,
        startEpochDay = taskContent_startEpochDay,
        createdAt = taskContent_createdAt
    )
}

fun SelectTaskWithAllTimeContentById.toTaskTable(): TaskTable {
    return TaskTable(
        id = task_id,
        type = task_type,
        progressType = task_progressType,
        title = task_title,
        description = task_description,
        startEpochDay = task_startEpochDay,
        endEpochDay = task_endEpochDay,
        priority = task_priority,
        createdAt = task_createdAt,
        deletedAt = task_deletedAt
    )
}

fun SelectTaskWithAllTimeContentById.toTaskContentTable(): TaskContentTable {
    return TaskContentTable(
        id = taskContent_id,
        taskId = taskContent_taskId,
        contentType = taskContent_contentType,
        content = taskContent_content,
        startEpochDay = taskContent_startEpochDay,
        createdAt = taskContent_createdAt
    )
}