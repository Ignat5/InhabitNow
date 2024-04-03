package com.example.inhabitnow.domain.util

import com.example.inhabitnow.core.type.ProgressLimitType
import com.example.inhabitnow.data.model.record.content.RecordContentEntity
import com.example.inhabitnow.data.model.task.content.TaskContentEntity
import com.example.inhabitnow.domain.model.statistics.TaskStatus
import kotlinx.datetime.LocalDate

object DomainUtil {
    fun validateInputLimitNumber(input: String): Boolean {
        return input.toDoubleOrNull()
            ?.let { it in DomainConst.MIN_LIMIT_NUMBER..DomainConst.MAX_LIMIT_NUMBER } ?: false
    }

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