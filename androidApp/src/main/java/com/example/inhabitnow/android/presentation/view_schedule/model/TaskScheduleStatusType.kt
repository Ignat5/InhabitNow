package com.example.inhabitnow.android.presentation.view_schedule.model

sealed interface TaskScheduleStatusType {
    // all
    data object Pending : Habit.Continuous, Habit.YesNo, Task
    data object Done : Habit.Continuous, Habit.YesNo, Task
//    data object Locked : Habit.Continuous, Habit.YesNo, Task

    // habit
    data object Skipped : Habit.Continuous, Habit.YesNo
    data object Failed : Habit.Continuous, Habit.YesNo

    // habit.continuous
    data object InProgress : Habit.Continuous

    sealed interface Habit : TaskScheduleStatusType {
        sealed interface Continuous : Habit
        sealed interface YesNo : Habit
    }

    sealed interface Task : TaskScheduleStatusType
}