package com.example.inhabitnow.domain.util

import com.example.inhabitnow.data.model.reminder.ReminderEntity
import com.example.inhabitnow.data.model.reminder.content.ReminderContentEntity
import com.example.inhabitnow.data.model.task.TaskEntity
import com.example.inhabitnow.data.model.task.TaskWithContentEntity
import com.example.inhabitnow.data.model.task.content.ArchiveContentEntity
import com.example.inhabitnow.data.model.task.content.FrequencyContentEntity
import com.example.inhabitnow.data.model.task.content.ProgressContentEntity
import com.example.inhabitnow.data.model.task.content.TaskContentEntity
import com.example.inhabitnow.domain.model.reminder.ReminderModel
import com.example.inhabitnow.domain.model.reminder.content.ReminderContentModel
import com.example.inhabitnow.domain.model.task.TaskModel
import com.example.inhabitnow.domain.model.task.TaskWithContentModel
import com.example.inhabitnow.domain.model.task.content.TaskContentModel
import kotlinx.datetime.LocalDate

/** task **/

fun TaskWithContentEntity.toTaskWithContentModel() = TaskWithContentModel(
    task = this.task.toTaskModel(),
    progressContent = this.progressContent.toProgressContentModel(),
    frequencyContent = this.frequencyContent.toFrequencyContentModel(),
    archiveContent = this.archiveContent.toArchiveContentModel()
)

private fun TaskEntity.toTaskModel() = TaskModel(
    id = id,
    type = type,
    progressType = progressType,
    title = title,
    description = description,
    startDate = startEpochDay.toLocalDate(),
    endDate = this.endEpochDay.let { epochDay ->
        if (isDistantFuture(epochDay)) null
        else epochDay.toLocalDate()
    },
    priority = priority,
    createdAt = createdAt,
    deletedAt = deletedAt
)

private fun ProgressContentEntity.toProgressContentModel(): TaskContentModel.ProgressContent =
    when (val content = this.content) {
        is TaskContentEntity.ProgressContent.YesNo -> TaskContentModel.ProgressContent.YesNo
        is TaskContentEntity.ProgressContent.Number -> TaskContentModel.ProgressContent.Number(
            limitType = content.limitType,
            limitNumber = content.limitNumber,
            limitUnit = content.limitUnit
        )

        is TaskContentEntity.ProgressContent.Time -> TaskContentModel.ProgressContent.Time(
            limitType = content.limitType,
            limitTime = content.limitTime
        )
    }

private fun FrequencyContentEntity.toFrequencyContentModel(): TaskContentModel.FrequencyContent =
    when (val content = this.content) {
        is TaskContentEntity.FrequencyContent.OneDay -> TaskContentModel.FrequencyContent.OneDay
        is TaskContentEntity.FrequencyContent.EveryDay -> TaskContentModel.FrequencyContent.Period.EveryDay
        is TaskContentEntity.FrequencyContent.DaysOfWeek -> TaskContentModel.FrequencyContent.Period.DaysOfWeek(
            content.daysOfWeek
        )
    }

private fun ArchiveContentEntity.toArchiveContentModel(): TaskContentModel.ArchiveContent =
    TaskContentModel.ArchiveContent(this.content.isArchived)

/** reminder **/
fun ReminderEntity.toReminderModel() = ReminderModel(
    id = this.id,
    taskId = this.taskId,
    type = this.type,
    time = this.time,
    schedule = this.schedule.toScheduleContentModel(),
    createdAt = this.createdAt
)

private fun ReminderContentEntity.ScheduleContent.toScheduleContentModel() =
    when (this) {
        is ReminderContentEntity.ScheduleContent.EveryDay -> ReminderContentModel.ScheduleContent.EveryDay
        is ReminderContentEntity.ScheduleContent.DaysOfWeek -> ReminderContentModel.ScheduleContent.DaysOfWeek(this.daysOfWeek)
    }

/** other **/

private fun Long.toLocalDate(): LocalDate = this.let { epochDay ->
    LocalDate.fromEpochDays(epochDay.toInt())
}

private fun isDistantFuture(epochDay: Long): Boolean = epochDay == DomainConst.distantFutureEpochDay