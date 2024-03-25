package com.example.inhabitnow.android.presentation.view_schedule.model

import com.example.inhabitnow.domain.model.record.content.RecordContentModel
import com.example.inhabitnow.domain.model.task.content.TaskContentModel

sealed interface TaskScheduleStatus {
    val statusType: StatusType

    sealed interface Habit : TaskScheduleStatus {
        override val statusType: StatusType.Habit

        sealed interface Continuous : Habit {
            override val statusType: StatusType.Habit.Continuous
            val progressContent: TaskContentModel.ProgressContent
            val record: RecordContentModel.Entry?

            data class Number(
                override val statusType: StatusType.Habit.Continuous,
                override val progressContent: TaskContentModel.ProgressContent.Number,
                override val record: RecordContentModel.Entry.Number?
            ) : Continuous

            data class Time(
                override val statusType: StatusType.Habit.Continuous,
                override val progressContent: TaskContentModel.ProgressContent.Time,
                override val record: RecordContentModel.Entry.Time?
            ) : Continuous
        }

        data class YesNo(override val statusType: StatusType.Habit.YesNo) : Habit
    }

    data class Task(
        override val statusType: StatusType.Task
    ) : TaskScheduleStatus

    sealed interface StatusType {
        // all
        data object Pending : Habit.Continuous, Habit.YesNo, Task
        data object Done : Habit.Continuous, Habit.YesNo, Task
//        data object Locked : Habit.Continuous, Habit.YesNo, Task

        // habit
        data object Skipped : Habit.Continuous, Habit.YesNo
        data object Failed : Habit.Continuous, Habit.YesNo

        // habit.continuous
        data object InProgress : Habit.Continuous

        sealed interface Habit : StatusType {
            sealed interface Continuous : Habit
            sealed interface YesNo : Habit
        }

        sealed interface Task : StatusType
    }
//    enum class Type { Pending, InProgress, Done, Skipped, Failed, Locked }
}