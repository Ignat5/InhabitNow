package com.example.inhabitnow.android.presentation.view_schedule.model

import com.example.inhabitnow.domain.model.record.content.RecordContentModel
import com.example.inhabitnow.domain.model.task.TaskModel

sealed interface TaskWithRecordModel {
    val task: TaskModel
    val recordEntry: RecordContentModel.Entry?
    val statusType: TaskScheduleStatusType

    sealed interface Habit : TaskWithRecordModel {
        abstract override val task: TaskModel.Habit
        abstract override val recordEntry: RecordContentModel.Entry.HabitEntry?
        abstract override val statusType: TaskScheduleStatusType.Habit

        sealed interface HabitContinuous : Habit {
            abstract override val task: TaskModel.Habit.HabitContinuous
            abstract override val recordEntry: RecordContentModel.Entry.HabitEntry.HabitContinuousEntry?

            data class HabitNumber(
                override val task: TaskModel.Habit.HabitContinuous.HabitNumber,
                override val recordEntry: RecordContentModel.Entry.HabitEntry.HabitContinuousEntry.Number?,
                override val statusType: TaskScheduleStatusType.Habit.Continuous
            ) : HabitContinuous

            data class HabitTime(
                override val task: TaskModel.Habit.HabitContinuous.HabitTime,
                override val recordEntry: RecordContentModel.Entry.HabitEntry.HabitContinuousEntry.Time?,
                override val statusType: TaskScheduleStatusType.Habit.Continuous
            ) : HabitContinuous

        }

        data class HabitYesNo(
            override val task: TaskModel.Habit.HabitYesNo,
            override val recordEntry: RecordContentModel.Entry.HabitEntry.HabitYesNoEntry?,
            override val statusType: TaskScheduleStatusType.Habit.YesNo
        ) : Habit
    }

    data class Task(
        override val task: TaskModel.Task,
        override val recordEntry: RecordContentModel.Entry.TaskEntry?,
        override val statusType: TaskScheduleStatusType.Task
    ) : TaskWithRecordModel
}