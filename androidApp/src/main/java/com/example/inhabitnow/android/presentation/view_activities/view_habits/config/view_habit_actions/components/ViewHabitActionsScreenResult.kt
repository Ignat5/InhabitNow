package com.example.inhabitnow.android.presentation.view_activities.view_habits.config.view_habit_actions.components

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.android.presentation.view_activities.model.ItemTaskAction

sealed interface ViewHabitActionsScreenResult : ScreenResult {
    data class Action(
        val taskId: String,
        val action: ItemTaskAction.HabitAction
    ) : ViewHabitActionsScreenResult

    data class Edit(val taskId: String) : ViewHabitActionsScreenResult
    data object Dismiss : ViewHabitActionsScreenResult
}