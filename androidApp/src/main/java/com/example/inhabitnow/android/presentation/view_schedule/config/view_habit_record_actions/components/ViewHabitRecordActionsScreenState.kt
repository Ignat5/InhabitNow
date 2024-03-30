package com.example.inhabitnow.android.presentation.view_schedule.config.view_habit_record_actions.components

import androidx.compose.runtime.Stable
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.android.presentation.view_schedule.config.view_habit_record_actions.model.ItemHabitRecordAction
import com.example.inhabitnow.android.presentation.view_schedule.model.TaskWithRecordModel
import kotlinx.datetime.LocalDate

@Stable
data class ViewHabitRecordActionsScreenState(
    val allActionItems: List<ItemHabitRecordAction>,
    val taskWithRecord: TaskWithRecordModel.Habit,
    val date: LocalDate
) : ScreenState
