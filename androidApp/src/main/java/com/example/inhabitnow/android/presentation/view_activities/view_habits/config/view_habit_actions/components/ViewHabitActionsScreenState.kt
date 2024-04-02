package com.example.inhabitnow.android.presentation.view_activities.view_habits.config.view_habit_actions.components

import androidx.compose.runtime.Stable
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.android.presentation.view_activities.model.ItemTaskAction
import com.example.inhabitnow.domain.model.task.TaskModel

@Stable
data class ViewHabitActionsScreenState(
    val habitModel: TaskModel.Habit,
    val allActionItems: List<ItemTaskAction.HabitAction>
) : ScreenState
