package com.example.inhabitnow.domain.util

import com.example.inhabitnow.core.type.ProgressLimitType
import com.example.inhabitnow.data.model.record.content.RecordContentEntity
import com.example.inhabitnow.data.model.reminder.content.ReminderContentEntity
import com.example.inhabitnow.data.model.task.TaskWithContentEntity
import com.example.inhabitnow.data.model.task.content.TaskContentEntity
import com.example.inhabitnow.domain.model.statistics.TaskStatus
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal object DomainUtil {

    internal fun TaskContentEntity.FrequencyContent.checkIfTaskScheduled(targetDate: LocalDate): Boolean =
        this.let { fc ->
            when (fc) {
                is TaskContentEntity.FrequencyContent.EveryDay -> true
                is TaskContentEntity.FrequencyContent.OneDay -> true

                is TaskContentEntity.FrequencyContent.DaysOfWeek -> {
                    targetDate.dayOfWeek in fc.daysOfWeek
                }
            }
        }

    internal fun TaskWithContentEntity.checkIfActive(targetDate: LocalDate): Boolean =
        this.let { taskWithContentEntity ->
            val isDeleted = taskWithContentEntity.task.deletedAt != null
            val isArchived = taskWithContentEntity.archiveContent.content.isArchived
            val inDateRange = taskWithContentEntity.task.let { task ->
                val endDate = task.endDate
                    ?: Instant.DISTANT_FUTURE.toLocalDateTime(TimeZone.currentSystemDefault()).date
                targetDate in task.startDate..endDate
            }
            !isDeleted && !isArchived && inDateRange
        }

    internal fun TaskWithContentEntity.checkIfActive(): Boolean =
        this.let { taskWithContentEntity ->
            val isDeleted = taskWithContentEntity.task.deletedAt != null
            val isArchived = taskWithContentEntity.archiveContent.content.isArchived
            !isDeleted && !isArchived
        }

    internal fun ReminderContentEntity.ScheduleContent.checkIfScheduled(targetDate: LocalDate): Boolean =
        this.let { scheduleContent ->
            when (scheduleContent) {
                is ReminderContentEntity.ScheduleContent.EveryDay -> true
                is ReminderContentEntity.ScheduleContent.DaysOfWeek -> {
                    targetDate.dayOfWeek in scheduleContent.daysOfWeek
                }
            }
        }

    internal fun checkIfRemindersOverlap(
        sourceSchedule: ReminderContentEntity.ScheduleContent,
        targetSchedule: ReminderContentEntity.ScheduleContent
    ): Boolean {
        return when (sourceSchedule) {
            is ReminderContentEntity.ScheduleContent.EveryDay -> true
            is ReminderContentEntity.ScheduleContent.DaysOfWeek -> {
                when (targetSchedule) {
                    is ReminderContentEntity.ScheduleContent.EveryDay -> true
                    is ReminderContentEntity.ScheduleContent.DaysOfWeek -> {
                        sourceSchedule.daysOfWeek.any { it in targetSchedule.daysOfWeek }
                    }
                }
            }
        }
    }

    internal fun TaskContentEntity.ProgressContent.getTaskStatusByEntry(entry: RecordContentEntity.Entry?): TaskStatus {
        return when (entry) {
            null -> TaskStatus.NotCompleted.Pending
            is RecordContentEntity.Entry.Done -> TaskStatus.Completed
            is RecordContentEntity.Entry.Skip -> TaskStatus.NotCompleted.Skipped
            is RecordContentEntity.Entry.Fail -> TaskStatus.NotCompleted.Failed
            is RecordContentEntity.Entry.Number -> {
                (this as? TaskContentEntity.ProgressContent.Number)?.let { pc ->
                    val entryNumber = entry.number
                    val isDone = when (pc.limitType) {
                        ProgressLimitType.AtLeast -> entryNumber >= pc.limitNumber
                        ProgressLimitType.Exactly -> entryNumber == pc.limitNumber
                    }
                    if (isDone) TaskStatus.Completed
                    else TaskStatus.NotCompleted.Pending
                } ?: throw IllegalStateException()
            }

            is RecordContentEntity.Entry.Time -> {
                (this as? TaskContentEntity.ProgressContent.Time)?.let { pc ->
                    val entryTime = entry.time
                    val isDone = when (pc.limitType) {
                        ProgressLimitType.AtLeast -> entryTime >= pc.limitTime
                        ProgressLimitType.Exactly -> entryTime == pc.limitTime
                    }
                    if (isDone) TaskStatus.Completed
                    else TaskStatus.NotCompleted.Pending
                } ?: throw IllegalStateException()
            }
        }
    }
}