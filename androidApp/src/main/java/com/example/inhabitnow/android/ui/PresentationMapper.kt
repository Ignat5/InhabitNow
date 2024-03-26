package com.example.inhabitnow.android.ui

import com.example.inhabitnow.android.presentation.model.UIReminderContent
import com.example.inhabitnow.android.presentation.model.UITaskContent
import com.example.inhabitnow.core.type.TaskType
import com.example.inhabitnow.domain.model.reminder.content.ReminderContentModel
import com.example.inhabitnow.domain.model.task.TaskModel
import com.example.inhabitnow.domain.model.task.content.TaskContentModel

fun TaskContentModel.ProgressContent.toUIProgressContent(): UITaskContent.Progress {
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
    }
}

fun TaskContentModel.FrequencyContent.toUIFrequencyContent(): UITaskContent.Frequency {
    return when (this) {
        is TaskContentModel.FrequencyContent.EveryDay -> UITaskContent.Frequency.EveryDay
        is TaskContentModel.FrequencyContent.DaysOfWeek ->
            UITaskContent.Frequency.DaysOfWeek(this.daysOfWeek)

//        else -> null
    }
}

//fun TaskModel.toUIDateContent(): UITaskContent.Date {
//    return when (this.type) {
//        TaskType.SingleTask -> UITaskContent.Date.OneDay(this.startDate)
//        TaskType.RecurringTask, TaskType.Habit -> UITaskContent.Date.Period(
//            startDate = this.startDate, endDate = this.endDate
//        )
//    }
//}

fun UITaskContent.Frequency.toFrequencyContent(): TaskContentModel.FrequencyContent {
    return when (this) {
        is UITaskContent.Frequency.EveryDay -> TaskContentModel.FrequencyContent.EveryDay
        is UITaskContent.Frequency.DaysOfWeek -> TaskContentModel.FrequencyContent.DaysOfWeek(this.daysOfWeek)
    }
}

fun UIReminderContent.Schedule.toScheduleContent(): ReminderContentModel.ScheduleContent {
    return when (this) {
        is UIReminderContent.Schedule.EveryDay -> ReminderContentModel.ScheduleContent.EveryDay
        is UIReminderContent.Schedule.DaysOfWeek -> ReminderContentModel.ScheduleContent.DaysOfWeek(
            this.daysOfWeek
        )
    }
}

fun ReminderContentModel.ScheduleContent.toUIScheduleContent(): UIReminderContent.Schedule {
    return when (this) {
        is ReminderContentModel.ScheduleContent.EveryDay -> UIReminderContent.Schedule.EveryDay
        is ReminderContentModel.ScheduleContent.DaysOfWeek -> UIReminderContent.Schedule.DaysOfWeek(
            this.daysOfWeek
        )
    }
}