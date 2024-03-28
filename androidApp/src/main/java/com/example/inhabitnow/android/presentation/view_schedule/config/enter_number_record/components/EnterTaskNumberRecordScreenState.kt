package com.example.inhabitnow.android.presentation.view_schedule.config.enter_number_record.components

import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.domain.model.task.TaskModel
import com.example.inhabitnow.domain.model.task.content.TaskContentModel
import kotlinx.datetime.LocalDate

data class EnterTaskNumberRecordScreenState(
    val inputNumber: String,
    val task: TaskModel.Habit.HabitContinuous.HabitNumber,
    val canConfirm: Boolean,
    val inputValidator: (String) -> Boolean,
    val date: LocalDate
) : ScreenState
