package com.example.inhabitnow.android.ui

import com.example.inhabitnow.android.presentation.model.UITaskContent
import com.example.inhabitnow.core.type.TaskType
import com.example.inhabitnow.domain.model.task.TaskModel
import com.example.inhabitnow.domain.model.task.content.TaskContentModel

fun TaskContentModel.ProgressContent.toUIProgressContent(): UITaskContent.Progress? {
    return when (this) {
        is TaskContentModel.ProgressContent.Number -> UITaskContent.Progress.Number(
            limitType = this.limitType,
            limitNumber = this.limitNumber,
            limitUnit = this.limitUnit
        )

        is TaskContentModel.ProgressContent.Time -> UITaskContent.Progress.Time(
            limitType = this.limitType,
            limitTime = this.limitTime
        )

        else -> null
    }
}

fun TaskContentModel.FrequencyContent.toUIFrequencyContent(): UITaskContent.Frequency? {
    return when (this) {
        is TaskContentModel.FrequencyContent.EveryDay -> UITaskContent.Frequency.EveryDay
        is TaskContentModel.FrequencyContent.DaysOfWeek ->
            UITaskContent.Frequency.DaysOfWeek(this.daysOfWeek)

        else -> null
    }
}

fun TaskModel.toUIDateContent(): UITaskContent.Date {
    return when (this.type) {
        TaskType.SingleTask -> UITaskContent.Date.OneDay(this.startDate)
        TaskType.RecurringTask, TaskType.Habit -> UITaskContent.Date.Period(
            startDate = this.startDate, endDate = this.endDate
        )
    }
}

fun UITaskContent.Frequency.toFrequencyContent(): TaskContentModel.FrequencyContent {
    return when (this) {
        is UITaskContent.Frequency.EveryDay -> TaskContentModel.FrequencyContent.EveryDay
        is UITaskContent.Frequency.DaysOfWeek -> TaskContentModel.FrequencyContent.DaysOfWeek(this.daysOfWeek)
    }
}