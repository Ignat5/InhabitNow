package com.example.inhabitnow.android.presentation.view_schedule.config.enter_time_record.components

import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.domain.model.task.TaskModel
import kotlinx.datetime.LocalDate

data class EnterTaskTimeRecordScreenState(
    val inputHours: Int,
    val inputMinutes: Int,
    val task: TaskModel.Habit.HabitContinuous.HabitTime,
    val date: LocalDate
) : ScreenState
