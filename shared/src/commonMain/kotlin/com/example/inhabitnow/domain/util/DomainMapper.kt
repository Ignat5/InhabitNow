package com.example.inhabitnow.domain.util

import com.example.inhabitnow.data.model.task.TaskEntity
import com.example.inhabitnow.data.model.task.TaskWithContentEntity
import com.example.inhabitnow.data.model.task.content.ProgressContentEntity
import com.example.inhabitnow.data.model.task.content.TaskContentEntity
import com.example.inhabitnow.domain.model.task.TaskModel
import com.example.inhabitnow.domain.model.task.TaskWithContentModel
import com.example.inhabitnow.domain.model.task.content.TaskContentModel

internal fun TaskWithContentEntity.toTaskWithContentModel(): TaskWithContentModel {
    return TaskWithContentModel(
        task = this.task.toTaskModel(),
        progressContent = this.progressContent.content.toProgressContentModel(),
        frequencyContent = this.frequencyContent.content.toFrequencyContentModel(),
        archiveContent = this.archiveContent.content.toArchiveContentModel()
    )
}

internal fun TaskEntity.toTaskModel(): TaskModel {
    return TaskModel(
        id = id,
        type = type,
        progressType = progressType,
        title = title,
        description = description,
        startDate = startDate,
        endDate = endDate,
        priority = priority,
        createdAt = createdAt,
        deletedAt = deletedAt
    )
}

private fun TaskContentEntity.ProgressContent.toProgressContentModel(): TaskContentModel.ProgressContent {
    return when (this) {
        is TaskContentEntity.ProgressContent.YesNo -> TaskContentModel.ProgressContent.YesNo
        is TaskContentEntity.ProgressContent.Number -> TaskContentModel.ProgressContent.Number(
            limitType = this.limitType,
            limitNumber = this.limitNumber,
            limitUnit = this.limitUnit
        )

        is TaskContentEntity.ProgressContent.Time -> TaskContentModel.ProgressContent.Time(
            limitType = this.limitType,
            limitTime = this.limitTime
        )
    }
}

internal fun TaskContentModel.ProgressContent.toProgressContentEntity(): TaskContentEntity.ProgressContent {
    return when (this) {
        is TaskContentModel.ProgressContent.YesNo -> TaskContentEntity.ProgressContent.YesNo
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
        is TaskContentEntity.FrequencyContent.OneDay -> TaskContentModel.FrequencyContent.OneDay
        is TaskContentEntity.FrequencyContent.EveryDay -> TaskContentModel.FrequencyContent.EveryDay
        is TaskContentEntity.FrequencyContent.DaysOfWeek -> TaskContentModel.FrequencyContent.DaysOfWeek(
            this.daysOfWeek
        )
    }
}

internal fun TaskContentModel.FrequencyContent.toFrequencyContentEntity(): TaskContentEntity.FrequencyContent {
    return when (this) {
        is TaskContentModel.FrequencyContent.OneDay -> TaskContentEntity.FrequencyContent.OneDay
        is TaskContentModel.FrequencyContent.EveryDay -> TaskContentEntity.FrequencyContent.EveryDay
        is TaskContentModel.FrequencyContent.DaysOfWeek -> TaskContentEntity.FrequencyContent.DaysOfWeek(
            this.daysOfWeek
        )
    }
}

private fun TaskContentEntity.ArchiveContent.toArchiveContentModel(): TaskContentModel.ArchiveContent {
    return TaskContentModel.ArchiveContent(this.isArchived)
}