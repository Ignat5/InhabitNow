package com.example.inhabitnow.domain.util

import com.example.inhabitnow.core.type.TaskProgressType
import com.example.inhabitnow.core.type.TaskType
import com.example.inhabitnow.data.model.record.RecordEntity
import com.example.inhabitnow.data.model.record.content.RecordContentEntity
import com.example.inhabitnow.data.model.reminder.ReminderEntity
import com.example.inhabitnow.data.model.reminder.content.ReminderContentEntity
import com.example.inhabitnow.data.model.tag.TagEntity
import com.example.inhabitnow.data.model.task.TaskWithContentEntity
import com.example.inhabitnow.data.model.task.content.TaskContentEntity
import com.example.inhabitnow.data.model.task.derived.FullTaskEntity
import com.example.inhabitnow.domain.model.record.RecordModel
import com.example.inhabitnow.domain.model.record.content.RecordContentModel
import com.example.inhabitnow.domain.model.reminder.ReminderModel
import com.example.inhabitnow.domain.model.reminder.content.ReminderContentModel
import com.example.inhabitnow.domain.model.tag.TagModel
import com.example.inhabitnow.domain.model.task.TaskModel
import com.example.inhabitnow.domain.model.task.content.TaskContentModel
import com.example.inhabitnow.domain.model.task.derived.FullTaskModel

// task

internal fun TaskWithContentEntity.toTaskModel(): TaskModel =
    when (this.task.type) {
        TaskType.Habit -> {
            when (this.task.progressType) {
                TaskProgressType.YesNo -> {
                    TaskModel.Habit.HabitYesNo(
                        id = this.task.id,
                        title = this.task.title,
                        description = this.task.description,
                        priority = this.task.priority,
                        isArchived = this.archiveContent.content.isArchived,
                        frequencyContent = this.frequencyContent.content.toFrequencyContentModel(),
                        dateContent = TaskContentModel.DateContent.Period(
                            startDate = this.task.startDate,
                            endDate = this.task.endDate
                        ),
                        createdAt = this.task.createdAt
                    )
                }

                TaskProgressType.Number -> {
                    TaskModel.Habit.HabitContinuous.HabitNumber(
                        id = this.task.id,
                        title = this.task.title,
                        description = this.task.description,
                        priority = this.task.priority,
                        isArchived = this.archiveContent.content.isArchived,
                        progressContent = this.progressContent.content.toProgressContentModel() as TaskContentModel.ProgressContent.Number,
                        frequencyContent = this.frequencyContent.content.toFrequencyContentModel(),
                        dateContent = TaskContentModel.DateContent.Period(
                            startDate = this.task.startDate,
                            endDate = this.task.endDate
                        ),
                        createdAt = this.task.createdAt
                    )
                }

                TaskProgressType.Time -> {
                    TaskModel.Habit.HabitContinuous.HabitTime(
                        id = this.task.id,
                        title = this.task.title,
                        description = this.task.description,
                        priority = this.task.priority,
                        isArchived = this.archiveContent.content.isArchived,
                        progressContent = this.progressContent.content.toProgressContentModel() as TaskContentModel.ProgressContent.Time,
                        frequencyContent = this.frequencyContent.content.toFrequencyContentModel(),
                        dateContent = TaskContentModel.DateContent.Period(
                            startDate = this.task.startDate,
                            endDate = this.task.endDate
                        ),
                        createdAt = this.task.createdAt
                    )
                }
            }
        }

        TaskType.SingleTask -> {
            TaskModel.Task.SingleTask(
                id = this.task.id,
                title = this.task.title,
                description = this.task.description,
                priority = this.task.priority,
                isArchived = this.archiveContent.content.isArchived,
                dateContent = TaskContentModel.DateContent.Day(
                    date = this.task.startDate
                ),
                createdAt = this.task.createdAt
            )
        }

        TaskType.RecurringTask -> {
            TaskModel.Task.RecurringTask(
                id = this.task.id,
                title = this.task.title,
                description = this.task.description,
                priority = this.task.priority,
                isArchived = this.archiveContent.content.isArchived,
                frequencyContent = this.frequencyContent.content.toFrequencyContentModel(),
                dateContent = TaskContentModel.DateContent.Period(
                    startDate = this.task.startDate,
                    endDate = this.task.endDate
                ),
                createdAt = this.task.createdAt
            )
        }
    }

private fun TaskContentEntity.ProgressContent.toProgressContentModel(): TaskContentModel.ProgressContent {
    return when (this) {
        is TaskContentEntity.ProgressContent.Number -> TaskContentModel.ProgressContent.Number(
            limitType = this.limitType,
            limitNumber = this.limitNumber,
            limitUnit = this.limitUnit
        )

        is TaskContentEntity.ProgressContent.Time -> TaskContentModel.ProgressContent.Time(
            limitType = this.limitType,
            limitTime = this.limitTime
        )

        else -> throw IllegalStateException()
    }
}

internal fun TaskContentModel.ProgressContent.toProgressContentEntity(): TaskContentEntity.ProgressContent {
    return when (this) {
        is TaskContentModel.ProgressContent.Number -> TaskContentEntity.ProgressContent.Number(
            limitType = this.limitType,
            limitNumber = this.limitNumber,
            limitUnit = this.limitUnit
        )

        is TaskContentModel.ProgressContent.Time -> TaskContentEntity.ProgressContent.Time(
            limitType = this.limitType,
            limitTime = this.limitTime
        )
    }
}

private fun TaskContentEntity.FrequencyContent.toFrequencyContentModel(): TaskContentModel.FrequencyContent {
    return when (this) {
        is TaskContentEntity.FrequencyContent.EveryDay -> TaskContentModel.FrequencyContent.EveryDay
        is TaskContentEntity.FrequencyContent.DaysOfWeek -> TaskContentModel.FrequencyContent.DaysOfWeek(
            this.daysOfWeek
        )

        else -> throw IllegalStateException()
    }
}

internal fun TaskContentModel.FrequencyContent.toFrequencyContentEntity(): TaskContentEntity.FrequencyContent {
    return when (this) {
//        is TaskContentModel.FrequencyContent.OneDay -> TaskContentEntity.FrequencyContent.OneDay
        is TaskContentModel.FrequencyContent.EveryDay -> TaskContentEntity.FrequencyContent.EveryDay
        is TaskContentModel.FrequencyContent.DaysOfWeek -> TaskContentEntity.FrequencyContent.DaysOfWeek(
            this.daysOfWeek
        )
    }
}

//private fun TaskContentEntity.ArchiveContent.toArchiveContentModel(): TaskContentModel.ArchiveContent {
//    return TaskContentModel.ArchiveContent(this.isArchived)
//}

internal fun FullTaskEntity.toFullTaskModel() = FullTaskModel(
    taskModel = taskWithContentEntity.toTaskModel(),
    allReminders = allReminders.map { it.toReminderModel() },
    allTags = allTags.map { it.toTagModel() }
)

// reminder

internal fun ReminderEntity.toReminderModel() = ReminderModel(
    id = id,
    taskId = taskId,
    type = type,
    time = time,
    schedule = schedule.toScheduleModel(),
    createdAt = createdAt
)

private fun ReminderContentEntity.ScheduleContent.toScheduleModel() =
    when (this) {
        is ReminderContentEntity.ScheduleContent.EveryDay -> ReminderContentModel.ScheduleContent.EveryDay
        is ReminderContentEntity.ScheduleContent.DaysOfWeek -> ReminderContentModel.ScheduleContent.DaysOfWeek(
            this.daysOfWeek
        )
    }

internal fun ReminderContentModel.ScheduleContent.toScheduleModel() =
    when (this) {
        is ReminderContentModel.ScheduleContent.EveryDay -> ReminderContentEntity.ScheduleContent.EveryDay
        is ReminderContentModel.ScheduleContent.DaysOfWeek -> ReminderContentEntity.ScheduleContent.DaysOfWeek(
            this.daysOfWeek
        )
    }

// tag
internal fun TagEntity.toTagModel() = TagModel(
    id = id,
    title = title,
    createdAt = createdAt
)

// record
internal fun RecordEntity.toRecordModel() = RecordModel(
    id = id,
    taskId = taskId,
    date = date,
    entry = entry.toRecordEntryModel(),
    createdAt = createdAt
)

private fun RecordContentEntity.Entry.toRecordEntryModel(): RecordContentModel.Entry =
    when (this) {
        is RecordContentEntity.Entry.Done -> RecordContentModel.Entry.Done
        is RecordContentEntity.Entry.Number -> RecordContentModel.Entry.Number(
            number = this.number
        )

        is RecordContentEntity.Entry.Time -> RecordContentModel.Entry.Time(
            time = this.time
        )

        is RecordContentEntity.Entry.Fail -> RecordContentModel.Entry.Fail
        is RecordContentEntity.Entry.Skip -> RecordContentModel.Entry.Skip
    }