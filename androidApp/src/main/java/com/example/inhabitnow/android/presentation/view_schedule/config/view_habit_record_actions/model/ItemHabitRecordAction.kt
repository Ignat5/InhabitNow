package com.example.inhabitnow.android.presentation.view_schedule.config.view_habit_record_actions.model

import com.example.inhabitnow.android.presentation.view_schedule.model.TaskWithRecordModel

sealed interface ItemHabitRecordAction {
    sealed interface ContinuousProgress : ItemHabitRecordAction {
        val taskWithRecord: TaskWithRecordModel.Habit.HabitContinuous

        data class Number(
            override val taskWithRecord: TaskWithRecordModel.Habit.HabitContinuous.HabitNumber
        ) : ContinuousProgress

        data class Time(
            override val taskWithRecord: TaskWithRecordModel.Habit.HabitContinuous.HabitTime
        ) : ContinuousProgress
    }

    data object Done : ItemHabitRecordAction
    data object Fail : ItemHabitRecordAction
    data object Skip : ItemHabitRecordAction
    data object Reset : ItemHabitRecordAction
}